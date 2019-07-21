package xyz.phanta.algane.lasergun;

import io.github.phantamanta44.libnine.util.ImpossibilityRealizedException;
import net.minecraft.item.ItemStack;

public interface LaserGun {

    ItemStack getCore();

    void setCore(ItemStack stack);

    ItemStack getEnergyCell();

    void setEnergyCell(ItemStack stack);

    ItemStack getModifier(int slot);

    void setModifier(int slot, ItemStack stack);

    int getModifierCount();

    int getFiringDuration();

    void setFiringDuration(int ticks);

    default void incrementFiringDuration() {
        setFiringDuration(getFiringDuration() + 1);
    }

    default boolean canAttachPart(LaserGunPart part) {
        switch (part) {
            case CORE:
                return getCore().isEmpty();
            case ENERGY_CELL:
                return getEnergyCell().isEmpty();
            case MODIFIER:
                for (int i = 0; i < getModifierCount(); i++) {
                    if (getModifier(i).isEmpty()) {
                        return true;
                    }
                }
                return false;
        }
        throw new ImpossibilityRealizedException();
    }

    default void attachPart(LaserGunPart part, ItemStack stack) {
        switch (part) {
            case CORE:
                setCore(stack);
                break;
            case ENERGY_CELL:
                setEnergyCell(stack);
                break;
            case MODIFIER:
                for (int i = 0; i < getModifierCount(); i++) {
                    if (getModifier(i).isEmpty()) {
                        setModifier(i, stack);
                    }
                }
                break;
        }
    }

    class Impl implements LaserGun {

        @Override
        public ItemStack getCore() {
            return ItemStack.EMPTY;
        }

        @Override
        public void setCore(ItemStack stack) {
            // NO-OP
        }

        @Override
        public ItemStack getEnergyCell() {
            return ItemStack.EMPTY;
        }

        @Override
        public void setEnergyCell(ItemStack stack) {
            // NO-OP
        }

        @Override
        public ItemStack getModifier(int slot) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void setModifier(int slot, ItemStack stack) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int getModifierCount() {
            return 0;
        }

        @Override
        public int getFiringDuration() {
            return 0;
        }

        @Override
        public void setFiringDuration(int ticks) {
            // NO-OP
        }

    }

}
