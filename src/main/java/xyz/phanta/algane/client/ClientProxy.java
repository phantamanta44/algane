package xyz.phanta.algane.client;

import io.github.phantamanta44.libnine.LibNine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.phanta.algane.CommonProxy;
import xyz.phanta.algane.client.event.ModelRegistrationHandler;
import xyz.phanta.algane.client.event.WeaponChargeHandler;
import xyz.phanta.algane.client.event.WeaponOverlayRenderer;
import xyz.phanta.algane.client.fx.BloomHandler;
import xyz.phanta.algane.client.particle.ParticleAsmdTracer;
import xyz.phanta.algane.client.particle.ParticleLaserBeam;
import xyz.phanta.algane.client.particle.DelayedParticleRenderer;
import xyz.phanta.algane.client.particle.ParticleShockBlast;
import xyz.phanta.algane.init.AlganeItems;
import xyz.phanta.algane.init.AlganeSounds;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;
import java.util.UUID;

public class ClientProxy extends CommonProxy {

    @SuppressWarnings("NullableProblems")
    private WeaponChargeHandler chargeHandler;

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        BloomHandler.init();
        MinecraftForge.EVENT_BUS.register(BloomHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(DelayedParticleRenderer.getInstance());
        MinecraftForge.EVENT_BUS.register(new WeaponOverlayRenderer());
        MinecraftForge.EVENT_BUS.register(new ModelRegistrationHandler());
        MinecraftForge.EVENT_BUS.register(chargeHandler = new WeaponChargeHandler());
        LibNine.PROXY.getRegistrar().queueItemColourHandlerReg(
                (s, i) -> i == 0 ? -1 : AlganeUtils.getLaserCore(AlganeUtils.getItemLaserGun(s))
                        .map(LaserGunCore::getDisplayColour).orElse(0x212121),
                AlganeItems.LASER_GUN
        );
    }

    @Override
    public void spawnParticleLaserBeam(World world, Vec3d from, Vec3d to, int colour, int radius,
                                       @Nullable UUID ownerId, @Nullable EnumHand hand) {
        if (world.isRemote) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.effectRenderer.addEffect(new ParticleLaserBeam(world, from, to, colour, radius, checkOwnership(mc, ownerId, hand)));
        } else {
            super.spawnParticleLaserBeam(world, from, to, colour, radius, ownerId, hand);
        }
    }

    @Override
    public void spawnParticleAsmd(World world, Vec3d from, Vec3d to, @Nullable UUID ownerId, @Nullable EnumHand hand) {
        if (world.isRemote) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.effectRenderer.addEffect(new ParticleAsmdTracer(world, from, to, checkOwnership(mc, ownerId, hand)));
        } else {
            super.spawnParticleAsmd(world, from, to, ownerId, hand);
        }
    }

    @Override
    public void spawnParticleShockBlast(World world, Vec3d pos, float radius, float intensity, int colour) {
        if (world.isRemote) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleShockBlast(world, pos, radius, intensity, colour));
        } else {
            super.spawnParticleShockBlast(world, pos, radius, intensity, colour);
        }
    }

    @Nullable
    private static EnumHandSide checkOwnership(Minecraft mc, @Nullable UUID ownerId, @Nullable EnumHand hand) {
        return ownerId != null && hand != null && mc.player.getUniqueID().equals(ownerId)
                ? AlganeUtils.getHandSide(hand) : null;
    }

    @Override
    public void playOrbChargeFx(World world, EntityLivingBase player) {
        if (world.isRemote) {
            chargeHandler.playChargeSound(AlganeSounds.GUN_ORB_CHARGE);
        } else {
            super.playOrbChargeFx(world, player);
        }
    }

    @Override
    public void playGaussChargeFx(World world, EntityLivingBase player) {
        if (world.isRemote) {
            chargeHandler.playChargeSound(AlganeSounds.GUN_GAUSS_CHARGE);
        } else {
            super.playGaussChargeFx(world, player);
        }
    }

    @Override
    public void stopChargeFx(World world, EntityLivingBase player) {
        if (world.isRemote) {
            chargeHandler.stopChargeSound();
        } else {
            super.stopChargeFx(world, player);
        }
    }

}
