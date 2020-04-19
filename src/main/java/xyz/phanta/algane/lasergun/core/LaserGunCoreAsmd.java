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
            Vec3d endPos = LasingUtils.laseEntity(
                    world, pos, dir, (float)AlganeConfig.coreSimple.maxRange, AlganeUtils.getLasingFilter(owner),
                    hit -> {
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
                            if (hit.attackEntityFrom(DamageHitscan.asmd(owner, stack),
                                    AlganeUtils.computeDamage((float)AlganeConfig.coreShock.baseDamage * energyUse, mods))) {
                                AlganeUtils.applyImpulse(
                                        hit, (float)AlganeConfig.coreShock.knockbackFactor * energyUse, dir.scale(-1D));
                            }
                        }
                    });
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
