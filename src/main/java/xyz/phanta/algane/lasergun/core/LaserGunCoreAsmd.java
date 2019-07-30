package xyz.phanta.algane.lasergun.core;

import io.github.phantamanta44.libnine.util.tuple.IPair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.entity.EntityShockOrb;
import xyz.phanta.algane.init.AlganeSounds;
import xyz.phanta.algane.item.ItemLaserCore;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;
import xyz.phanta.algane.lasergun.damage.DamageBlast;
import xyz.phanta.algane.lasergun.damage.DamageHitscan;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.LasingUtils;

import javax.annotation.Nullable;
import java.util.Comparator;

public class LaserGunCoreAsmd implements LaserGunCore {

    private static final int BASE_ENERGY = 1500;
    private static final float BASE_DAMAGE = 5.5F;
    private static final float BASE_KNOCKBACK = 1.5F;
    private static final float BASE_RANGE = 64F;
    private static final float BASE_HEAT = 50F;

    private static final float SHOCK_COMBO_RADIUS = 7F;
    private static final float SHOCK_COMBO_MULTIPLIER = 4F;

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
            Vec3d endPos = pos.add(dir.scale(BASE_RANGE));
            RayTraceResult trace = LasingUtils.traceLaser(world, pos, endPos, LasingUtils::isImpassible);
            if (trace != null) {
                endPos = trace.hitVec;
            }
            LasingUtils.getEntitiesOnLine(Entity.class, world, pos, endPos)
                    .filter(e -> e != owner && (e instanceof EntityLivingBase || e instanceof EntityShockOrb))
                    .map(e -> IPair.of(e, e.getDistanceSq(pos.x, pos.y, pos.z)))
                    .min(Comparator.comparing(IPair::getB))
                    .map(IPair::getA)
                    .ifPresent(hit -> {
                        if (hit instanceof EntityShockOrb) {
                            EntityShockOrb hitOrb = (EntityShockOrb)hit;
                            float damage = SHOCK_COMBO_MULTIPLIER
                                    * (hitOrb.getDamage() + AlganeUtils.computeDamage(BASE_DAMAGE * energyUse, mods));
                            hitOrb.detonate(SHOCK_COMBO_RADIUS, damage, DamageBlast.shockCombo(hitOrb, owner));
                            world.playSound(null, hitOrb.posX, hitOrb.posY, hitOrb.posZ,
                                    AlganeSounds.GUN_ORB_COMBO, SoundCategory.MASTER, 1F, 1F);
                            Algane.PROXY.spawnParticleShockBlast(
                                    world, hitOrb.getPositionVector(), SHOCK_COMBO_RADIUS, 100F, 0x2196F3);
                        } else {
                            EntityLivingBase hitLiving = (EntityLivingBase)hit;
                            hitLiving.attackEntityFrom(
                                    DamageHitscan.asmd(owner), AlganeUtils.computeDamage(BASE_DAMAGE * energyUse, mods));
                            // should probably be fine?
                            //noinspection ConstantConditions
                            hitLiving.knockBack(owner, BASE_KNOCKBACK * energyUse, -dir.x, -dir.z);
                        }
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
