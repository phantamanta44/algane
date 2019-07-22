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

    private static final TextureResource OVERLAY_WEAPON = getOverlayResource("weapon_overlay", 256, 55);
    public static final TextureRegion OVERLAY_WEAPON_BG = OVERLAY_WEAPON.getRegion(0, 0, 256, 32);
    public static final TextureRegion OVERLAY_WEAPON_ENERGY = OVERLAY_WEAPON.getRegion(0, 32, 244, 20);
    public static final TextureRegion OVERLAY_WEAPON_HEAT = OVERLAY_WEAPON.getRegion(0, 52, 121, 3);
    public static final TextureRegion OVERLAY_WEAPON_HEAT_LOCK = OVERLAY_WEAPON.getRegion(121, 52, 121, 3);

    private static TextureResource getOverlayResource(String key, int width, int height) {
        return Algane.INSTANCE.newTextureResource("textures/overlay/" + key + ".png", width, height);
    }

}
