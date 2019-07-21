package xyz.phanta.algane.lasergun.core;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.lasergun.LaserGun;

public interface LaserGunCore {

    FiringParadigm getFiringParadigm();

    default int startFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir) {
        return 0;
    }

    int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir);

    default int finishFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, boolean offCooldown) {
        return 0;
    }

    default boolean isMachineOperable() {
        return !getFiringParadigm().requiresFinish;
    }

    class Impl implements LaserGunCore {

        @Override
        public FiringParadigm getFiringParadigm() {
            return FiringParadigm.SEMI_AUTO;
        }

        @Override
        public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir) {
            return 0;
        }

    }

    enum FiringParadigm {

        SEMI_AUTO(false, false),
        AUTO(true, false),
        CHARGE(true, true);

        public final boolean requiresTick, requiresFinish;

        FiringParadigm(boolean requiresTick, boolean requiresFinish) {
            this.requiresTick = requiresTick;
            this.requiresFinish = requiresFinish;
        }

    }

}
