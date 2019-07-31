package xyz.phanta.algane.block;

import io.github.phantamanta44.libnine.block.L9BlockStated;
import io.github.phantamanta44.libnine.item.L9ItemBlock;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeBlocks;
import xyz.phanta.algane.init.AlganeGuis;
import xyz.phanta.algane.item.block.ItemBlockLaserTurret;
import xyz.phanta.algane.tile.TileLaserTurret;

import java.util.function.Supplier;

public class BlockLaserTurret extends L9BlockStated {

    public static final IProperty<Tier> TIER = PropertyEnum.create("tier", Tier.class);
    public static final IProperty<EnumFacing> ROTATION = PropertyEnum.create("rotation", EnumFacing.class);

    public BlockLaserTurret() {
        super(LangConst.BLOCK_LASER_TURRET, Material.IRON);
        setHardness(4F);
        setResistance(8F);
        setTileFactory((w, m) -> Tier.getForMeta(m).createTile());
    }

    @Override
    protected L9ItemBlock initItemBlock() {
        return new ItemBlockLaserTurret(this);
    }

    @Override
    protected void accrueProperties(Accrue<IProperty<?>> props) {
        props.accept(TIER);
    }

    @Override
    protected void accrueVolatileProperties(Accrue<IProperty<?>> props) {
        props.accept(ROTATION);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileLaserTurret tile = getTileEntity(world, pos);
        return tile == null ? state : state.withProperty(ROTATION, tile.getRotation());
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            Algane.INSTANCE.getGuiHandler().openGui(player, AlganeGuis.LASER_TURRET, new WorldBlockPos(world, pos));
        }
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(getRepStack(state, world, pos));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return getRepStack(state, world, pos);
    }

    private ItemStack getRepStack(IBlockState state, IBlockAccess world, BlockPos pos) {
        ItemStack stack = state.getValue(TIER).createStack();
        TileLaserTurret tile = getTileEntity(world, pos);
        if (tile != null) {
            stack.setTagCompound(tile.serializeItemNbt());
        }
        return stack;
    }

    public enum Tier implements IStringSerializable {

        BASIC(1, TileLaserTurret.Basic::new),
        ADVANCED(2, TileLaserTurret.Advanced::new),
        ELITE(3, TileLaserTurret.Elite::new),
        ULTIMATE(4, TileLaserTurret.Ultimate::new);

        public static final Tier[] VALUES = values();

        public static Tier getForMeta(int meta) {
            return VALUES[meta];
        }

        public static Tier getForStack(ItemStack stack) {
            return getForMeta(stack.getMetadata());
        }

        public final int modifierCount;

        private final Supplier<TileLaserTurret> tileFactory;

        Tier(int modifierCount, Supplier<TileLaserTurret> tileFactory) {
            this.modifierCount = modifierCount;
            this.tileFactory = tileFactory;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        TileLaserTurret createTile() {
            return tileFactory.get();
        }

        public ItemStack createStack() {
            return new ItemStack(AlganeBlocks.TURRET, 1, ordinal());
        }

    }

}
