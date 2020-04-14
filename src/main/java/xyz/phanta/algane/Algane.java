package xyz.phanta.algane;

import io.github.phantamanta44.libnine.Virtue;
import io.github.phantamanta44.libnine.util.L9CreativeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import xyz.phanta.algane.item.ItemLaserGun;

@Mod(modid = Algane.MOD_ID, version = Algane.VERSION, useMetadata = true)
public class Algane extends Virtue {

    public static final String MOD_ID = "algane";
    public static final String VERSION = "1.0.1";

    @SuppressWarnings("NotNullFieldNotInitialized")
    @Mod.Instance(MOD_ID)
    public static Algane INSTANCE;

    @SuppressWarnings("NotNullFieldNotInitialized")
    @SidedProxy(
            clientSide = "xyz.phanta.algane.client.ClientProxy",
            serverSide = "xyz.phanta.algane.CommonProxy")
    public static CommonProxy PROXY;

    @SuppressWarnings("NotNullFieldNotInitialized")
    public static Logger LOGGER;

    public Algane() {
        super(MOD_ID, new L9CreativeTab(MOD_ID, ItemLaserGun.Tier.ULTIMATE::createStack));
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        PROXY.onPreInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        PROXY.onInit(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        PROXY.onPostInit(event);
    }

}
