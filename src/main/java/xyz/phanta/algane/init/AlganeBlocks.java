package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.block.BlockLaserGunTable;
import xyz.phanta.algane.block.BlockLaserTurret;

@SuppressWarnings("NullableProblems")
public class AlganeBlocks {

    public static BlockLaserGunTable WORKBENCH;
    public static BlockLaserTurret TURRET;

    @InitMe(Algane.MOD_ID)
    public static void init() {
        WORKBENCH = new BlockLaserGunTable();
        TURRET = new BlockLaserTurret();
    }

}
