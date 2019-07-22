package xyz.phanta.algane.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import xyz.phanta.algane.Algane;

public class AlganeSounds {

    public static final SoundEvent GUN_SIMPLE_FIRE = new SoundEvent(getSndWeapon("simple.fire"));

    private static ResourceLocation getSndWeapon(String key) {
        return Algane.INSTANCE.newResourceLocation(Algane.MOD_ID + ".weapon." + key);
    }

}
