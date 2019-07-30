package xyz.phanta.algane.lasergun.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.Algane;
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

public class LaserGunCoreSimple implements LaserGunCore {

    private static final int BASE_ENERGY = 800;
    private static final float BASE_DAMAGE = 4F;
    private static final float BASE_HEAT = 32F;
    private static final float BASE_RANGE = 48F;
    private static final int BASE_COLOUR = 0x4CAF50;

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.SEMI_AUTO;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                    @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        float energyUse = AlganeUtils.consumeEnergy(gun, BASE_ENERGY, mods);
        if (energyUse > 0) {
            Vec3d endPos = LasingUtils.laseEntity(world, pos, dir, BASE_RANGE, owner, hit -> hit.attackEntityFrom(
                    DamageHitscan.laser(owner), AlganeUtils.computeDamage(BASE_DAMAGE * energyUse, mods)));
            world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_SIMPLE_FIRE, SoundCategory.MASTER, 1F, 1F);
            Algane.PROXY.spawnParticleLaserBeam(
                    world, pos, endPos, BASE_COLOUR, 8, owner != null ? owner.getUniqueID() : null, hand);
            AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat(BASE_HEAT, mods));
            return 12;
        }
        return 0;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.SIMPLE);
    }

}
