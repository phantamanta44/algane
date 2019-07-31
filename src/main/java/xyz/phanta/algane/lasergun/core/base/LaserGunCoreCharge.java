package xyz.phanta.algane.lasergun.core.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public abstract class LaserGunCoreCharge implements LaserGunCore {

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.CHARGE;
    }

    @Override
    public int startFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                           @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        if (chargeTick(gun, 0)) {
            onStartCharge(stack, gun, world, pos, dir, owner, hand);
            return 0;
        }
        return -1;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                    @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        return chargeTick(gun, ticks) ? 0 : -1;
    }

    @Override
    public int finishFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                            @Nullable EntityLivingBase owner, @Nullable EnumHand hand, boolean offCooldown) {
        return ticks > 0 ? discharge(stack, gun, world, pos, dir, ticks, owner, hand) : 0;
    }


    private boolean chargeTick(LaserGun gun, int ticks) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        float energyUse = AlganeUtils.consumeEnergy(gun, getEnergyCost(ticks), mods);
        return energyUse == 1F;
    }

    protected abstract int getEnergyCost(int ticks);

    protected abstract void onStartCharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                                          @Nullable EntityLivingBase owner, @Nullable EnumHand hand);

    protected abstract int discharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                                     @Nullable EntityLivingBase owner, @Nullable EnumHand hand);

}
