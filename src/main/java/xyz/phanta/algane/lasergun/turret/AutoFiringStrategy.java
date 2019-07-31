package xyz.phanta.algane.lasergun.turret;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;
import xyz.phanta.algane.tile.TileLaserTurret;
import xyz.phanta.algane.util.AlganeUtils;

public class AutoFiringStrategy implements TurretFiringStrategy {

    @Override
    public boolean tick(TileLaserTurret tile, LaserGun gun, boolean powered, boolean offCooldown, int ticks) {
        return AlganeUtils.getLaserCore(gun)
                .map(core -> powered && handleFireTick(tile, gun, core, offCooldown, ticks)).orElse(false);
    }

    static boolean handleFireTick(TileLaserTurret tile, LaserGun gun, LaserGunCore core, boolean offCooldown, int ticks) {
        if (offCooldown) {
            Vec3d dir = tile.getFiringDir();
            if (ticks == 0) {
                int cooldown = core.startFiring(
                        ItemStack.EMPTY, gun, tile.getWorld(), tile.getFiringPos(dir), dir, null, null);
                if (cooldown < 0) {
                    tile.setCooldown(Math.abs(cooldown + 1));
                    return false;
                } else {
                    tile.setCooldown(cooldown);
                }
            } else {
                Vec3d pos = tile.getFiringPos(dir);
                int cooldown = core.fire(ItemStack.EMPTY, gun, tile.getWorld(), pos, dir, ticks, null, null);
                if (cooldown < 0) {
                    tile.setCooldown(core.finishFiring(
                            ItemStack.EMPTY, gun, tile.getWorld(), pos, dir, ticks, null, null, true));
                    return false;
                } else {
                    tile.setCooldown(cooldown);
                }
            }
            return true;
        }
        return ticks > 0;
    }

}
