package xyz.phanta.algane.lasergun.core;

import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.util.AlganeUtils;

public class LaserGunCoreSimple implements LaserGunCore {

    @Override
    public FiringParadigm getFiringParadigm() {
        return FiringParadigm.SEMI_AUTO;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir) {
        IEnergyStorage energy = AlganeUtils.getLaserEnergy(gun).get();
        if (energy.getEnergyStored() > 24) {
            energy.extractEnergy(24, false);
            EntitySnowball sb = new EntitySnowball(world, pos.x, pos.y, pos.z);
            sb.shoot(dir.x, dir.y, dir.z, 3F, 0F);
            world.spawnEntity(sb);
            return 8;
        }
        return 0;
    }

}
