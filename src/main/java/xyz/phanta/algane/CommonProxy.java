package xyz.phanta.algane;

import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import xyz.phanta.algane.event.ItemTickHandler;
import xyz.phanta.algane.event.LootTableHandler;
import xyz.phanta.algane.network.SPacketLaserBeam;

import javax.annotation.Nullable;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        Algane.INSTANCE.getNetworkHandler()
                .registerMessage(new SPacketLaserBeam.Handler(), SPacketLaserBeam.class, 0, Side.CLIENT);
        MinecraftForge.EVENT_BUS.register(new ItemTickHandler());
        MinecraftForge.EVENT_BUS.register(new LootTableHandler());
    }

    public void onInit(FMLInitializationEvent event) {
        // NO-OP
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        // NO-OP
    }

    public void spawnParticleLaserBeam(World world, Vec3d from, Vec3d to, int colour, int radius, @Nullable EnumHand hand) {
        Algane.INSTANCE.getNetworkHandler().sendToAllAround(new SPacketLaserBeam(from, to, colour, radius, hand),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), from.x, from.y, from.z, 64D));
    }

}
