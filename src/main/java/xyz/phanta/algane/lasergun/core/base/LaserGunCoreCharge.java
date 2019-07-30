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
import java.util.Objects;

public abstract class LaserGunCoreCharge implements LaserGunCore {

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.CHARGE;
    }

    @Override
    public int startFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                           @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        if (chargeTick(Objects.requireNonNull(owner), gun, 0)) {
            onStartCharge(stack, gun, world, pos, dir, owner, Objects.requireNonNull(hand));
        }
        return 0;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                    @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        if (!chargeTick(Objects.requireNonNull(owner), gun, ticks)) {
            return finishFiring(stack, gun, world, pos, dir, ticks, owner, hand, true);
        }
        return 0;
    }

    protected boolean chargeTick(EntityLivingBase owner, LaserGun gun, int ticks) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        float energyUse = AlganeUtils.consumeEnergy(gun, getEnergyCost(ticks), mods);
        if (energyUse < 1F) {
            owner.resetActiveHand();
            return false;
        }
        return true;
    }

    protected abstract int getEnergyCost(int ticks);

    protected abstract void onStartCharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                                          EntityLivingBase owner, EnumHand hand);

}
