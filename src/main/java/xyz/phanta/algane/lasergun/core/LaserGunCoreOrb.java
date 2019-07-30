package xyz.phanta.algane.lasergun.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.entity.EntityShockOrb;
import xyz.phanta.algane.init.AlganeSounds;
import xyz.phanta.algane.item.ItemLaserCore;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.lasergun.core.base.LaserGunCoreCharge;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class LaserGunCoreOrb extends LaserGunCoreCharge {

    private static final int BASE_ENERGY = 100;
    private static final float BASE_DAMAGE = 0.25F;
    private static final float BASE_HEAT = 64F;
    private static final float BASE_RECOIL = 0.25F;

    @Override
    protected int getEnergyCost(int ticks) {
        return BASE_ENERGY;
    }

    @Override
    protected void onStartCharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                                 EntityLivingBase owner, EnumHand hand) {
        Algane.PROXY.playOrbChargeFx(world, owner);
    }

    @Override
    public int finishFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                            @Nullable EntityLivingBase owner, @Nullable EnumHand hand, boolean offCooldown) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        world.spawnEntity(new EntityShockOrb(world, pos, dir, owner, stack)
                .init(AlganeUtils.computeDamage(BASE_DAMAGE * ticks, mods)));
        world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_ORB_FIRE, SoundCategory.MASTER, 1F, 1F);
        AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat(BASE_HEAT, mods));
        Algane.PROXY.stopChargeFx(world, Objects.requireNonNull(owner));
        AlganeUtils.applyRecoilKnockback(owner, BASE_RECOIL, dir);
        return 8;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.ORB);
    }

}
