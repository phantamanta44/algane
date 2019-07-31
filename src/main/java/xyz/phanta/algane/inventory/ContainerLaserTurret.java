package xyz.phanta.algane.inventory;

import io.github.phantamanta44.libnine.gui.L9Container;
import net.minecraft.entity.player.InventoryPlayer;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.tile.TileLaserTurret;

public class ContainerLaserTurret extends L9Container {

    private final TileLaserTurret tile;

    public ContainerLaserTurret(InventoryPlayer ipl, TileLaserTurret tile) {
        super(ipl);
        this.tile = tile;
    }

    public LaserGun getGun() {
        return tile.getGun();
    }

}
