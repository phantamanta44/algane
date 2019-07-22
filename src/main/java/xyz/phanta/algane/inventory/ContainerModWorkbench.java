package xyz.phanta.algane.inventory;

import io.github.phantamanta44.libnine.capability.impl.L9AspectInventory;
import io.github.phantamanta44.libnine.gui.L9Container;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import mekanism.common.MekanismItems;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.item.ItemLaserModifier;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.tile.TileModifierWorkbench;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.ModifierAccumulator;

import javax.annotation.Nullable;

public class ContainerModWorkbench extends L9Container {

    private final TileModifierWorkbench tile;

    public ContainerModWorkbench(InventoryPlayer ipl, TileModifierWorkbench tile) {
        super(ipl);
        this.tile = tile;
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 49, 56));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 112, 56));
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public LaserGunModifier getCurrentStats() {
        L9AspectInventory inv = tile.getInventory();
        ItemStack baseStack = inv.getStackInSlot(0);
        if (baseStack.hasCapability(AlganeCaps.LASER_GUN_MOD, null)) {
            LaserGunModifier baseMod = baseStack.getCapability(AlganeCaps.LASER_GUN_MOD, null);
            ItemStack addStack = inv.getStackInSlot(1);
            if (!addStack.isEmpty()) {
                if (addStack.hasCapability(AlganeCaps.LASER_GUN_MOD, null)) {
                    return AlganeUtils.computeJoinedMods(baseMod, addStack.getCapability(AlganeCaps.LASER_GUN_MOD, null));
                } else {
                    return computeAugment(baseMod, addStack);
                }
            } else {
                return baseMod;
            }
        }
        return null;
    }

    public boolean isOperable() {
        L9AspectInventory inv = tile.getInventory();
        return inv.getStackInSlot(0).hasCapability(AlganeCaps.LASER_GUN_MOD, null)
                && !inv.getStackInSlot(1).isEmpty();
    }

    public void combine() {
        sendInteraction(new byte[0]);
    }

    @Override
    public void onClientInteraction(ByteUtils.Reader data) {
        if (isOperable()) {
            L9AspectInventory inv = tile.getInventory();
            LaserGunModifier baseMod = AlganeUtils.getItemLaserMod(inv.getStackInSlot(0));
            ItemStack addStack = inv.getStackInSlot(1);
            LaserGunModifier result;
            if (addStack.hasCapability(AlganeCaps.LASER_GUN_MOD, null)) {
                //noinspection ConstantConditions
                result = AlganeUtils.computeJoinedMods(baseMod, addStack.getCapability(AlganeCaps.LASER_GUN_MOD, null));
            } else {
                result = computeAugment(baseMod, addStack);
            }
            inv.setStackInSlot(0, ItemLaserModifier.createStack(1, result, false));
            inv.setStackInSlot(1, ItemStack.EMPTY);
        }
    }

    private static LaserGunModifier computeAugment(LaserGunModifier base, ItemStack augment) {
        ModifierAccumulator acc = new ModifierAccumulator(base);
        Item augItem = augment.getItem();
        if (augItem == MekanismItems.SpeedUpgrade) {
            acc.addPowerMod(0.01F * augment.getCount());
        } else if (augItem == MekanismItems.EnergyUpgrade) {
            acc.addEfficiencyMod(0.01F * augment.getCount());
        } else if (augItem == MekanismItems.GasUpgrade) {
            acc.addHeatMod(0.01F * augment.getCount());
        }
        return acc;
    }

}
