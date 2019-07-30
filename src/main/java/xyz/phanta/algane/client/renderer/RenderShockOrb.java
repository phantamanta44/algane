package xyz.phanta.algane.client.renderer;

import io.github.phantamanta44.libnine.util.render.RenderUtils;
import mekanism.common.PacketHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import xyz.phanta.algane.constant.ResConst;
import xyz.phanta.algane.entity.EntityShockOrb;

import javax.annotation.Nullable;

public class RenderShockOrb extends Render<EntityShockOrb> {

    public RenderShockOrb(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(EntityShockOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        ResConst.PARTICLE_ORB.bind();
        if (entity.getDamage() > 20F) {
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderUtils.renderWorldOrtho(x, y + 0.5D, z, 1F, 1F, (entity.ticksExisted + partialTicks) * 20F);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        } else {
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderUtils.renderWorldOrtho(x, y + 0.5D, z, 1F, 1F, (entity.ticksExisted + partialTicks) * 10F);
        }
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityShockOrb entity) {
        return null;
    }

}
