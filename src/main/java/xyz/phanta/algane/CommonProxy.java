package xyz.phanta.algane;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xyz.phanta.algane.event.ItemTickHandler;
import xyz.phanta.algane.event.LootTableHandler;
import xyz.phanta.algane.network.SPacketAsmdTracer;
import xyz.phanta.algane.network.SPacketChargeSound;
import xyz.phanta.algane.network.SPacketLaserBeam;
import xyz.phanta.algane.network.SPacketShockBlast;

import javax.annotation.Nullable;
import java.util.UUID;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        SimpleNetworkWrapper net = Algane.INSTANCE.getNetworkHandler();
        net.registerMessage(new SPacketLaserBeam.Handler(), SPacketLaserBeam.class, 0, Side.CLIENT);
        net.registerMessage(new SPacketAsmdTracer.Handler(), SPacketAsmdTracer.class, 1, Side.CLIENT);
        net.registerMessage(new SPacketShockBlast.Handler(), SPacketShockBlast.class, 2, Side.CLIENT);
        net.registerMessage(new SPacketChargeSound.Handler(), SPacketChargeSound.class, 32, Side.CLIENT);
        MinecraftForge.EVENT_BUS.register(new ItemTickHandler());
        MinecraftForge.EVENT_BUS.register(new LootTableHandler());
    }

    public void onInit(FMLInitializationEvent event) {
        // NO-OP
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        // NO-OP
    }

    public void spawnParticleLaserBeam(World world, Vec3d from, Vec3d to, int colour, int radius,
                                       @Nullable UUID ownerId, @Nullable EnumHand hand) {
        Algane.INSTANCE.getNetworkHandler().sendToAllAround(new SPacketLaserBeam(from, to, colour, radius, ownerId, hand),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), from.x, from.y, from.z, 64D));
    }

    public void spawnParticleAsmd(World world, Vec3d from, Vec3d to, @Nullable UUID ownerId, @Nullable EnumHand hand) {
        Algane.INSTANCE.getNetworkHandler().sendToAllAround(new SPacketAsmdTracer(from, to, ownerId, hand),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), from.x, from.y, from.z, 64D));
    }

    public void spawnParticleShockBlast(World world, Vec3d pos, float radius, float intensity, int colour) {
        Algane.INSTANCE.getNetworkHandler().sendToAllAround(new SPacketShockBlast(pos, radius, intensity, colour),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.x, pos.y, pos.z, 64D));
    }

    public void playOrbChargeFx(World world, EntityLivingBase player) {
        if (player instanceof EntityPlayerMP) {
            Algane.INSTANCE.getNetworkHandler().sendTo(new SPacketChargeSound((byte)1), (EntityPlayerMP)player);
        }
    }

    public void stopChargeFx(World world, EntityLivingBase player) {
        if (player instanceof EntityPlayerMP) {
            Algane.INSTANCE.getNetworkHandler().sendTo(new SPacketChargeSound((byte)0), (EntityPlayerMP)player);
        }
    }

}
