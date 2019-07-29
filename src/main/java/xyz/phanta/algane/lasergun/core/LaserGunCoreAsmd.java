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
import xyz.phanta.algane.lasergun.damage.DamageHitscan;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.LasingUtils;

import javax.annotation.Nullable;

public class LaserGunCoreAsmd implements LaserGunCore {

    private static final int BASE_ENERGY = 1400;
    private static final float BASE_DAMAGE = 5.5F;
    private static final float BASE_KNOCKBACK = 1.5F;
    private static final float BASE_RANGE = 64F;
    private static final float BASE_HEAT = 50F;

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.SEMI_AUTO;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                    @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        // TODO shock combo
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        float energyUse = AlganeUtils.consumeEnergy(gun, BASE_ENERGY, mods);
        if (energyUse > 0) {
            // TODO don't lase through transparent blocks
            Vec3d endPos = LasingUtils.laseEntity(world, pos, dir, BASE_RANGE, owner, hit -> {
                hit.attackEntityFrom(DamageHitscan.asmd(owner), AlganeUtils.computeDamage(BASE_DAMAGE * energyUse, mods));
                // should probably be fine?
                //noinspection ConstantConditions
                hit.knockBack(owner, BASE_KNOCKBACK * energyUse, -dir.x, -dir.z);
            });
            world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_SHOCK_FIRE, SoundCategory.MASTER, 1F, 1F);
            Algane.PROXY.spawnParticleAsmd(world, pos, endPos, owner != null ? owner.getUniqueID() : null, hand);
            AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat(BASE_HEAT, mods));
            return 18;
        }
        return 0;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.SHOCK);
    }

}
