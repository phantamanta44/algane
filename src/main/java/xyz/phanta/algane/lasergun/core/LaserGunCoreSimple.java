package xyz.phanta.algane.lasergun.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.entity.EntityLaserBolt;
import xyz.phanta.algane.init.AlganeSounds;
import xyz.phanta.algane.item.ItemLaserCore;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public class LaserGunCoreSimple implements LaserGunCore {

    private static final int BASE_ENERGY = 800;
    private static final float BASE_DAMAGE = 4F;
    private static final float BASE_HEAT = 32F;
    private static final float BASE_RANGE = 64F;
    private static final int BASE_COLOUR = 0x2196F3;

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.SEMI_AUTO;
    }

    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks, @Nullable EntityLivingBase owner) {
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        float energyUse = AlganeUtils.consumeEnergy(gun, BASE_ENERGY, mods);
        if (energyUse > 0) {
            EntityLaserBolt bolt = new EntityLaserBolt(world, pos, dir.scale(1.5D),
                    AlganeUtils.computeDamage(BASE_DAMAGE * energyUse, mods), BASE_RANGE, owner);
            bolt.init(BASE_COLOUR, 0.1F);
            world.spawnEntity(bolt);
            world.playSound(null, pos.x, pos.y, pos.z, AlganeSounds.GUN_SIMPLE_FIRE, SoundCategory.MASTER, 1F, 1F);
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
