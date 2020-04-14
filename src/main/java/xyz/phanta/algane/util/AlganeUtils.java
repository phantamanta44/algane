package xyz.phanta.algane.util;

import io.github.phantamanta44.libnine.util.helper.OptUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class AlganeUtils {

    public static LaserGun getItemLaserGun(ItemStack stack) {
        return Objects.requireNonNull(stack.getCapability(AlganeCaps.LASER_GUN, null));
    }

    public static Optional<LaserGunCore> getLaserCore(LaserGun gun) {
        return OptUtils.capability(gun.getCore(), AlganeCaps.LASER_GUN_CORE);
    }

    public static Optional<IEnergyStorage> getLaserEnergy(LaserGun gun) {
        return OptUtils.capability(gun.getEnergyCell(), CapabilityEnergy.ENERGY);
    }

    public static LaserGunModifier computeTotalMods(LaserGun gun) {
        int modCount = gun.getModifierCount();
        ModifierAccumulator acc = new ModifierAccumulator();
        for (int i = 0; i < modCount; i++) {
            ItemStack modStack = gun.getModifier(i);
            if (modStack.hasCapability(AlganeCaps.LASER_GUN_MOD, null)) {
                //noinspection ConstantConditions
                acc.accumulate(modStack.getCapability(AlganeCaps.LASER_GUN_MOD, null));
            }
        }
        return acc;
    }

    public static LaserGunModifier computeJoinedMods(LaserGunModifier a, LaserGunModifier b) {
        float avgW = (a.computeWeight() + b.computeWeight()) / 2F;
        float x = a.getPowerMod() + b.getPowerMod();
        float y = a.getEfficiencyMod() + b.getEfficiencyMod();
        float z = a.getHeatMod() + b.getHeatMod();
        float mult = avgW / (float)Math.sqrt(x * x + y * y + z * z);
        return new ModifierAccumulator(x * mult, y * mult, z * mult);
    }

    public static float getDamageMultiplier(LaserGunModifier mod) {
        return 1F + mod.getPowerMod();
    }

    public static float computeDamage(float base, LaserGunModifier mod) {
        return base * getDamageMultiplier(mod);
    }

    public static float getEnergyMultiplier(LaserGunModifier mod) {
        return (1F + 1.75F * mod.getPowerMod()) / (1F + mod.getEfficiencyMod());
    }

    public static int computeEnergy(int base, LaserGunModifier mod) {
        return (int)Math.ceil(base * getEnergyMultiplier(mod));
    }

    public static float getHeatMultiplier(LaserGunModifier mod) {
        return 1F / (1F + mod.getHeatMod());
    }

    public static float computeHeat(float base, LaserGunModifier mod) {
        return base * getHeatMultiplier(mod);
    }

    public static void incrementHeat(LaserGun gun, float amount) {
        float heat = gun.getOverheat() + amount;
        if (heat >= 100F) {
            gun.setOverheat(100F);
            gun.setHeatLocked(true);
        } else {
            gun.setOverheat(heat);
        }
    }

    public static void coolDown(LaserGun gun) {
        float heat = gun.getOverheat();
        if (heat > 0F) {
            heat -= 1F;
            if (heat <= 0F) {
                gun.setOverheat(0F);
                gun.setHeatLocked(false);
            } else {
                gun.setOverheat(heat);
            }
        }
    }

    public static float consumeEnergy(LaserGun gun, int baseEnergy, LaserGunModifier totalMods) {
        int energyCost = AlganeUtils.computeEnergy(baseEnergy, totalMods);
        //noinspection OptionalGetWithoutIsPresent
        IEnergyStorage energy = AlganeUtils.getLaserEnergy(gun).get();
        return energy.extractEnergy(energyCost, false) / (float)energyCost;
    }

    public static LaserGunModifier getItemLaserMod(ItemStack stack) {
        return Objects.requireNonNull(stack.getCapability(AlganeCaps.LASER_GUN_MOD, null));
    }

    public static EnumHandSide getHandSide(EnumHand hand) {
        return hand == EnumHand.OFF_HAND
                ? Minecraft.getMinecraft().player.getPrimaryHand().opposite()
                : Minecraft.getMinecraft().player.getPrimaryHand();
    }

    public static void applyImpulse(@Nullable Entity entity, float magnitude, Vec3d dir) {
        if (entity != null) {
            entity.motionX += -magnitude * dir.x;
            entity.motionY += -magnitude * dir.y;
            entity.motionZ += -magnitude * dir.z;
            entity.isAirBorne = entity.velocityChanged = true;
        }
    }

    public static boolean canLaseEntity(Entity entity) {
        return entity.canBeCollidedWith() && entity.canBeAttackedWithItem();
    }

    public static Predicate<Entity> getLasingFilter(@Nullable Entity shooter) {
        return shooter == null ? AlganeUtils::canLaseEntity : e -> e != shooter && canLaseEntity(e);
    }

}
