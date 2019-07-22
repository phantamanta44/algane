package xyz.phanta.algane.lasergun.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.lasergun.LaserGun;

import javax.annotation.Nullable;

public abstract class ChargingLaserGunCore implements LaserGunCore {

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.CHARGE;
    }

    @Override
    public int startFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, @Nullable EntityLivingBase owner) {
        gun.setFiringDuration(0);
        return 0;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, @Nullable EntityLivingBase owner) {

        gun.incrementFiringDuration();
        return 0;
    }

    @Override
    public abstract int finishFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                                     @Nullable EntityLivingBase owner, boolean offCooldown);

    protected abstract int getChargeCost();

}
