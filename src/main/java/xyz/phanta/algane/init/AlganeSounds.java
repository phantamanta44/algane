package xyz.phanta.algane.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import xyz.phanta.algane.Algane;

public class AlganeSounds {

    public static final SoundEvent GUN_SIMPLE_FIRE = new SoundEvent(getSndWeapon("simple.fire"));
    public static final SoundEvent GUN_REPEATER_FIRE = new SoundEvent(getSndWeapon("repeater.fire"));
    public static final SoundEvent GUN_SHOCK_FIRE = new SoundEvent(getSndWeapon("shock.fire"));
    public static final SoundEvent GUN_ORB_FIRE = new SoundEvent(getSndWeapon("orb.fire"));
    public static final SoundEvent GUN_ORB_DETONATE = new SoundEvent(getSndWeapon("orb.detonate"));
    public static final SoundEvent GUN_ORB_CHARGE = new SoundEvent(getSndWeapon("orb.charge"));
    public static final SoundEvent GUN_ORB_COMBO = new SoundEvent(getSndWeapon("orb.combo"));
    public static final SoundEvent GUN_GAUSS_FIRE = new SoundEvent(getSndWeapon("gauss.fire"));
    public static final SoundEvent GUN_GAUSS_CHARGE = new SoundEvent(getSndWeapon("gauss.charge"));

    private static ResourceLocation getSndWeapon(String key) {
        return Algane.INSTANCE.newResourceLocation(Algane.MOD_ID + ".weapon." + key);
    }

}
