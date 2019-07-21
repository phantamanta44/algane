package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.item.ItemLaserCore;
import xyz.phanta.algane.item.ItemLaserGun;
import xyz.phanta.algane.item.ItemMisc;

@SuppressWarnings("NullableProblems")
public class AlganeItems {

    public static ItemLaserGun LASER_GUN;
    public static ItemLaserCore LASER_CORE;
    public static ItemMisc MISC;

    @InitMe(Algane.MOD_ID)
    public static void init() {
        LASER_GUN = new ItemLaserGun();
        LASER_CORE = new ItemLaserCore();
        MISC = new ItemMisc();
    }

}
