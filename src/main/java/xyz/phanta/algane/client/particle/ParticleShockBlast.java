package xyz.phanta.algane.client.particle;

import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
import io.github.phantamanta44.libnine.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import xyz.phanta.algane.constant.ResConst;

public class ParticleShockBlast extends DelayedParticleRenderer.DelayedParticle {

    private final Vec3d pos;
    private final float radius;
    private final float intensity;

    public ParticleShockBlast(World world, Vec3d pos, float radius, float intensity, int colour) {
        super(world, pos.x, pos.y, pos.z);
        this.pos = pos;
        this.radius = radius;
        this.intensity = intensity;
        this.particleRed = TextFormatUtils.getComponent(colour, 2);
        this.particleGreen = TextFormatUtils.getComponent(colour, 1);
        this.particleBlue = TextFormatUtils.getComponent(colour, 0);
        this.particleMaxAge = 20;
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
        Vec3d diffPos = pos.subtract(RenderUtils.getInterpPos(Minecraft.getMinecraft().player, partialTicks));
        float drawRadius = radius;
        float ticksAlive = particleAge + particleAge;
        float spin = ticksAlive * 20F;
        if (ticksAlive < 4) {
            drawRadius *= ticksAlive / 4F;
            GlStateManager.color(particleRed, particleGreen, particleBlue, 1F);
        } else {
            float frac = 1F - Math.min((ticksAlive - 4F) / (particleMaxAge - 4F), 1F);
            GlStateManager.color(particleRed * frac, particleGreen * frac, particleBlue * frac, frac);
        }

        GlStateManager.disableLighting();
        RenderUtils.enableFullBrightness();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        ResConst.PARTICLE_BLAST_RING.bind();
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.translate(diffPos.x, diffPos.y, diffPos.z);
        drawPlane(tess, buf, drawRadius, spin);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(diffPos.x, diffPos.y, diffPos.z);
        GlStateManager.rotate(90F, 0F, 1F, 0F);
        drawPlane(tess, buf, drawRadius, spin);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(diffPos.x, diffPos.y, diffPos.z);
        GlStateManager.rotate(90F, 1F, 0F, 0F);
        drawPlane(tess, buf, drawRadius, spin);
        GlStateManager.popMatrix();

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderUtils.restoreLightmap();
        GlStateManager.enableLighting();
    }

    private static void drawPlane(Tessellator tess, BufferBuilder buf, float radius, float spin) {
        GlStateManager.rotate(spin, 0F, 0F, 1F);
        buf.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-radius, radius, 0).tex(0, 0).endVertex();
        buf.pos(-radius, -radius, 0).tex(0, 1).endVertex();
        buf.pos(radius, radius, 0).tex(1, 0).endVertex();
        buf.pos(radius, -radius, 0).tex(1, 1).endVertex();
        buf.pos(-radius, radius, 0).tex(0, 0).endVertex();
        buf.pos(-radius, -radius, 0).tex(0, 1).endVertex();
        tess.draw();
    }

}
