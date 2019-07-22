package xyz.phanta.algane.item;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import io.github.phantamanta44.libnine.util.nbt.ChainingTagCompound;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.init.AlganeItems;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.TooltipUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLaserModifier extends L9ItemSubs implements ParameterizedItemModel.IParamaterized {

    public ItemLaserModifier() {
        super(LangConst.ITEM_LASER_MOD, 2);
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        // NO-OP
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityBroker().with(AlganeCaps.LASER_GUN_MOD, new ModifierDelegator(stack));
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("dungeon", Boolean.toString(isDungeonMod(stack)));
    }

    public static ItemStack createStack(int count, LaserGunModifier mod, boolean dungeon) {
        return createStack(count, mod.getPowerMod(), mod.getEfficiencyMod(), mod.getHeatMod(), dungeon);
    }

    public static ItemStack createStack(int count, float modPower, float modEff, float modHeat, boolean dungeon) {
        ItemStack stack = new ItemStack(AlganeItems.LASER_MOD, count, dungeon ? 1 : 0);
        stack.setTagCompound(new ChainingTagCompound()
                .withFloat("LaserPower", modPower)
                .withFloat("LaserEfficiency", modEff)
                .withFloat("LaserHeat", modHeat));
        return stack;
    }

    public static boolean isDungeonMod(ItemStack stack) {
        return stack.getMetadata() == 1;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        LaserGunModifier mod = AlganeUtils.getItemLaserMod(stack);
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_MOD_POWER, mod.getPowerMod()));
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_MOD_EFF, mod.getEfficiencyMod()));
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_MOD_HEAT, mod.getHeatMod()));
        tooltip.add(TooltipUtils.formatDecimalTooltip(LangConst.TT_MOD_WEIGHT, mod.computeWeight() * 100F));
    }

    private static class ModifierDelegator implements LaserGunModifier {

        private final ItemStack stack;

        ModifierDelegator(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public float getPowerMod() {
            return getOrCreateTag().getFloat("LaserPower");
        }

        @Override
        public float getEfficiencyMod() {
            return getOrCreateTag().getFloat("LaserEfficiency");
        }

        @Override
        public float getHeatMod() {
            return getOrCreateTag().getFloat("LaserHeat");
        }

        private NBTTagCompound getOrCreateTag() {
            if (stack.hasTagCompound()) {
                //noinspection ConstantConditions
                return stack.getTagCompound();
            }
            NBTTagCompound tag = new ChainingTagCompound()
                    .withFloat("LaserPower", 0F)
                    .withFloat("LaserEfficiency", 0F)
                    .withFloat("LaserHeat", 0F);
            stack.setTagCompound(tag);
            return tag;
        }

    }

}
