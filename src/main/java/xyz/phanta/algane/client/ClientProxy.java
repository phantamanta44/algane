package xyz.phanta.algane.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.phanta.algane.CommonProxy;
import xyz.phanta.algane.client.event.HandRenderHandler;
import xyz.phanta.algane.client.event.WeaponOverlayRenderer;
import xyz.phanta.algane.client.fx.BloomHandler;
import xyz.phanta.algane.client.particle.ParticleLaserBeam;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        BloomHandler.init();
        HandRenderHandler.init();
        MinecraftForge.EVENT_BUS.register(BloomHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new WeaponOverlayRenderer());
        MinecraftForge.EVENT_BUS.register(HandRenderHandler.getInstance());
    }

    @Override
    public void spawnParticleLaserBeam(World world, Vec3d from, Vec3d to, int colour, int radius, @Nullable EnumHand hand) {
        if (world.isRemote) {
            Minecraft.getMinecraft().effectRenderer.addEffect(
                    new ParticleLaserBeam(world, from, to, colour, radius, hand == null ? null : AlganeUtils.getHandSide(hand)));
        } else {
            super.spawnParticleLaserBeam(world, from, to, colour, radius, hand);
        }
    }

}
