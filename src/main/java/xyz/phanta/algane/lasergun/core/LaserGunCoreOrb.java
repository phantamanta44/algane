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
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class LaserGunCoreOrb implements LaserGunCore {

    private static final int BASE_ENERGY = 100;
    private static final float BASE_DAMAGE = 0.25F;
    private static final float BASE_HEAT = 64F;

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.CHARGE;
    }

    @Override
    public int startFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                           @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        if (chargeTick(Objects.requireNonNull(owner), gun)) {
            Algane.PROXY.playOrbChargeFx(world, owner);
        }
        return 0;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                    @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        if (!chargeTick(Objects.requireNonNull(owner), gun)) {
            return finishFiring(stack, gun, world, pos, dir, ticks, owner, hand, true);
        }
        return 0;
    }

    private boolean chargeTick(EntityLivingBase owner, LaserGun gun) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        float energyUse = AlganeUtils.consumeEnergy(gun, BASE_ENERGY, mods);
        if (energyUse < 1F) {
            owner.resetActiveHand();
            return false;
        }
        return true;
    }

    @Override
    public int finishFiring(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                            @Nullable EntityLivingBase owner, @Nullable EnumHand hand, boolean offCooldown) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        world.spawnEntity(new EntityShockOrb(world, pos, dir, owner).init(AlganeUtils.computeDamage(BASE_DAMAGE * ticks, mods)));
        world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_ORB_FIRE, SoundCategory.MASTER, 1F, 1F);
        AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat(BASE_HEAT, mods));
        Algane.PROXY.stopChargeFx(world, Objects.requireNonNull(owner));
        return 8;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.ORB);
    }

}
