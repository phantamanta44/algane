package xyz.phanta.algane.inventory;

import io.github.phantamanta44.libnine.capability.impl.L9AspectInventory;
import io.github.phantamanta44.libnine.gui.L9Container;
import io.github.phantamanta44.libnine.gui.slot.UnmodifiableSlot;
import io.github.phantamanta44.libnine.util.LazyConstant;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.item.ItemMisc;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunPart;
import xyz.phanta.algane.tile.TileGunWorkbench;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.DummyItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class ContainerGunWorkbench extends L9Container {

    private final TileGunWorkbench tile;

    public ContainerGunWorkbench(InventoryPlayer ipl, TileGunWorkbench tile) {
        super(ipl);
        this.tile = tile;
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 35, 47));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 98, 47));
        Supplier<ItemStack> getGun = () -> tile.getInventory().getStackInSlot(0);
        addSlotToContainer(new UnmodifiableSlot(new GunSlotProxy(getGun, LaserGun::getCore), 0, 35, 24));
        addSlotToContainer(new UnmodifiableSlot(new GunSlotProxy(getGun, LaserGun::getEnergyCell), 0, 53, 24));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new UnmodifiableSlot(new GunModProxy(getGun, i), 0, 73 + 18 * i, 24));
        }
    }

    @Nullable
    public LaserGunPart getAttachmentType() {
        L9AspectInventory inv = tile.getInventory();
        ItemStack gunStack = inv.getStackInSlot(0);
        if (gunStack.hasCapability(AlganeCaps.LASER_GUN, null)) {
            LaserGunPart partType = LaserGunPart.getPartType(inv.getStackInSlot(1));
            if (partType != null && AlganeUtils.getItemLaserGun(gunStack).canAttachPart(partType)) {
                return partType;
            }
        }
        return null;
    }

    public void attachPart() {
        sendInteraction(new byte[0]);
    }

    @Override
    public void onClientInteraction(ByteUtils.Reader data) {
        LaserGunPart partType = getAttachmentType();
        if (partType != null) {
            L9AspectInventory inv = tile.getInventory();
            AlganeUtils.getItemLaserGun(inv.getStackInSlot(0)).attachPart(partType, inv.getStackInSlot(1));
            inv.setStackInSlot(1, ItemStack.EMPTY);
            detectAndSendChanges();
        }
    }

    private static class GunSlotProxy extends DummyItemHandler {

        private final Supplier<ItemStack> getGun;
        private final Function<LaserGun, ItemStack> getStack;

        GunSlotProxy(Supplier<ItemStack> getGun, Function<LaserGun, ItemStack> getStack) {
            this.getGun = getGun;
            this.getStack = getStack;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            ItemStack gunStack = getGun.get();
            if (!gunStack.isEmpty()) {
                return getStack.apply(AlganeUtils.getItemLaserGun(gunStack));
            }
            return ItemStack.EMPTY;
        }

    }

    private static class GunModProxy extends DummyItemHandler {

        private final Supplier<ItemStack> getGun;
        private final int modSlot;
        private final LazyConstant<ItemStack> noModStack = new LazyConstant<>(() -> ItemMisc.Type.NO_MODIFIER.createStack(1));

        GunModProxy(Supplier<ItemStack> getGun, int modSlot) {
            this.getGun = getGun;
            this.modSlot = modSlot;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            ItemStack gunStack = getGun.get();
            if (!gunStack.isEmpty()) {
                LaserGun gun = AlganeUtils.getItemLaserGun(gunStack);
                if (modSlot >= gun.getModifierCount()) {
                    return noModStack.get();
                } else {
                    return gun.getModifier(modSlot);
                }
            }
            return ItemStack.EMPTY;
        }

    }

}
