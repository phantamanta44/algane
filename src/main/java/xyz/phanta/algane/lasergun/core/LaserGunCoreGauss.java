package xyz.phanta.algane.lasergun.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.AlganeConfig;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeSounds;
import xyz.phanta.algane.item.ItemLaserCore;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.lasergun.core.base.LaserGunCoreCharge;
import xyz.phanta.algane.lasergun.damage.DamageHitscan;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.LasingUtils;

import javax.annotation.Nullable;

public class LaserGunCoreGauss extends LaserGunCoreCharge {

    private static final int BASE_COLOUR = 0xE1C117;

    @Override
    protected int getEnergyCost(int ticks) {
        return AlganeConfig.coreGauss.baseEnergyUse
                + (int)Math.ceil(AlganeConfig.coreGauss.baseEnergyUse * AlganeConfig.coreGauss.costFactor * ticks);
    }

    @Override
    protected void onStartCharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                                 @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        if (owner != null) {
            Algane.PROXY.playGaussChargeFx(world, owner);
        }
    }

    @Override
    protected int discharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                            @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        Vec3d endPos = LasingUtils.laseEntity(
                world, pos, dir, (float)AlganeConfig.coreGauss.maxRange,AlganeUtils.getLasingFilter(owner),
                hit -> hit.attackEntityFrom(DamageHitscan.gauss(owner, stack),
                        AlganeUtils.computeDamage((float)AlganeConfig.coreGauss.baseDamage * ticks * ticks * ticks, mods)));
        world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_GAUSS_FIRE, SoundCategory.MASTER, 1F, 1F);
        Algane.PROXY.spawnParticleLaserBeam(
                world, pos, endPos, BASE_COLOUR, 16, owner != null ? owner.getUniqueID() : null, hand);
        AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat((float)AlganeConfig.coreGauss.baseHeat, mods));
        if (owner != null) {
            Algane.PROXY.stopChargeFx(world, owner);
            AlganeUtils.applyImpulse(owner, (float)AlganeConfig.coreGauss.recoilFactor, dir);
        }
        return AlganeConfig.coreGauss.shotDelay;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.GAUSS);
    }

    @Override
    public int getDisplayColour() {
        return BASE_COLOUR;
    }

}
