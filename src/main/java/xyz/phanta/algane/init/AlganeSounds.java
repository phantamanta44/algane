package xyz.phanta.algane.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import xyz.phanta.algane.Algane;

public class AlganeSounds {

    public static final SoundEvent GUN_SIMPLE_FIRE = new SoundEvent(getSndWeapon("simple.fire"));
    public static final SoundEvent GUN_REPEATER_FIRE = new SoundEvent(getSndWeapon("repeater.fire"));
    public static final SoundEvent GUN_SHOCK_FIRE = new SoundEvent(getSndWeapon("shock.fire"));

    private static ResourceLocation getSndWeapon(String key) {
        return Algane.INSTANCE.newResourceLocation(Algane.MOD_ID + ".weapon." + key);
    }

}
