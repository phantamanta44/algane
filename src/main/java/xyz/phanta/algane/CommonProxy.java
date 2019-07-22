package xyz.phanta.algane;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.phanta.algane.event.ItemTickHandler;
import xyz.phanta.algane.event.LootTableHandler;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ItemTickHandler());
        MinecraftForge.EVENT_BUS.register(new LootTableHandler());
    }

    public void onInit(FMLInitializationEvent event) {
        // NO-OP
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        // NO-OP
    }

}
