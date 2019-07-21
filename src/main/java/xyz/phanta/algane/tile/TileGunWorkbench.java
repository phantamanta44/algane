package xyz.phanta.algane.tile;

import io.github.phantamanta44.libnine.capability.impl.L9AspectInventory;
import io.github.phantamanta44.libnine.tile.L9TileEntity;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.lasergun.LaserGunPart;

@RegisterTile(Algane.MOD_ID)
public class TileGunWorkbench extends L9TileEntity {

    @AutoSerialize
    private final L9AspectInventory inv = new L9AspectInventory.Observable(2, (i, o, n) -> markDirty())
            .withPredicate(0, s -> s.hasCapability(AlganeCaps.LASER_GUN, null))
            .withPredicate(1, s -> LaserGunPart.getPartType(s) != null);

    public L9AspectInventory getInventory() {
        return inv;
    }

}
