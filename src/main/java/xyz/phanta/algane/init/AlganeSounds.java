package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import net.minecraft.util.SoundEvent;
import xyz.phanta.algane.Algane;

public class AlganeSounds {

    public static final SoundEvent MACHINE_INSTALL = getSndMachine("install");

    private static SoundEvent getSndMachine(String key) {
        return createSound("machine." + key);
    }

    public static final SoundEvent GUN_SIMPLE_FIRE = getSndWeapon("simple.fire");
    public static final SoundEvent GUN_REPEATER_FIRE = getSndWeapon("repeater.fire");
    public static final SoundEvent GUN_SHOCK_FIRE = getSndWeapon("shock.fire");
    public static final SoundEvent GUN_ORB_FIRE = getSndWeapon("orb.fire");
    public static final SoundEvent GUN_ORB_DETONATE = getSndWeapon("orb.detonate");
    public static final SoundEvent GUN_ORB_CHARGE = getSndWeapon("orb.charge");
    public static final SoundEvent GUN_ORB_COMBO = getSndWeapon("orb.combo");
    public static final SoundEvent GUN_GAUSS_FIRE = getSndWeapon("gauss.fire");
    public static final SoundEvent GUN_GAUSS_CHARGE = getSndWeapon("gauss.charge");

    private static SoundEvent getSndWeapon(String key) {
        return createSound("weapon." + key);
    }

    private static SoundEvent createSound(String key) {
        return Algane.INSTANCE.newSoundEvent(Algane.MOD_ID + "." + key);
    }

    @InitMe
    public static void init() {
        // NO-OP
    }

}
