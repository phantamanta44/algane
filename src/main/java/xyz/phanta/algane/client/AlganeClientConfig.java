package xyz.phanta.algane.client;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.phanta.algane.Algane;

@SideOnly(Side.CLIENT)
@Config(modid = Algane.MOD_ID, name = Algane.MOD_ID + "_client")
public class AlganeClientConfig {

    @Config.Comment("The offset of the HUD from the left edge of the screen.")
    @Config.RangeInt(min = 0)
    public static int hudOffsetX = 4;

    @Config.Comment("The offset of the HUD from the bottom edge of the screen.")
    @Config.RangeInt(min = 0)
    public static int hudOffsetY = 4;

    @Config.Comment({
            "Whether to display the damage/firerate/range ratings on laser core item tooltips or not.",
            "Note that these ratings are hard-coded and may not match modified weapon configurations.",
            "Modpack users are advised to leave this setting at whatever the modpack creator selected."
    })
    public static boolean showCoreRatings = true;

}
