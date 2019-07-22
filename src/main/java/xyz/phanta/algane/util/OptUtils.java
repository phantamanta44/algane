package xyz.phanta.algane.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Optional;

public class OptUtils {

    public static <T> Optional<T> getCapOpt(ICapabilityProvider provider, Capability<T> cap) {
        if (provider.hasCapability(cap, null)) {
            //noinspection ConstantConditions
            return Optional.of(provider.getCapability(cap, null));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<NBTTagCompound> getTagOpt(ItemStack stack) {
        //noinspection ConstantConditions
        return stack.hasTagCompound() ? Optional.of(stack.getTagCompound()) : Optional.empty();
    }

}
