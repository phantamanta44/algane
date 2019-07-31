package xyz.phanta.algane.item;

import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import io.github.phantamanta44.libnine.util.math.MathUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeItems;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMisc extends L9ItemSubs implements ParameterizedItemModel.IParamaterized {

    private static final IRarity RARITY_LOCKED = new IRarity() {
        @Override
        public TextFormatting getColor() {
            return TextFormatting.RED;
        }

        @Override
        public String getName() {
            return "Locked";
        }
    };

    public ItemMisc() {
        super(LangConst.ITEM_MISC, Type.VALUES.length);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (Type type : Type.VALUES) {
                if (type.visible) {
                    items.add(type.createStack(1));
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        switch (Type.getForStack(stack)) {
            case MODIFIER_KIT:
                generateModifier(player, 0.1F + 0.24F * (float)Math.random(), false);
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            case DUNGEON_MODIFIER_KIT:
                generateModifier(player, 0.25F + 0.25F * (float)Math.random(), true);
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    private static void generateModifier(EntityPlayer player, float weight, boolean dungeon) {
        float theta = (float)Math.random() * MathUtils.PI_F / 2F, phi = (float)Math.random() * MathUtils.PI_F / 2F;
        float xz = (float)Math.cos(phi);
        player.dropItem(ItemLaserModifier.createStack(1,
                (float)Math.cos(theta) * xz * weight, (float)Math.sin(phi) * weight, (float)Math.sin(theta) * xz * weight,
                dungeon), false, true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        switch (Type.getForStack(stack)) {
            case MODIFIER_KIT:
            case DUNGEON_MODIFIER_KIT:
                tooltip.add(TextFormatting.GRAY + I18n.format(LangConst.TT_MODKIT));
                break;
        }
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        if (Type.getForStack(stack) == Type.NO_MODIFIER) {
            return RARITY_LOCKED;
        }
        return super.getForgeRarity(stack);
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("type", Type.getForStack(stack).name());
    }

    public enum Type {

        NO_MODIFIER(false),
        MODIFIER_KIT,
        DUNGEON_MODIFIER_KIT,
        OPTICAL_COMPONENT_CORE,
        MODIFIER_COMPONENT,
        TAU_CONDENSER_EMPTY,
        TAU_CONDENSER;

        private static final Type[] VALUES = values();

        public static Type getByMeta(int meta) {
            return VALUES[meta];
        }

        public static Type getForStack(ItemStack stack) {
            return getByMeta(stack.getMetadata());
        }

        private final boolean visible;

        Type(boolean visible) {
            this.visible = visible;
        }

        Type() {
            this(true);
        }

        public ItemStack createStack(int count) {
            return new ItemStack(AlganeItems.MISC, count, ordinal());
        }

    }

}
