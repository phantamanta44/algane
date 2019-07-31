package xyz.phanta.algane.lasergun.core;

import io.github.phantamanta44.libnine.util.math.LinAlUtils;
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
import xyz.phanta.algane.AlganeConfig;
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

    private static final int BASE_COLOUR = 0x2196F3;
    private static final float SHOCK_COMBO_RADIUS = 7F;

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.SEMI_AUTO;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                    @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        float energyUse = AlganeUtils.consumeEnergy(gun, AlganeConfig.coreShock.baseEnergyUse, mods);
        if (energyUse > 0) {
            Vec3d endPos = pos.add(dir.scale(AlganeConfig.coreShock.maxRange));
            RayTraceResult trace = LasingUtils.traceLaser(world, pos, endPos, LasingUtils::isImpassible);
            if (trace != null) {
                endPos = trace.hitVec;
            }
            endPos = LasingUtils.getEntitiesOnLine(Entity.class, world, pos, endPos)
                    .filter(e -> e != owner && (e instanceof EntityLivingBase || e instanceof EntityShockOrb))
                    .map(e -> IPair.of(e, e.getDistanceSq(pos.x, pos.y, pos.z)))
                    .min(Comparator.comparing(IPair::getB))
                    .map(IPair::getA)
                    .map(hit -> {
                        Vec3d hitPos = hit.getPositionVector();
                        if (hit instanceof EntityShockOrb) {
                            EntityShockOrb hitOrb = (EntityShockOrb)hit;
                            float damage = (float)AlganeConfig.coreOrb.shockComboMultiplier * (hitOrb.getDamage()
                                    + AlganeUtils.computeDamage((float)AlganeConfig.coreShock.baseDamage * energyUse, mods));
                            hitOrb.detonate(SHOCK_COMBO_RADIUS, damage, DamageBlast.shockCombo(hitOrb, owner, stack));
                            world.playSound(null, hitOrb.posX, hitOrb.posY, hitOrb.posZ,
                                    AlganeSounds.GUN_ORB_COMBO, SoundCategory.MASTER, 1F, 1F);
                            Algane.PROXY.spawnParticleShockBlast(world, hitPos, SHOCK_COMBO_RADIUS, 100F, BASE_COLOUR);
                        } else {
                            EntityLivingBase hitLiving = (EntityLivingBase)hit;
                            hitLiving.attackEntityFrom(DamageHitscan.asmd(owner, stack),
                                    AlganeUtils.computeDamage((float)AlganeConfig.coreShock.baseDamage * energyUse, mods));
                            // should probably be fine?
                            //noinspection ConstantConditions
                            hitLiving.knockBack(owner, (float)AlganeConfig.coreShock.knockbackFactor * energyUse, -dir.x, -dir.z);
                        }
                        return LinAlUtils.castOntoPlane(pos, dir, hitPos, dir);
                    }).orElse(endPos);
            world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_SHOCK_FIRE, SoundCategory.MASTER, 1F, 1F);
            Algane.PROXY.spawnParticleAsmd(world, pos, endPos, owner != null ? owner.getUniqueID() : null, hand);
            AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat((float)AlganeConfig.coreShock.baseHeat, mods));
            return AlganeConfig.coreShock.shotDelay;
        }
        return 0;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.SHOCK);
    }

    @Override
    public int getDisplayColour() {
        return BASE_COLOUR;
    }

}
