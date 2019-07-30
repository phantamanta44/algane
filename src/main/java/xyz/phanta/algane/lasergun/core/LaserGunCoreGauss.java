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
import xyz.phanta.algane.lasergun.core.base.LaserGunCoreCharge;
import xyz.phanta.algane.lasergun.damage.DamageHitscan;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.LasingUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class LaserGunCoreGauss extends LaserGunCoreCharge {

    private static final int BASE_ENERGY = 100;
    private static final float BASE_DAMAGE = 0.1F;
    private static final float BASE_RANGE = 64F;
    private static final float BASE_HEAT = 75F;
    private static final int BASE_COLOUR = 0xE1C117;
    private static final float BASE_RECOIL = 0.32F;

    private static final float COST_FACTOR = 0.08F;
    private static final float EQ_TIME = 40F;
    private static final float DAMAGE_FACTOR = (EQ_TIME + 1F) * COST_FACTOR / (2 * EQ_TIME * EQ_TIME);

    @Override
    protected int getEnergyCost(int ticks) {
        return (int)Math.ceil(BASE_ENERGY * COST_FACTOR * (ticks + 1));
    }

    @Override
    protected void onStartCharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                                 EntityLivingBase owner, EnumHand hand) {
        Algane.PROXY.playGaussChargeFx(world, owner);
    }

    @Override
    public int finishFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                            @Nullable EntityLivingBase owner, @Nullable EnumHand hand, boolean offCooldown) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        Vec3d endPos = LasingUtils.laseEntity(world, pos, dir, BASE_RANGE, owner, hit ->
                hit.attackEntityFrom(DamageHitscan.gauss(owner, stack),
                        AlganeUtils.computeDamage(BASE_DAMAGE * ticks * ticks * ticks * DAMAGE_FACTOR, mods)));
        world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_GAUSS_FIRE, SoundCategory.MASTER, 1F, 1F);
        Algane.PROXY.spawnParticleLaserBeam(
                world, pos, endPos, BASE_COLOUR, 16, owner != null ? owner.getUniqueID() : null, hand);
        AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat(BASE_HEAT, mods));
        Algane.PROXY.stopChargeFx(world, Objects.requireNonNull(owner));
        AlganeUtils.applyRecoilKnockback(owner, BASE_RECOIL, dir);
        return 20;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.GAUSS);
    }

}
