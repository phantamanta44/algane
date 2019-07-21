package xyz.phanta.algane.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class UnmodifiableSlot extends SlotItemHandler {

    public UnmodifiableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return false;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        // NO-OP
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
        return ItemStack.EMPTY;
    }

}
