package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.libnine.gui.GuiIdentity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.client.gui.GuiGunWorkbench;
import xyz.phanta.algane.client.gui.GuiLaserTurret;
import xyz.phanta.algane.client.gui.GuiModWorkbench;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.inventory.ContainerGunWorkbench;
import xyz.phanta.algane.inventory.ContainerLaserTurret;
import xyz.phanta.algane.inventory.ContainerModWorkbench;

import java.util.Objects;

public class AlganeGuis {

    public static final GuiIdentity<ContainerGunWorkbench, GuiGunWorkbench> LASER_GUN_TABLE
            = new GuiIdentity<>(LangConst.INV_LASER_GUN_TABLE, ContainerGunWorkbench.class);
    public static final GuiIdentity<ContainerModWorkbench, GuiModWorkbench> MODIFIER_TABLE
            = new GuiIdentity<>(LangConst.INV_MODIFIER_TABLE, ContainerModWorkbench.class);
    public static final GuiIdentity<ContainerLaserTurret, GuiLaserTurret> LASER_TURRET
            = new GuiIdentity<>(LangConst.INV_LASER_TURRET, ContainerLaserTurret.class);

    @InitMe(Algane.MOD_ID)
    public static void init() {
        LibNine.PROXY.getRegistrar().queueGuiServerReg(LASER_GUN_TABLE,
                (p, w, x, y, z) -> new ContainerGunWorkbench(p.inventory, getTile(w, x, y, z)));
        LibNine.PROXY.getRegistrar().queueGuiServerReg(MODIFIER_TABLE,
                (p, w, x, y, z) -> new ContainerModWorkbench(p.inventory, getTile(w, x, y, z)));
        LibNine.PROXY.getRegistrar().queueGuiServerReg(LASER_TURRET,
                (p, w, x, y, z) -> new ContainerLaserTurret(p.inventory, getTile(w, x, y, z)));
    }

    @InitMe(value = Algane.MOD_ID, sides = { Side.CLIENT })
    @SideOnly(Side.CLIENT)
    public static void clientInit() {
        LibNine.PROXY.getRegistrar().queueGuiClientReg(LASER_GUN_TABLE, (c, p, w, x, y, z) -> new GuiGunWorkbench(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(MODIFIER_TABLE, (c, p, w, x, y, z) -> new GuiModWorkbench(c));
        LibNine.PROXY.getRegistrar().queueGuiClientReg(LASER_TURRET, (c, p, w, x, y, z) -> new GuiLaserTurret(c));
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> T getTile(World world, int x, int y, int z) {
        return (T)Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z)));
    }

}
