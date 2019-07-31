package xyz.phanta.algane.lasergun.core.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.lasergun.LaserGun;

import javax.annotation.Nullable;

public interface LaserGunCore {

    FiringParadigm getFiringParadigm();

    default int startFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                            @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        return 0;
    }

    int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
             @Nullable EntityLivingBase owner, @Nullable EnumHand hand);

    default int finishFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                             @Nullable EntityLivingBase owner, @Nullable EnumHand hand, boolean offCooldown) {
        return 0;
    }

    String getTranslationKey();

    int getDisplayColour();

    class Impl implements LaserGunCore {

        @Override
        public FiringParadigm getFiringParadigm() {
            return FiringParadigm.SEMI_AUTO;
        }

        @Override
        public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                        @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
            return 0;
        }

        @Override
        public String getTranslationKey() {
            return "<default impl>";
        }

        @Override
        public int getDisplayColour() {
            return 0;
        }

    }

    enum FiringParadigm {

        SEMI_AUTO(false, false),
        AUTO(true, false),
        CHARGE(true, true);

        public static final FiringParadigm[] VALUES = values();

        public final boolean requiresTick, requiresFinish;

        FiringParadigm(boolean requiresTick, boolean requiresFinish) {
            this.requiresTick = requiresTick;
            this.requiresFinish = requiresFinish;
        }

    }

}
