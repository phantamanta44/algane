package xyz.phanta.algane.client.particle;

import io.github.phantamanta44.libnine.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import xyz.phanta.algane.client.particle.base.ParticleBeam;
import xyz.phanta.algane.constant.ResConst;

import javax.annotation.Nullable;

public class ParticleAsmdTracer extends ParticleBeam {

    private final int auxCount;
    private final float auxDist;

    public ParticleAsmdTracer(World world, Vec3d from, Vec3d to, @Nullable EnumHandSide side) {
        super(world, from, to, side);
        this.particleRed = 0.25882F;
        this.particleGreen = 0.64706F;
        this.particleBlue = 0.96078F;
        this.particleMaxAge = 18;
        this.auxCount = length > 9 ? 9 : (int)Math.floor(length);
        this.auxDist = length / auxCount;
    }

    @Override
    protected void doRender(float x, float y, float z, float frac, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        if (side == null || mc.gameSettings.thirdPersonView != 0) {
            setUpRender();
            transformWorld(x, y, z);
            renderBeam(frac, partialTicks);
        } else {
            setUpRender();
            transformHand(mc.player, x, y, z, partialTicks);
            renderBeam(frac, partialTicks);
        }
    }

    private void setUpRender() {
        ResConst.PARTICLE_RING.bind();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        RenderUtils.enableFullBrightness();
    }

    private void renderBeam(float frac, float partialTicks) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        alpha(1F - frac);
        drawRing(tess, buf, frac * 3.5F, length - 0.025F);

        int currentAuxCount = Math.min(particleAge, auxCount);
        for (int i = 0; i < currentAuxCount; i += 2) {
            float auxFrac = Math.min((particleAge + partialTicks - i) / (float)(particleMaxAge - i), 1F);
            alpha(1F - auxFrac);
            drawRing(tess, buf, auxFrac * 0.5F, i * auxDist);
        }

        RenderUtils.restoreLightmap();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableBlend();
    }

    private void alpha(float alpha) {
        GlStateManager.color(particleRed, particleGreen, particleBlue, alpha);
    }

    private static void drawRing(Tessellator tess, BufferBuilder buf, float radius, float dist) {
        buf.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-radius, radius, dist).tex(0, 0).endVertex();
        buf.pos(-radius, -radius, dist).tex(0, 1).endVertex();
        buf.pos(radius, radius, dist).tex(1, 0).endVertex();
        buf.pos(radius, -radius, dist).tex(1, 1).endVertex();
        buf.pos(-radius, radius, dist).tex(0, 0).endVertex();
        buf.pos(-radius, -radius, dist).tex(0, 1).endVertex();
        tess.draw();
    }

}
