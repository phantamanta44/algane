package xyz.phanta.algane.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
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
import xyz.phanta.algane.client.particle.ParticleShockBlast;
import xyz.phanta.algane.init.AlganeSounds;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy {

    @SuppressWarnings("NullableProblems")
    private WeaponChargeHandler chargeHandler;

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        BloomHandler.init();
        MinecraftForge.EVENT_BUS.register(BloomHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new WeaponOverlayRenderer());
        MinecraftForge.EVENT_BUS.register(new ModelRegistrationHandler());
        MinecraftForge.EVENT_BUS.register(chargeHandler = new WeaponChargeHandler());
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

    @Override
    public void spawnParticleAsmd(World world, Vec3d from, Vec3d to, @Nullable EnumHand hand) {
        if (world.isRemote) {
            Minecraft.getMinecraft().effectRenderer.addEffect(
                    new ParticleAsmdTracer(world, from, to, hand == null ? null : AlganeUtils.getHandSide(hand)));
        } else {
            super.spawnParticleAsmd(world, from, to, hand);
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

    @Override
    public void playOrbChargeFx(World world, EntityLivingBase player) {
        if (world.isRemote) {
            chargeHandler.playChargeSound(AlganeSounds.GUN_ORB_CHARGE);
        } else {
            super.playOrbChargeFx(world, player);
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
