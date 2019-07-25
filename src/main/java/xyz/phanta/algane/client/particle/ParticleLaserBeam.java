package xyz.phanta.algane.client.particle;

import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
import io.github.phantamanta44.libnine.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import xyz.phanta.algane.client.event.HandRenderHandler;

import javax.annotation.Nullable;

public class ParticleLaserBeam extends Particle {

    private final float length, theta, phi;
    @Nullable
    private final EnumHandSide side;

    public ParticleLaserBeam(World world, Vec3d from, Vec3d to, int colour, int radius, @Nullable EnumHandSide side) {
        super(world, from.x, from.y, from.z);
        this.particleRed = TextFormatUtils.getComponent(colour, 2);
        this.particleGreen = TextFormatUtils.getComponent(colour, 1);
        this.particleBlue = TextFormatUtils.getComponent(colour, 0);
        this.particleMaxAge = radius;
        this.side = side;
        Vec3d diffPos = to.subtract(from);
        this.length = (float)diffPos.length();
        this.theta = (float)Math.atan2(diffPos.z, diffPos.x) * MathUtils.R2D_F - 90F;
        this.phi = (float)Math.asin(diffPos.y / length) * MathUtils.R2D_F;
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
        float r = (particleMaxAge - particleAge) / 80F, dx = r * 1.73205F, dy = 2 * r, dz = length;
        Minecraft mc = Minecraft.getMinecraft();
        if (side == null || mc.gameSettings.thirdPersonView != 0) {
            setUpRender(frac);
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(theta, 0F, -1F, 0F);
            GlStateManager.rotate(phi, -1F, 0F, 0F);
            renderBeam(r, dx, dy, dz);
        } else {
            HandRenderHandler.getInstance().queueRender(side, () -> {
                setUpRender(frac);
                boolean leftHand = side == EnumHandSide.LEFT;
                GlStateManager.rotate(mc.player.rotationYaw, 0F, 1F, 0F);
                GlStateManager.translate(-x, y - mc.player.getEyeHeight(), -z);
                GlStateManager.rotate(theta - 180F, 0F, -1F, 0F);
                GlStateManager.rotate(mc.player.rotationPitch + phi, -1F, 0F, 0F);
                GlStateManager.translate(leftHand ? 0.56F : -0.52F, -0.32F, 1.56F);
                renderBeam(r, dx, dy, dz);
            });
        }
    }

    private void setUpRender(float frac) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.color(particleRed, particleGreen, particleBlue, frac);
        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
    }

    private void renderBeam(float r, float dx, float dy, float dz) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
        buf.pos(-dx, -r, 0).endVertex();
        buf.pos(-dx, -r, dz).endVertex();
        buf.pos(0, dy, 0).endVertex();
        buf.pos(0, dy, dz).endVertex();
        buf.pos(dx, -r, 0).endVertex();
        buf.pos(dx, -r, dz).endVertex();
        buf.pos(-dx, -r, 0).endVertex();
        buf.pos(-dx, -r, dz).endVertex();
        tess.draw();

        buf.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        buf.pos(-dx, -r, 0);
        buf.pos(dx, -r, 0);
        buf.pos(0, dy, 0);
        buf.pos(0, dy, dz);
        buf.pos(dx, -r, dz);
        buf.pos(-dx, -r, dz);
        tess.draw();

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

}
