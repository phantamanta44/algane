package xyz.phanta.algane.event;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.phanta.algane.item.base.TickingUseItem;

public class ItemUseTickHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.world.isRemote) {
            ItemStack stack = event.player.getActiveItemStack();
            Item item = stack.getItem();
            if (item instanceof TickingUseItem) {
                ((TickingUseItem)item).whileItemInUse(stack, event.player);
            }
        }
    }

}
