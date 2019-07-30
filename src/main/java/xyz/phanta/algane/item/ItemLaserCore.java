package xyz.phanta.algane.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.init.AlganeItems;
import xyz.phanta.algane.lasergun.core.*;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLaserCore extends L9ItemSubs implements ParameterizedItemModel.IParamaterized {

    private static final String[] TT_STRINGS = new String[] {
            TextFormatting.DARK_GRAY + "\u25a0\u25a0\u25a0",
            TextFormatting.RED + "\u25a0" + TextFormatting.DARK_GRAY + "\u25a0\u25a0",
            TextFormatting.YELLOW + "\u25a0\u25a0" + TextFormatting.DARK_GRAY + "\u25a0",
            TextFormatting.GREEN + "\u25a0\u25a0\u25a0"
    };

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

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        Type type = Type.getForStack(stack);
        tooltip.add(formatRating(LangConst.TT_RATE_DAMAGE, type.ratingDamage));
        tooltip.add(formatRating(LangConst.TT_RATE_FIRERATE, type.ratingFirerate));
        tooltip.add(formatRating(LangConst.TT_RATE_RANGE, type.ratingRange));
        switch (type) {
            case SHOCK:
                tooltip.add(formatSpecial(LangConst.TT_KNOCKBACK));
                break;
            case ORB:
                tooltip.add(formatSpecial(LangConst.TT_SPLASH));
                break;
        }
    }

    private static String formatRating(String label, int rating) {
        return TT_STRINGS[rating] + " " + ChatFormatting.GRAY + I18n.format(label);
    }

    private static String formatSpecial(String label) {
        return TextFormatting.BLUE + I18n.format(label);
    }

    public enum Type {

        SIMPLE(2, 2, 2, new LaserGunCoreSimple()),
        REPEATER(1, 3, 1, new LaserGunCoreRepeater()),
        SHOCK(2, 1, 3, new LaserGunCoreAsmd()),
        ORB(2, 0, 3, new LaserGunCoreOrb()),
        GAUSS(3, 0, 3, new LaserGunCoreGauss());
        // TODO maybe some kind of "dispersion" shotgun
        // TODO maybe some kind of heat gun

        public static final Type[] VALUES = values();

        public static Type getForMeta(int meta) {
            return VALUES[meta];
        }

        public static Type getForStack(ItemStack stack) {
            return getForMeta(stack.getMetadata());
        }

        private final int ratingDamage, ratingFirerate, ratingRange;
        private final LaserGunCore core;

        Type(int ratingDamage, int ratingFirerate, int ratingRange, LaserGunCore core) {
            this.ratingDamage = ratingDamage;
            this.ratingFirerate = ratingFirerate;
            this.ratingRange = ratingRange;
            this.core = core;
        }

        public ItemStack createStack() {
            return new ItemStack(AlganeItems.LASER_CORE, 1, ordinal());
        }

    }

}
