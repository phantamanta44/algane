package xyz.phanta.algane.item;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import io.github.phantamanta44.libnine.util.format.FormatUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.init.AlganeItems;
import xyz.phanta.algane.item.base.TickingItem;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.OptUtils;
import xyz.phanta.algane.util.TooltipUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
                .with(AlganeCaps.LASER_GUN, new LaserGunDelegator(stack))
                .with(CapabilityEnergy.ENERGY, new LaserGunEnergyDelegator(stack));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return AlganeUtils.getLaserCore(AlganeUtils.getItemLaserGun(stack))
                .map(c -> c.getFiringParadigm().requiresTick ? MAX_USE_TICKS : 0).orElse(0);
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return OptUtils.getCapOpt(newStack, AlganeCaps.LASER_GUN)
                .flatMap(gun -> OptUtils.getCapOpt(oldStack, AlganeCaps.LASER_GUN)
                        .map(laserGun -> laserGun.areBuildsEqual(gun)))
                .orElse(false);
    }

    @Override
    public void updateItem(ItemStack stack, EntityPlayer player) {
        LaserGun gun = AlganeUtils.getItemLaserGun(stack);
        float heat = gun.getOverheat();
        if (heat > 0F) {
            heat -= 1F;
            if (heat <= 0F) {
                gun.setOverheat(0F);
                gun.setHeatLocked(false);
            } else {
                gun.setOverheat(heat);
            }
        }
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
                                player.setActiveHand(hand);
                                cooldown = core.startFiring(
                                        stack, gun, world, getFiringPos(player), player.getLookVec(), player, hand);
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
                        int cooldown = core.fire(stack, gun, player.world, getFiringPos(player), player.getLookVec(),
                                MAX_USE_TICKS - count, player, player.getActiveHand());
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
        m.mutate("tier", getLaserTier(stack).name());
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return AlganeUtils.getLaserCore(AlganeUtils.getItemLaserGun(stack))
                .map(core -> I18n.format(
                        LangConst.getLaserTierName(getLaserTier(stack)), I18n.format(core.getTranslationKey())))
                .orElse(super.getItemStackDisplayName(stack));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        LaserGun gun = AlganeUtils.getItemLaserGun(stack);
        AlganeUtils.getLaserEnergy(gun).ifPresent(energy -> tooltip.add(TooltipUtils.formatFractionTooltip(LangConst.TT_ENERGY,
                FormatUtils.formatSI(energy.getEnergyStored(), "FE"), FormatUtils.formatSI(energy.getMaxEnergyStored(), "FE"))));
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_STAT_DAMAGE, AlganeUtils.getDamageMultiplier(mods)));
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_STAT_ENERGY, AlganeUtils.getEnergyMultiplier(mods)));
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_STAT_HEAT, AlganeUtils.getHeatMultiplier(mods)));
    }

    private static Tier getLaserTier(ItemStack stack) {
        return Tier.getForMeta(stack.getMetadata());
    }

    private static class LaserGunDelegator implements LaserGun {

        private final ItemStack stack;
        private final int modCount;

        LaserGunDelegator(ItemStack stack) {
            this.stack = stack;
            this.modCount = getLaserTier(stack).modifierCount;
        }

        @Override
        public ItemStack getCore() {
            return getCoreTag().map(ItemStack::new).orElse(ItemStack.EMPTY);
        }

        private Optional<NBTTagCompound> getCoreTag() {
            return OptUtils.getTagOpt(stack).map(tag -> tag.getCompoundTag("Core"));
        }

        @Override
        public void setCore(ItemStack stack) {
            getOrCreateTag().setTag("Core", stack.serializeNBT());
        }

        @Override
        public ItemStack getEnergyCell() {
            return OptUtils.getTagOpt(stack)
                    .map(tag -> new ItemStack(tag.getCompoundTag("EnergyCell")))
                    .orElse(ItemStack.EMPTY);
        }

        @Override
        public void setEnergyCell(ItemStack stack) {
            getOrCreateTag().setTag("EnergyCell", stack.serializeNBT());
        }

        @Override
        public ItemStack getModifier(int slot) {
            return getModifiersTag().map(tag -> new ItemStack(tag.getCompoundTagAt(slot))).orElse(ItemStack.EMPTY);
        }

        private Optional<NBTTagList> getModifiersTag() {
            return OptUtils.getTagOpt(stack).map(tag -> tag.getTagList("Modifiers", Constants.NBT.TAG_COMPOUND));
        }

        @Override
        public void setModifier(int slot, ItemStack stack) {
            NBTTagCompound tag = getOrCreateTag();
            NBTTagList modDto;
            if (tag.hasKey("Modifiers")) {
                modDto = tag.getTagList("Modifiers", Constants.NBT.TAG_COMPOUND);
            } else {
                modDto = new NBTTagList();
                NBTTagCompound emptyTag = ItemStack.EMPTY.serializeNBT();
                for (int i = 0; i < modCount; i++) {
                    modDto.appendTag(emptyTag);
                }
                tag.setTag("Modifiers", modDto);
            }
            modDto.set(slot, stack.serializeNBT());
        }

        @Override
        public int getModifierCount() {
            return modCount;
        }

        @Override
        public float getOverheat() {
            return OptUtils.getTagOpt(stack)
                    .map(tag -> tag.getFloat("Heat"))
                    .orElse(0F);
        }

        @Override
        public void setOverheat(float ticks) {
            getOrCreateTag().setFloat("Heat", ticks);
        }

        @Override
        public boolean isHeatLocked() {
            return OptUtils.getTagOpt(stack)
                    .map(tag -> tag.getBoolean("HeatLock"))
                    .orElse(false);
        }

        @Override
        public void setHeatLocked(boolean heatLocked) {
            getOrCreateTag().setBoolean("HeatLock", heatLocked);
        }

        @Override
        public boolean areBuildsEqual(LaserGun other) {
            if (!(other instanceof LaserGunDelegator)) {
                return false;
            }
            LaserGunDelegator otherGun = (LaserGunDelegator)other;
            return getCoreTag().equals(otherGun.getCoreTag()) && getModifiersTag().equals(otherGun.getModifiersTag());
        }

        private NBTTagCompound getOrCreateTag() {
            if (!stack.hasTagCompound()) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("GunId", UUID.randomUUID().toString());
                stack.setTagCompound(tag);
                return tag;
            }
            return Objects.requireNonNull(stack.getTagCompound());
        }

    }

    private static class LaserGunEnergyDelegator implements IEnergyStorage {

        private final ItemStack stack;

        LaserGunEnergyDelegator(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return AlganeUtils.getLaserEnergy(AlganeUtils.getItemLaserGun(stack))
                    .map(e -> e.receiveEnergy(maxReceive, simulate)).orElse(0);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return AlganeUtils.getLaserEnergy(AlganeUtils.getItemLaserGun(stack))
                    .map(e -> e.receiveEnergy(maxExtract, simulate)).orElse(0);
        }

        @Override
        public int getEnergyStored() {
            return AlganeUtils.getLaserEnergy(AlganeUtils.getItemLaserGun(stack))
                    .map(IEnergyStorage::getEnergyStored).orElse(0);
        }

        @Override
        public int getMaxEnergyStored() {
            return AlganeUtils.getLaserEnergy(AlganeUtils.getItemLaserGun(stack))
                    .map(IEnergyStorage::getMaxEnergyStored).orElse(0);
        }

        @Override
        public boolean canExtract() {
            return AlganeUtils.getLaserEnergy(AlganeUtils.getItemLaserGun(stack))
                    .map(IEnergyStorage::canExtract).orElse(false);
        }

        @Override
        public boolean canReceive() {
            return AlganeUtils.getLaserEnergy(AlganeUtils.getItemLaserGun(stack))
                    .map(IEnergyStorage::canReceive).orElse(false);
        }

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

        public final int modifierCount;

        Tier(int modifierCount) {
            this.modifierCount = modifierCount;
        }

        public ItemStack createStack() {
            return new ItemStack(AlganeItems.LASER_GUN, 1, ordinal());
        }

    }

}
