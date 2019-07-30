package xyz.phanta.algane.lasergun.core;

import io.github.phantamanta44.libnine.util.math.LinAlUtils;
import io.github.phantamanta44.libnine.util.math.MathUtils;
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
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;
import xyz.phanta.algane.lasergun.damage.DamageHitscan;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.LasingUtils;

import javax.annotation.Nullable;

public class LaserGunCoreRepeater implements LaserGunCore {

    private static final int BASE_COLOUR = 0xEF5350;

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.AUTO;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                    @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        float energyUse = AlganeUtils.consumeEnergy(gun, AlganeConfig.coreRepeater.baseEnergyUse, mods);
        if (energyUse > 0) {
            float driftSlope = (float)AlganeConfig.coreRepeater.driftMaxSlope;
            if (driftSlope > 0) {
                Vec3d inaccU = LinAlUtils.findOrthogonal(dir).normalize();
                Vec3d inaccV = inaccU.crossProduct(dir).normalize();
                float inaccTheta = world.rand.nextFloat() * MathUtils.PI_F * 2F;
                dir = dir.add(inaccU.scale(Math.cos(inaccTheta)).add(inaccV.scale(Math.sin(inaccTheta)))
                        .scale(world.rand.nextFloat()
                                * Math.min(ticks * driftSlope / AlganeConfig.coreRepeater.driftMaxTime, driftSlope)));
            }
            Vec3d endPos = LasingUtils.laseEntity(world, pos, dir, (float)AlganeConfig.coreRepeater.maxRange, owner,
                    hit -> hit.attackEntityFrom(DamageHitscan.laser(owner, stack),
                            AlganeUtils.computeDamage((float)AlganeConfig.coreRepeater.baseDamage * energyUse, mods)));
            world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_REPEATER_FIRE, SoundCategory.MASTER, 0.75F, 1F);
            Algane.PROXY.spawnParticleLaserBeam(
                    world, pos, endPos, BASE_COLOUR, 4, owner != null ? owner.getUniqueID() : null, hand);
            AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat((float)AlganeConfig.coreRepeater.baseHeat, mods));
            return AlganeConfig.coreRepeater.shotDelay;
        }
        return 0;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.REPEATER);
    }

    @Override
    public int getDisplayColour() {
        return BASE_COLOUR;
    }

}
