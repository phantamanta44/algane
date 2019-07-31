package xyz.phanta.algane.lasergun.turret;

import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.tile.TileLaserTurret;

public interface TurretFiringStrategy {

    boolean tick(TileLaserTurret tile, LaserGun gun, boolean powered, boolean offCooldown, int ticks);

}
