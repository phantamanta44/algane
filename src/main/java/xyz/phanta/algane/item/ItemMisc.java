package xyz.phanta.algane.item;

import io.github.phantamanta44.libnine.item.L9ItemSubs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.init.AlganeItems;

public class ItemMisc extends L9ItemSubs {

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

    public enum Type {

        NO_MODIFIER(false);

        private static final Type[] VALUES = values();

        public static Type getByMeta(int meta) {
            return VALUES[meta];
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
