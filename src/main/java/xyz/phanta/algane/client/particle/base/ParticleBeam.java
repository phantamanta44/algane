package xyz.phanta.algane.client.particle.base;

import io.github.phantamanta44.libnine.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.client.particle.DelayedParticleRenderer;

import javax.annotation.Nullable;

public abstract class ParticleBeam extends DelayedParticleRenderer.DelayedParticle {

    protected final float length, theta, phi;
    protected final Vec3d dir;
    @Nullable
    protected final EnumHandSide side;

    protected ParticleBeam(World world, Vec3d from, Vec3d to, @Nullable EnumHandSide side) {
        super(world, from.x, from.y, from.z);
        this.side = side;
        Vec3d diffPos = to.subtract(from);
        this.length = (float)diffPos.length();
        this.phi = (float)Math.asin(MathUtils.clamp(diffPos.y, -length, length) / length) * MathUtils.R2D_F;
        if (phi >= 89.98F || phi <= -89.98F) {
            this.theta = side == null ? 0F : Minecraft.getMinecraft().player.rotationYaw;
        } else {
            this.theta = (float)Math.atan2(diffPos.z, diffPos.x) * MathUtils.R2D_F - 90F;
        }
        this.dir = diffPos.scale(1F / length);
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
    public void renderDelayed(float partialTicks) {
        doRender((float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX),
                (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY),
                (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ),
                Math.min(particleAge + partialTicks, particleMaxAge) / particleMaxAge, partialTicks);
    }

    protected abstract void doRender(float x, float y, float z, float frac, float partialTicks);

    protected void transformWorld(float x, float y, float z) {
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(theta, 0F, -1F, 0F);
        GlStateManager.rotate(phi, -1F, 0F, 0F);
    }

    protected void transformHand(EntityPlayerSP player, float x, float y, float z, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        float fovV = mc.entityRenderer.getFOVModifier(partialTicks, true);
        float fovI = mc.entityRenderer.getFOVModifier(partialTicks, false);
        transformWorld(x, y, z);
        GlStateManager.translate(side == EnumHandSide.LEFT ? 0.48F : -0.48F, -0.26F, 1.56F * fovI / fovV);
    }

}
