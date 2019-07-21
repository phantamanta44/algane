package xyz.phanta.algane.block;

import io.github.phantamanta44.libnine.block.L9BlockStated;
import io.github.phantamanta44.libnine.gui.GuiIdentity;
import io.github.phantamanta44.libnine.tile.L9TileEntity;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.world.WorldBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeBlocks;
import xyz.phanta.algane.init.AlganeGuis;
import xyz.phanta.algane.tile.TileGunWorkbench;
import xyz.phanta.algane.tile.TileModifierWorkbench;

import java.util.function.Supplier;

public class BlockLaserGunTable extends L9BlockStated {

    private static final IProperty<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockLaserGunTable() {
        super(LangConst.BLOCK_LASER_GUN_TABLE, Material.IRON);
        setTileFactory((w, m) -> Type.getForMeta(m).createTileEntity());
    }

    @Override
    protected void accrueProperties(Accrue<IProperty<?>> props) {
        props.accept(TYPE);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Algane.INSTANCE.getGuiHandler().openGui(player, state.getValue(TYPE).guiType, new WorldBlockPos(world, pos));
        return true;
    }

    private enum Type implements IStringSerializable {

        LASER(TileGunWorkbench::new, AlganeGuis.LASER_GUN_TABLE),
        MODIFIER(TileModifierWorkbench::new, null);

        private static final Type[] VALUES = values();

        public static Type getForMeta(int meta) {
            return VALUES[meta];
        }

        private final Supplier<? extends L9TileEntity> tileFactory;
        private final GuiIdentity<?, ?> guiType;

        Type(Supplier<? extends L9TileEntity> tileFactory, GuiIdentity<?, ?> guiType) {
            this.tileFactory = tileFactory;
            this.guiType = guiType;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        public L9TileEntity createTileEntity() {
            return tileFactory.get();
        }

        public ItemStack createStack(int count) {
            return new ItemStack(AlganeBlocks.WORKBENCH, count, ordinal());
        }

    }

}
