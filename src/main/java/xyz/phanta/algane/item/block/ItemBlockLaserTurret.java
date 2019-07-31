package xyz.phanta.algane.item.block;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemBlockStated;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import xyz.phanta.algane.block.BlockLaserTurret;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.item.base.TickingItem;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.item.LaserItemEnergyDelegator;
import xyz.phanta.algane.lasergun.item.LaserItemGunDelegator;
import xyz.phanta.algane.tile.TileLaserTurret;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.TooltipUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ItemBlockLaserTurret extends L9ItemBlockStated implements TickingItem, ParameterizedItemModel.IParamaterized {

    public ItemBlockLaserTurret(BlockLaserTurret block) {
        super(block);
        setMaxStackSize(1);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityBroker()
                .with(AlganeCaps.LASER_GUN, new LaserItemGunDelegator(stack,
                        BlockLaserTurret.Tier.getForStack(stack).modifierCount))
                .with(CapabilityEnergy.ENERGY, new LaserItemEnergyDelegator(stack));
    }

    @Override
    public void updateItem(ItemStack stack, EntityPlayer player) {
        AlganeUtils.coolDown(AlganeUtils.getItemLaserGun(stack));
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
                                EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
            TileLaserTurret tile = (TileLaserTurret)Objects.requireNonNull(world.getTileEntity(pos));
            tile.deserializeItemNbt(stack.getTagCompound());
            tile.setRotation(EnumFacing.getDirectionFromEntityLiving(pos, player));
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        TooltipUtils.handleLaserGunTooltip(AlganeUtils.getItemLaserGun(stack), tooltip);
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
        m.mutate("tier", getBlock().getStates().get(stack.getMetadata()).get(BlockLaserTurret.TIER).name());
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getDisplayName(AlganeUtils.getItemLaserGun(stack))
                .map(name -> I18n.format(LangConst.getLaserTierName(BlockLaserTurret.Tier.getForStack(stack)), name))
                .orElse(super.getItemStackDisplayName(stack));
    }

    public static Optional<String> getDisplayName(LaserGun gun) {
        return AlganeUtils.getLaserCore(gun)
                .map(core -> I18n.format(LangConst.LASER_POSTFIX_TURRET, I18n.format(core.getTranslationKey())));
    }

}
