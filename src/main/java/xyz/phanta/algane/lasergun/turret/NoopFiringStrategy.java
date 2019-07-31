package xyz.phanta.algane.lasergun.turret;

import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.tile.TileLaserTurret;

public class NoopFiringStrategy implements TurretFiringStrategy {

    public static final NoopFiringStrategy INSTANCE = new NoopFiringStrategy();

    private NoopFiringStrategy() {
        // NO-OP
    }

    @Override
    public boolean tick(TileLaserTurret tile, LaserGun gun, boolean powered, boolean offCooldown, int ticks) {
        return false;
    }

}
