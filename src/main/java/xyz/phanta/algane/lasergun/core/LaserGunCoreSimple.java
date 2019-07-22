package xyz.phanta.algane.lasergun.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.item.ItemLaserCore;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public class LaserGunCoreSimple implements LaserGunCore {

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.SEMI_AUTO;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, @Nullable Entity owner) {
        IEnergyStorage energy = AlganeUtils.getLaserEnergy(gun).get();
        if (energy.getEnergyStored() > 24) {
            energy.extractEnergy(24, false);
            EntitySnowball sb = new EntitySnowball(world, pos.x + dir.x, pos.y + dir.y, pos.z + dir.z);
            sb.shoot(dir.x, dir.y, dir.z, 3F, 0F);
            world.spawnEntity(sb);
            AlganeUtils.incrementHeat(gun, 25F);
            return 8;
        }
        return 0;
    }

    @Override
    public String getTranslationKey() {
        return LangConst.getLaserCoreName(ItemLaserCore.Type.SIMPLE);
    }

}
