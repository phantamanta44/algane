package xyz.phanta.algane.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.phanta.algane.CommonProxy;
import xyz.phanta.algane.client.event.ModelRegistrationHandler;
import xyz.phanta.algane.client.event.WeaponOverlayRenderer;
import xyz.phanta.algane.client.fx.BloomHandler;
import xyz.phanta.algane.client.particle.ParticleLaserBolt;

public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        BloomHandler.init();
        MinecraftForge.EVENT_BUS.register(BloomHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new WeaponOverlayRenderer());
        MinecraftForge.EVENT_BUS.register(new ModelRegistrationHandler());
    }

    @Override
    public void spawnParticleBolt(World world, Vec3d pos, int colour) {
        if (world.isRemote) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLaserBolt(world, pos, colour));
        }
    }

}
