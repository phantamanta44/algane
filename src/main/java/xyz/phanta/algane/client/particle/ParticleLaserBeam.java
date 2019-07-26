package xyz.phanta.algane.client.particle;

import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
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

import javax.annotation.Nullable;

public class ParticleLaserBeam extends ParticleBeam {

    public ParticleLaserBeam(World world, Vec3d from, Vec3d to, int colour, int radius, @Nullable EnumHandSide side) {
        super(world, from, to, side);
        this.particleRed = TextFormatUtils.getComponent(colour, 2);
        this.particleGreen = TextFormatUtils.getComponent(colour, 1);
        this.particleBlue = TextFormatUtils.getComponent(colour, 0);
        this.particleMaxAge = radius;
    }

    @Override
    protected void doRender(float x, float y, float z, float frac, float partialTicks) {
        float r = (particleMaxAge - particleAge) / 80F, dx = r * 1.73205F, dy = 2 * r;
        Minecraft mc = Minecraft.getMinecraft();
        if (side == null || mc.gameSettings.thirdPersonView != 0) {
            setUpRender(1F - frac);
            transformWorld(x, y, z);
            renderBeam(r, dx, dy);
        } else {
            setUpRender(1F - frac);
            transformHand(mc.player, x, y, z, partialTicks);
            renderBeam(r, dx, dy);
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

    private void renderBeam(float r, float dx, float dy) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
        buf.pos(-dx, -r, 0).endVertex();
        buf.pos(-dx, -r, length).endVertex();
        buf.pos(0, dy, 0).endVertex();
        buf.pos(0, dy, length).endVertex();
        buf.pos(dx, -r, 0).endVertex();
        buf.pos(dx, -r, length).endVertex();
        buf.pos(-dx, -r, 0).endVertex();
        buf.pos(-dx, -r, length).endVertex();
        tess.draw();

        buf.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        buf.pos(-dx, -r, 0);
        buf.pos(dx, -r, 0);
        buf.pos(0, dy, 0);
        buf.pos(0, dy, length);
        buf.pos(dx, -r, length);
        buf.pos(-dx, -r, length);
        tess.draw();

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

}
