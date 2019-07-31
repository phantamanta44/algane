package xyz.phanta.algane.lasergun.item;

import io.github.phantamanta44.libnine.util.helper.OptUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import xyz.phanta.algane.lasergun.LaserGun;

import java.util.Objects;
import java.util.Optional;

public class LaserItemGunDelegator implements LaserGun {

    private final ItemStack stack;
    private final int modCount;

    public LaserItemGunDelegator(ItemStack stack, int modCount) {
        this.stack = stack;
        this.modCount = modCount;
    }

    @Override
    public ItemStack getCore() {
        return getCoreTag().map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    private Optional<NBTTagCompound> getCoreTag() {
        return OptUtils.stackTag(stack).map(tag -> tag.getCompoundTag("Core"));
    }

    @Override
    public void setCore(ItemStack stack) {
        getOrCreateTag().setTag("Core", stack.serializeNBT());
    }

    @Override
    public ItemStack getEnergyCell() {
        return OptUtils.stackTag(stack)
                .map(tag -> new ItemStack(tag.getCompoundTag("EnergyCell")))
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public void setEnergyCell(ItemStack stack) {
        getOrCreateTag().setTag("EnergyCell", stack.serializeNBT());
    }

    @Override
    public ItemStack getModifier(int slot) {
        return getModifiersTag().map(tag -> new ItemStack(tag.getCompoundTagAt(slot))).orElse(ItemStack.EMPTY);
    }

    private Optional<NBTTagList> getModifiersTag() {
        return OptUtils.stackTag(stack).map(tag -> tag.getTagList("Modifiers", Constants.NBT.TAG_COMPOUND));
    }

    @Override
    public void setModifier(int slot, ItemStack stack) {
        NBTTagCompound tag = getOrCreateTag();
        NBTTagList modDto;
        if (tag.hasKey("Modifiers")) {
            modDto = tag.getTagList("Modifiers", Constants.NBT.TAG_COMPOUND);
        } else {
            modDto = new NBTTagList();
            NBTTagCompound emptyTag = ItemStack.EMPTY.serializeNBT();
            for (int i = 0; i < modCount; i++) {
                modDto.appendTag(emptyTag);
            }
            tag.setTag("Modifiers", modDto);
        }
        modDto.set(slot, stack.serializeNBT());
    }

    @Override
    public int getModifierCount() {
        return modCount;
    }

    @Override
    public float getOverheat() {
        return OptUtils.stackTag(stack)
                .map(tag -> tag.getFloat("Heat"))
                .orElse(0F);
    }

    @Override
    public void setOverheat(float ticks) {
        getOrCreateTag().setFloat("Heat", ticks);
    }

    @Override
    public boolean isHeatLocked() {
        return OptUtils.stackTag(stack)
                .map(tag -> tag.getBoolean("HeatLock"))
                .orElse(false);
    }

    @Override
    public void setHeatLocked(boolean heatLocked) {
        getOrCreateTag().setBoolean("HeatLock", heatLocked);
    }

    @Override
    public boolean areBuildsEqual(LaserGun other) {
        if (!(other instanceof LaserItemGunDelegator)) {
            return false;
        }
        LaserItemGunDelegator otherGun = (LaserItemGunDelegator)other;
        return getCoreTag().equals(otherGun.getCoreTag()) && getModifiersTag().equals(otherGun.getModifiersTag());
    }

    private NBTTagCompound getOrCreateTag() {
        if (!stack.hasTagCompound()) {
            NBTTagCompound tag = new NBTTagCompound();
            stack.setTagCompound(tag);
            return tag;
        }
        return Objects.requireNonNull(stack.getTagCompound());
    }

}
