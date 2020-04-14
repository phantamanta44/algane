package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.block.BlockLaserGunTable;
import xyz.phanta.algane.block.BlockLaserTurret;
import xyz.phanta.algane.constant.LangConst;

@SuppressWarnings("NotNullFieldNotInitialized")
public class AlganeBlocks {

    @GameRegistry.ObjectHolder(Algane.MOD_ID + ":" + LangConst.BLOCK_LASER_GUN_TABLE)
    public static BlockLaserGunTable WORKBENCH;
    @GameRegistry.ObjectHolder(Algane.MOD_ID + ":" + LangConst.BLOCK_LASER_TURRET)
    public static BlockLaserTurret TURRET;

    @InitMe(Algane.MOD_ID)
    public static void init() {
        new BlockLaserGunTable();
        new BlockLaserTurret();
    }

}
