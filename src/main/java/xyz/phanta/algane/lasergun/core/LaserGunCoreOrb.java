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
import xyz.phanta.algane.lasergun.core.base.LaserGunCoreCharge;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public class LaserGunCoreOrb extends LaserGunCoreCharge {

    @Override
    protected int getEnergyCost(int ticks) {
        return AlganeConfig.coreOrb.baseEnergyUse;
    }

    @Override
    protected void onStartCharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir,
                                 @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        if (owner != null) {
            Algane.PROXY.playOrbChargeFx(world, owner);
        }
    }

    @Override
    protected int discharge(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                            @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        world.spawnEntity(new EntityShockOrb(world, pos, dir, owner, stack)
                .init(AlganeUtils.computeDamage((float)AlganeConfig.coreOrb.baseDamage * ticks, mods)));
        world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_ORB_FIRE, SoundCategory.MASTER, 1F, 1F);
        AlganeUtils.incrementHeat(gun, AlganeUtils.computeHeat((float)AlganeConfig.coreOrb.baseHeat, mods));
        if (owner != null) {
            Algane.PROXY.stopChargeFx(world, owner);
            AlganeUtils.applyRecoilKnockback(owner, (float)AlganeConfig.coreOrb.recoilFactor, dir);
        }
        return AlganeConfig.coreOrb.shotDelay;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.ORB);
    }

    @Override
    public int getDisplayColour() {
        return 0xC300D8;
    }

}
