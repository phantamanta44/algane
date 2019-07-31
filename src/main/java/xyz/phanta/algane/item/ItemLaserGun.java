package xyz.phanta.algane.item;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import io.github.phantamanta44.libnine.util.helper.OptUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.init.AlganeItems;
import xyz.phanta.algane.item.base.TickingItem;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;
import xyz.phanta.algane.lasergun.item.LaserItemEnergyDelegator;
import xyz.phanta.algane.lasergun.item.LaserItemGunDelegator;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.TooltipUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemLaserGun extends L9ItemSubs implements TickingItem, ParameterizedItemModel.IParamaterized {

    private static final int MAX_USE_TICKS = 72000;

    public ItemLaserGun() {
        super(LangConst.ITEM_LASER_GUN, Tier.VALUES.length);
        setMaxStackSize(1);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityBroker()
                .with(AlganeCaps.LASER_GUN, new LaserItemGunDelegator(stack, Tier.getForStack(stack).modifierCount))
                .with(CapabilityEnergy.ENERGY, new LaserItemEnergyDelegator(stack));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return AlganeUtils.getLaserCore(AlganeUtils.getItemLaserGun(stack))
                .map(c -> c.getFiringParadigm().requiresTick ? MAX_USE_TICKS : 0).orElse(0);
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return OptUtils.capability(newStack, AlganeCaps.LASER_GUN)
                .flatMap(gun -> OptUtils.capability(oldStack, AlganeCaps.LASER_GUN)
                        .map(laserGun -> laserGun.areBuildsEqual(gun)))
                .orElse(false);
    }

    @Override
    public void updateItem(ItemStack stack, EntityPlayer player) {
        AlganeUtils.coolDown(AlganeUtils.getItemLaserGun(stack));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            CooldownTracker cooldowns = player.getCooldownTracker();
            if (!cooldowns.hasCooldown(this)) {
                LaserGun gun = AlganeUtils.getItemLaserGun(stack);
                if (!gun.isHeatLocked()) {
                    Optional<LaserGunCore> coreOpt = AlganeUtils.getLaserCore(gun);
                    if (coreOpt.isPresent()) {
                        if (AlganeUtils.getLaserEnergy(gun).isPresent()) {
                            LaserGunCore core = coreOpt.get();
                            int cooldown;
                            if (core.getFiringParadigm().requiresTick) {
                                cooldown = core.startFiring(
                                        stack, gun, world, getFiringPos(player), player.getLookVec(), player, hand);
                                if (cooldown < 0) {
                                    cooldown = Math.abs(cooldown + 1);
                                } else {
                                    player.setActiveHand(hand);
                                }
                            } else {
                                cooldown = core.fire(
                                        stack, gun, world, getFiringPos(player), player.getLookVec(), 0, player, hand);
                            }
                            if (cooldown > 0) {
                                cooldowns.setCooldown(this, cooldown);
                            }
                            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                        } else {
                            player.sendStatusMessage(new TextComponentTranslation(LangConst.STATUS_GUN_MISSING_ENERGY), true);
                        }
                    } else {
                        player.sendStatusMessage(new TextComponentTranslation(LangConst.STATUS_GUN_MISSING_CORE), true);
                    }
                } else {
                    player.sendStatusMessage(new TextComponentTranslation(LangConst.STATUS_GUN_OVERHEAT), true);
                }
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (player instanceof EntityPlayer && !player.world.isRemote) {
            LaserGun gun = AlganeUtils.getItemLaserGun(stack);
            if (gun.isHeatLocked()) {
                player.resetActiveHand();
            } else {
                CooldownTracker cooldowns = ((EntityPlayer)player).getCooldownTracker();
                if (!cooldowns.hasCooldown(this)) {
                    AlganeUtils.getLaserCore(gun).filter(c -> c.getFiringParadigm().requiresTick).ifPresent(core -> {
                        Vec3d firingPos = getFiringPos(player), firingDir = player.getLookVec();
                        int cooldown = core.fire(stack, gun, player.world, firingPos, firingDir,
                                MAX_USE_TICKS - count, player, player.getActiveHand());
                        if (cooldown < 0) {
                            player.resetActiveHand();
                            cooldown = core.finishFiring(stack, gun, player.world, firingPos, firingDir,
                                    MAX_USE_TICKS - count, player, player.getActiveHand(), true);
                        }
                        if (cooldown > 0) {
                            cooldowns.setCooldown(this, cooldown);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int timeLeft) {
        if (!world.isRemote && player instanceof EntityPlayer) {
            CooldownTracker cooldowns = ((EntityPlayer)player).getCooldownTracker();
            LaserGun gun = AlganeUtils.getItemLaserGun(stack);
            AlganeUtils.getLaserCore(gun).filter(c -> c.getFiringParadigm().requiresFinish).ifPresent(core -> {
                int cooldown = core.finishFiring(stack, gun, world, getFiringPos(player), player.getLookVec(),
                        MAX_USE_TICKS - timeLeft, player, player.getActiveHand(), !cooldowns.hasCooldown(this));
                if (cooldown > 0) {
                    cooldowns.setCooldown(this, cooldown);
                }
            });
        }
    }

    private static Vec3d getFiringPos(EntityLivingBase player) {
        return player.getPositionEyes(1F);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !canContinueUsing(oldStack, newStack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return AlganeUtils.getLaserEnergy(AlganeUtils.getItemLaserGun(stack)).isPresent();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return AlganeUtils.getLaserEnergy(AlganeUtils.getItemLaserGun(stack))
                .map(e -> e.getMaxEnergyStored() == 0 ? 1D : (1D - e.getEnergyStored() / (double)e.getMaxEnergyStored()))
                .orElse(1D);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0x42A5F5;
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("tier", Tier.getForStack(stack).name());
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return AlganeUtils.getLaserCore(AlganeUtils.getItemLaserGun(stack))
                .map(core -> I18n.format(
                        LangConst.getLaserTierName(Tier.getForStack(stack)), I18n.format(core.getTranslationKey())))
                .orElse(super.getItemStackDisplayName(stack));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        TooltipUtils.handleLaserGunTooltip(AlganeUtils.getItemLaserGun(stack), tooltip);
    }

    public enum Tier {

        BASIC(1),
        ADVANCED(2),
        ELITE(3),
        ULTIMATE(4);

        public static final Tier[] VALUES = values();

        public static Tier getForMeta(int meta) {
            return VALUES[meta];
        }

        public static Tier getForStack(ItemStack stack) {
            return getForMeta(stack.getMetadata());
        }

        public final int modifierCount;

        Tier(int modifierCount) {
            this.modifierCount = modifierCount;
        }

        public ItemStack createStack() {
            return new ItemStack(AlganeItems.LASER_GUN, 1, ordinal());
        }

    }

}
