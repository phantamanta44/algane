package xyz.phanta.algane.lasergun;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.init.AlganeCaps;

import javax.annotation.Nullable;
import java.util.Objects;

public enum LaserGunPart {

    CORE,
    ENERGY_CELL,
    MODIFIER;

    @Nullable
    public static LaserGunPart getPartType(ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.hasCapability(AlganeCaps.LASER_GUN_CORE, null)) {
                return CORE;
            } else if (stack.hasCapability(AlganeCaps.LASER_GUN_MOD, null)) {
                return MODIFIER;
            } else if (stack.hasCapability(CapabilityEnergy.ENERGY, null) && !stack.hasCapability(AlganeCaps.LASER_GUN, null)) {
                IEnergyStorage energy = Objects.requireNonNull(stack.getCapability(CapabilityEnergy.ENERGY, null));
                if (energy.canExtract() && energy.canReceive()) {
                    return ENERGY_CELL;
                }
            }
        }
        return null;
    }

}
