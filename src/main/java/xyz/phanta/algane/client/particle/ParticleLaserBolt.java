package xyz.phanta.algane.client.particle;

import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
import io.github.phantamanta44.libnine.util.render.RenderUtils;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.constant.ResConst;

public class ParticleLaserBolt extends Particle {

    public ParticleLaserBolt(World world, double x, double y, double z, int colour) {
        super(world, x, y, z);
        this.particleRed = TextFormatUtils.getComponent(colour, 2);
        this.particleGreen = TextFormatUtils.getComponent(colour, 1);
        this.particleBlue = TextFormatUtils.getComponent(colour, 0);
        this.particleMaxAge = 6;
    }

    public ParticleLaserBolt(World world, Vec3d pos, int colour) {
        this(world, pos.x, pos.y, pos.z, colour);
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (particleAge++ >= particleMaxAge) {
            setExpired();
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks,
                               float lookX, float lookXZ, float lookZ, float lookYZ, float lookXY) {
        float x = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float y = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float z = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);
        float frac = 1 - Math.min(particleAge + partialTicks, particleMaxAge) / particleMaxAge;
        float scale = frac / 4F;
        ResConst.PARTICLE_BOLT.bind();
        GlStateManager.enableBlend();
        GlStateManager.color(particleRed, particleGreen, particleBlue, frac);
        RenderUtils.renderWorldOrtho(x, y, z, scale, scale, 0F, lookX, lookXZ, lookZ, lookYZ, lookXY);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableBlend();
    }

}
