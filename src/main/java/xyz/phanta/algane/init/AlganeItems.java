package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.item.ItemLaserCore;
import xyz.phanta.algane.item.ItemLaserGun;
import xyz.phanta.algane.item.ItemLaserModifier;
import xyz.phanta.algane.item.ItemMisc;

@SuppressWarnings("NotNullFieldNotInitialized")
public class AlganeItems {

    @GameRegistry.ObjectHolder(Algane.MOD_ID + ":" + LangConst.ITEM_LASER_GUN)
    public static ItemLaserGun LASER_GUN;
    @GameRegistry.ObjectHolder(Algane.MOD_ID + ":" + LangConst.ITEM_LASER_CORE)
    public static ItemLaserCore LASER_CORE;
    @GameRegistry.ObjectHolder(Algane.MOD_ID + ":" + LangConst.ITEM_LASER_MOD)
    public static ItemLaserModifier LASER_MOD;
    @GameRegistry.ObjectHolder(Algane.MOD_ID + ":" + LangConst.ITEM_MISC)
    public static ItemMisc MISC;

    @InitMe(Algane.MOD_ID)
    public static void init() {
        new ItemLaserGun();
        new ItemLaserCore();
        new ItemLaserModifier();
        new ItemMisc();
    }

}
