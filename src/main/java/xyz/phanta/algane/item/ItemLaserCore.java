package xyz.phanta.algane.item;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.init.AlganeItems;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.core.LaserGunCore;
import xyz.phanta.algane.lasergun.core.LaserGunCoreAsmd;
import xyz.phanta.algane.lasergun.core.LaserGunCoreRepeater;
import xyz.phanta.algane.lasergun.core.LaserGunCoreSimple;

import javax.annotation.Nullable;

public class ItemLaserCore extends L9ItemSubs implements ParameterizedItemModel.IParamaterized {

    public ItemLaserCore() {
        super(LangConst.ITEM_LASER_CORE, Type.VALUES.length);
        setMaxStackSize(1);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityBroker().with(AlganeCaps.LASER_GUN_CORE, Type.getForMeta(stack.getMetadata()).core);
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("type", Type.getForStack(stack).name());
    }

    public enum Type {

        SIMPLE(new LaserGunCoreSimple()),
        REPEATER(new LaserGunCoreRepeater()),
        SHOCK(new LaserGunCoreAsmd()), // TODO finish
        ORB(new LaserGunCoreNoop()), // TODO finish
        GAUSS(new LaserGunCoreNoop()); // TODO finish
        // TODO maybe some kind of "dispersion" shotgun
        // TODO maybe some kind of heat gun

        private static class LaserGunCoreNoop implements LaserGunCore {

            @Override
            public FiringParadigm getFiringParadigm() {
                return FiringParadigm.SEMI_AUTO;
            }

            @Override
            public int fire(ItemStack stack, LaserGun gun, World world, Vec3d pos, Vec3d dir, int ticks,
                            @Nullable EntityLivingBase owner, @Nullable EnumHand hand) {
                return 8;
            }

            @Override
            public String getTranslationKey() {
                return "NOOP";
            }

        }

        public static final Type[] VALUES = values();

        public static Type getForMeta(int meta) {
            return VALUES[meta];
        }

        public static Type getForStack(ItemStack stack) {
            return getForMeta(stack.getMetadata());
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
