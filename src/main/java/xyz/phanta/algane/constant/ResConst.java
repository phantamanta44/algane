package xyz.phanta.algane.constant;

import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.libnine.util.render.TextureResource;
import net.minecraft.util.ResourceLocation;
import xyz.phanta.algane.Algane;

public class ResConst {

    public static final ResourceLocation GUI_LASER_GUN_TABLE = getGuiResource("laser_workbench");
    public static final ResourceLocation GUI_MODIFIER_TABLE = getGuiResource("mod_workbench");

    private static ResourceLocation getGuiResource(String key) {
        return Algane.INSTANCE.newResourceLocation("textures/gui/" + key + ".png");
    }

    private static final TextureResource GUI_COMP_SUBMIT = getGuiCompResource("submit", 39, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_NORMAL = GUI_COMP_SUBMIT.getRegion(0, 0, 13, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_DISABLED = GUI_COMP_SUBMIT.getRegion(13, 0, 13, 13);
    public static final TextureRegion GUI_COMP_SUBMIT_HOVERED = GUI_COMP_SUBMIT.getRegion(26, 0, 13, 13);

    private static TextureResource getGuiCompResource(String key, int width, int height) {
        return Algane.INSTANCE.newTextureResource("textures/gui/component/" + key + ".png", width, height);
    }

    public static final TextureResource PARTICLE_BOLT = getParticleResource("bolt", 32, 32);

    private static TextureResource getParticleResource(String key, int width, int height) {
        return Algane.INSTANCE.newTextureResource("textures/particle/" + key + ".png", width, height);
    }

}
