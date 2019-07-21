package xyz.phanta.algane.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.core.LaserGunCore;

import java.util.Objects;
import java.util.Optional;

public class AlganeUtils {

    public static <T> Optional<T> getCapOpt(ICapabilityProvider provider, Capability<T> cap) {
        if (provider.hasCapability(cap, null)) {
            //noinspection ConstantConditions
            return Optional.of(provider.getCapability(cap, null));
        } else {
            return Optional.empty();
        }
    }

    public static LaserGun getItemLaserGun(ItemStack stack) {
        return Objects.requireNonNull(stack.getCapability(AlganeCaps.LASER_GUN, null));
    }

    public static Optional<LaserGunCore> getLaserCore(LaserGun gun) {
        return AlganeUtils.getCapOpt(gun.getCore(), AlganeCaps.LASER_GUN_CORE);
    }

    public static Optional<IEnergyStorage> getLaserEnergy(LaserGun gun) {
        return AlganeUtils.getCapOpt(gun.getEnergyCell(), CapabilityEnergy.ENERGY);
    }

    public static String formatFractionTooltip(String labelKey, String nom, String denom) {
        return TextFormatting.GRAY + I18n.format(labelKey, TextFormatting.AQUA + I18n.format(LangConst.TT_FRACTION, nom, denom));
    }

    public static Optional<NBTTagCompound> getTagOpt(ItemStack stack) {
        //noinspection ConstantConditions
        return stack.hasTagCompound() ? Optional.of(stack.getTagCompound()) : Optional.empty();
    }

}
