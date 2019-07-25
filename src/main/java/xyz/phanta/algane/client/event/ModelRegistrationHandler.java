package xyz.phanta.algane.client.event;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.algane.client.renderer.RenderLaserBolt;
import xyz.phanta.algane.entity.EntityLaserBolt;

public class ModelRegistrationHandler {

    @SubscribeEvent
    public void onModelRegistration(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityLaserBolt.class, RenderLaserBolt::new);
    }

}
