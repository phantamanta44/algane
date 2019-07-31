package xyz.phanta.algane.lasergun.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.util.AlganeUtils;

public class LaserItemEnergyDelegator implements IEnergyStorage {

    private final ItemStack stack;

    public LaserItemEnergyDelegator(ItemStack stack) {
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
