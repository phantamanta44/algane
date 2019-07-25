package xyz.phanta.algane.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.phanta.algane.util.AlganeUtils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class HandRenderHandler {

    @SuppressWarnings("NullableProblems")
    private static HandRenderHandler INSTANCE;

    public static void init() {
        INSTANCE = new HandRenderHandler();
    }

    public static HandRenderHandler getInstance() {
        return INSTANCE;
    }

    private final Minecraft mc = Minecraft.getMinecraft();
    private final EnumMap<EnumHandSide, List<Runnable>> renderQueues = new EnumMap<>(EnumHandSide.class);

    private HandRenderHandler() {
        renderQueues.put(EnumHandSide.LEFT, new ArrayList<>());
        renderQueues.put(EnumHandSide.RIGHT, new ArrayList<>());
    }

    public void queueRender(EnumHandSide hand, Runnable renderer) {
        renderQueues.get(hand).add(renderer);
    }

    @SubscribeEvent
    public void onRenderHand(RenderSpecificHandEvent event) {
        renderQueues.get(AlganeUtils.getHandSide(event.getHand())).forEach(Runnable::run);
    }

    @SubscribeEvent
    public void onRenderFinished(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            renderQueues.forEach((h, q) -> q.clear());
        }
    }

}
