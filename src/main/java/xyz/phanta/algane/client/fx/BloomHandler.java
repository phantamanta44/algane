package xyz.phanta.algane.client.fx;

import io.github.phantamanta44.libnine.util.render.shader.ShaderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import xyz.phanta.algane.init.AlganeShaders;

import java.util.LinkedList;
import java.util.List;

public class BloomHandler {

    @SuppressWarnings("NullableProblems")
    private static BloomHandler INSTANCE;

    public static void init() {
        INSTANCE = new BloomHandler();
    }

    public static BloomHandler getInstance() {
        return INSTANCE;
    }

    private final Minecraft mc = Minecraft.getMinecraft();
    private int knownWidth = mc.displayWidth, knownHeight = mc.displayHeight;
    private final Framebuffer bloomBuf = new Framebuffer(knownWidth, knownHeight, false);
    private final Framebuffer swapBuf = new Framebuffer(knownWidth, knownHeight, false);
    private boolean needsUpdate = true;
    private final List<Runnable> renderQueue = new LinkedList<>();

    private BloomHandler() {
        bloomBuf.setFramebufferColor(0F, 0F, 0F, 0F);
        swapBuf.setFramebufferColor(0F, 0F, 0F, 0F);
    }

    public void withBloom(Runnable render) {
        renderQueue.add(render);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!renderQueue.isEmpty()) {
            bloomBuf.bindFramebuffer(true);
            renderQueue.forEach(Runnable::run);
            renderQueue.clear();

            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0D, knownWidth, knownHeight, 0D, 1000D, 3000D);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0F, 0F, -2000F);
            GlStateManager.viewport(0, 0, knownWidth, knownHeight);
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();
            Vec2f dims = new Vec2f(knownWidth, knownHeight);

            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            AlganeShaders.BLOOM.use()
                    .setUniform(AlganeShaders.BLOOM_SIZE, dims)
                    .setUniform(AlganeShaders.BLOOM_TEX_SAMPLER, 0)
                    .setUniform(AlganeShaders.BLOOM_RADIUS, 8F)
                    .setUniform(AlganeShaders.BLOOM_DIRECTION, Vec2f.UNIT_X);
            blit(bloomBuf, swapBuf);

            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            AlganeShaders.BLOOM.setUniform(AlganeShaders.BLOOM_DIRECTION, Vec2f.UNIT_Y);
            blit(swapBuf, mc.getFramebuffer());

            AlganeShaders.WHITE.use()
                    .setUniform(AlganeShaders.WHITE_SIZE, dims)
                    .setUniform(AlganeShaders.WHITE_TEX_SAMPLER, 0);
            mc.getFramebuffer().bindFramebuffer(true);
            blit(bloomBuf, mc.getFramebuffer());

            ShaderUtils.clearShaderProgram();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();

            bloomBuf.framebufferClear();
            swapBuf.framebufferClear();
            mc.getFramebuffer().bindFramebuffer(true);
        }
    }

    @SubscribeEvent
    public void onRenderStart(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START
                && (needsUpdate || mc.displayWidth != knownWidth || mc.displayHeight != knownHeight)) {
            knownWidth = mc.displayWidth;
            knownHeight = mc.displayHeight;
            bloomBuf.createBindFramebuffer(knownWidth, knownHeight);
            swapBuf.createBindFramebuffer(knownWidth, knownHeight);
            stealDepthBuffer();
            needsUpdate = false;
        }
    }

    private void stealDepthBuffer() {
        Framebuffer mainBuf = mc.getFramebuffer();
        bloomBuf.bindFramebuffer(false);
        OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT,
                OpenGlHelper.GL_RENDERBUFFER, mainBuf.depthBuffer);
        mainBuf.bindFramebuffer(false);
    }

    private void blit(Framebuffer src, Framebuffer dest) {
        src.bindFramebufferTexture();
        dest.bindFramebuffer(true);
        float uMax = src.framebufferWidth / (float)src.framebufferTextureWidth;
        float vMax = src.framebufferHeight / (float)src.framebufferTextureHeight;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(0D, knownHeight, 0D).tex(0D, 0D).endVertex();
        buf.pos(knownWidth, knownHeight, 0D).tex(uMax, 0D).endVertex();
        buf.pos(knownWidth, 0D, 0D).tex(uMax, vMax).endVertex();
        buf.pos(0D, 0D, 0D).tex(0D, vMax).endVertex();
        tess.draw();
        src.unbindFramebufferTexture();
    }

}
