package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.block.BlockLaserGunTable;

@SuppressWarnings("NullableProblems")
public class AlganeBlocks {

    public static BlockLaserGunTable WORKBENCH;

    @InitMe(Algane.MOD_ID)
    public static void init() {
        WORKBENCH = new BlockLaserGunTable();
    }

}
