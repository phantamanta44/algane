package xyz.phanta.algane.client.event;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.algane.client.renderer.RenderShockOrb;
import xyz.phanta.algane.entity.EntityShockOrb;

public class ModelRegistrationHandler {

    @SubscribeEvent
    public void onModelRegistration(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityShockOrb.class, RenderShockOrb::new);
    }

}
