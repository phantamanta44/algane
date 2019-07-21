package xyz.phanta.algane.item.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface TickingUseItem {

    void whileItemInUse(ItemStack stack, EntityPlayer player);

}
