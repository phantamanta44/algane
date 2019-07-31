package xyz.phanta.algane.lasergun.turret;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.tile.TileLaserTurret;
import xyz.phanta.algane.util.AlganeUtils;

public class ChargeFiringStrategy implements TurretFiringStrategy {

    @Override
    public boolean tick(TileLaserTurret tile, LaserGun gun, boolean powered, boolean offCooldown, int ticks) {
        return AlganeUtils.getLaserCore(gun).map(core -> {
            if (powered) {
                return AutoFiringStrategy.handleFireTick(tile, gun, core, offCooldown, ticks);
            } else if (ticks > 0) {
                Vec3d dir = tile.getFiringDir();
                tile.setCooldown(core.finishFiring(
                        ItemStack.EMPTY, gun, tile.getWorld(), tile.getFiringPos(dir), dir, ticks, null, null, true));
            }
            return false;
        }).orElse(false);
    }

}
