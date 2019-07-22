package xyz.phanta.algane.tile;

import io.github.phantamanta44.libnine.capability.impl.L9AspectInventory;
import io.github.phantamanta44.libnine.tile.L9TileEntity;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import mekanism.common.MekanismItems;
import net.minecraft.item.Item;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.init.AlganeItems;

@RegisterTile(Algane.MOD_ID)
public class TileModifierWorkbench extends L9TileEntity {

    @AutoSerialize
    private final L9AspectInventory inv = new L9AspectInventory.Observable(2, (i, o, n) -> markDirty())
            .withPredicate(0, s -> s.getItem() == AlganeItems.LASER_MOD)
            .withPredicate(1, s -> {
                Item item = s.getItem();
                return item == AlganeItems.LASER_MOD || item == MekanismItems.SpeedUpgrade
                        || item == MekanismItems.EnergyUpgrade || item == MekanismItems.GasUpgrade;
            });

    public L9AspectInventory getInventory() {
        return inv;
    }

}
