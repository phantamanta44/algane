package xyz.phanta.algane.item;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.init.AlganeItems;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.core.LaserGunCore;
import xyz.phanta.algane.lasergun.core.LaserGunCoreSimple;

import javax.annotation.Nullable;

public class ItemLaserCore extends L9ItemSubs {

    public ItemLaserCore() {
        super(LangConst.ITEM_LASER_CORE, Type.VALUES.length);
        setMaxStackSize(1);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityBroker().with(AlganeCaps.LASER_GUN_CORE, Type.getForMeta(stack.getMetadata()).core);
    }

    public enum Type { // TODO modifiers

        SIMPLE(new LaserGunCoreSimple()),
        REPEATER(new LaserGunCoreNoop()), // TODO finish
        BEAM(new LaserGunCoreNoop()), // TODO finish
        SHOCK(new LaserGunCoreNoop()), // TODO finish
        ORB(new LaserGunCoreNoop()), // TODO finish
        GAUSS(new LaserGunCoreNoop()); // TODO finish

        private static class LaserGunCoreNoop implements LaserGunCore {

            @Override
            public FiringParadigm getFiringParadigm() {
                return FiringParadigm.SEMI_AUTO;
            }

            @Override
            public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir) {
                return 8;
            }

        }

        private static final Type[] VALUES = values();

        public static Type getForMeta(int meta) {
            return VALUES[meta];
        }

        private final LaserGunCore core;

        Type(LaserGunCore core) {
            this.core = core;
        }

        public ItemStack createStack() {
            return new ItemStack(AlganeItems.LASER_CORE, 1, ordinal());
        }

    }

}
