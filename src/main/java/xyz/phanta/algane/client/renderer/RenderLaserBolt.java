package xyz.phanta.algane.client.renderer;

import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import xyz.phanta.algane.client.fx.BloomHandler;
import xyz.phanta.algane.entity.EntityLaserBolt;

import javax.annotation.Nullable;

public class RenderLaserBolt extends Render<EntityLaserBolt> {

    public RenderLaserBolt(RenderManager renderManager) {
        super(renderManager);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void doRender(EntityLaserBolt entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.ticksExisted > 1) {
            BloomHandler.getInstance().withBloom(() -> {
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                int colour = entity.getColour();
                GlStateManager.color(TextFormatUtils.getComponent(colour, 2),
                        TextFormatUtils.getComponent(colour, 1),
                        TextFormatUtils.getComponent(colour, 0), 1F);
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(entity.rotationYaw, 0F, 1F, 0F);
                GlStateManager.rotate(entity.rotationPitch, -1F, 0F, 0F);

                float r = entity.getRadius(), dx = r * 1.73205F, dy = 2 * r, dz = 0.3F;

                Tessellator tess = Tessellator.getInstance();
                BufferBuilder buf = tess.getBuffer();
                buf.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
                buf.pos(-dx, -r, -dz).endVertex();
                buf.pos(-dx, -r, dz).endVertex();
                buf.pos(0, dy, -dz).endVertex();
                buf.pos(0, dy, dz).endVertex();
                buf.pos(dx, -r, -dz).endVertex();
                buf.pos(dx, -r, dz).endVertex();
                buf.pos(-dx, -r, -dz).endVertex();
                buf.pos(-dx, -r, dz).endVertex();
                tess.draw();

                buf.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
                buf.pos(dx, -r, dz).endVertex();
                buf.pos(0, dy, dz).endVertex();
                buf.pos(-dx, -r, dz).endVertex();
                buf.pos(-dx, -r, -dz).endVertex();
                buf.pos(0, dy, -dz).endVertex();
                buf.pos(dx, -r, -dz).endVertex();
                tess.draw();

                GlStateManager.popMatrix();
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
            });
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityLaserBolt entity) {
        return null;
    }

}
