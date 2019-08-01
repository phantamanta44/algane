package xyz.phanta.algane.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DelayedParticleRenderer {

    @Nullable
    private static DelayedParticleRenderer INSTANCE;

    public static DelayedParticleRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DelayedParticleRenderer();
        }
        return INSTANCE;
    }

    private final List<DelayedParticle> renderQueue = new ArrayList<>();

    private void queueRender(DelayedParticle particle) {
        renderQueue.add(particle);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        float partialTicks = event.getPartialTicks();
        GlStateManager.enableBlend();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        renderQueue.forEach(p -> p.renderDelayed(partialTicks));
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.disableBlend();
        renderQueue.clear();
    }

    public static abstract class DelayedParticle extends net.minecraft.client.particle.Particle {

        protected float lookX, lookXZ, lookZ, lookYZ, lookXY;

        protected DelayedParticle(World world, double posX, double posY, double posZ) {
            super(world, posX, posY, posZ);
        }

        @Override
        public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks,
                                   float lookX, float lookXZ, float lookZ, float lookYZ, float lookXY) {
            this.lookX = lookX;
            this.lookXZ = lookXZ;
            this.lookZ = lookZ;
            this.lookYZ = lookYZ;
            this.lookXY = lookXY;
            getInstance().queueRender(this);
        }

        protected abstract void renderDelayed(float partialTicks);

    }

}
