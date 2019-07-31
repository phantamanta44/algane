package xyz.phanta.algane.tile;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.tile.L9TileEntityTicking;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.ISerializable;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.data.serialization.IDatum;
import io.github.phantamanta44.libnine.util.helper.ItemUtils;
import io.github.phantamanta44.libnine.util.helper.OptUtils;
import io.github.phantamanta44.libnine.util.math.LinAlUtils;
import io.github.phantamanta44.libnine.util.world.WorldUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.block.BlockLaserTurret;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;
import xyz.phanta.algane.lasergun.turret.*;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;

public class TileLaserTurret extends L9TileEntityTicking {

    private static final EnumMap<LaserGunCore.FiringParadigm, TurretFiringStrategy> FIRING_STRATEGIES;

    static {
        FIRING_STRATEGIES = new EnumMap<>(LaserGunCore.FiringParadigm.class);
        FIRING_STRATEGIES.put(LaserGunCore.FiringParadigm.SEMI_AUTO, new SemiAutoFiringStrategy());
        FIRING_STRATEGIES.put(LaserGunCore.FiringParadigm.AUTO, new AutoFiringStrategy());
        FIRING_STRATEGIES.put(LaserGunCore.FiringParadigm.CHARGE, new ChargeFiringStrategy());
    }

    @AutoSerialize
    private final LaserTurretGun gun;
    @AutoSerialize
    private final IDatum.OfInt firingTicks = IDatum.ofInt(0);
    @AutoSerialize
    private final IDatum.OfInt cooldown = IDatum.ofInt(0);
    @AutoSerialize
    private final IDatum<EnumFacing> facing = IDatum.of(EnumFacing.NORTH);

    @Nullable
    private TurretFiringStrategy firingStrategy = null;

    private TileLaserTurret(int modCount) {
        this.gun = new LaserTurretGun(this, modCount);
        markRequiresSync();
        setInitialized();
    }

    @Override
    protected ICapabilityProvider initCapabilities() {
        return new CapabilityBroker()
                .with(AlganeCaps.LASER_GUN, gun)
                .with(CapabilityEnergy.ENERGY, gun);
    }

    @Override
    protected void tick() {
        if (!world.isRemote) {
            boolean offCooldown = cooldown.get() == 0;
            int ticks = firingTicks.get();
            boolean dirty = false;

            if (!gun.isHeatLocked() && getFiringStrategy().tick(this, gun, world.isBlockPowered(pos), offCooldown, ticks)) {
                firingTicks.postincrement();
                dirty = true;
            } else if (ticks != 0) {
                firingTicks.set(0);
                dirty = true;
            }

            gun.tickHeat();
            if (cooldown.get() > 0) {
                cooldown.postincrement(-1);
                dirty = true;
            }
            if (dirty) {
                setDirty();
            }
        }
    }

    public Vec3d getFiringPos(Vec3d dir) {
        return WorldUtils.getBlockCenter(pos).add(dir.scale(0.5025D));
    }

    public Vec3d getFiringDir() {
        return LinAlUtils.getDir(getRotation());
    }

    private TurretFiringStrategy getFiringStrategy() {
        if (firingStrategy == null) {
            firingStrategy = AlganeUtils.getLaserCore(gun)
                    .map(core -> FIRING_STRATEGIES.get(core.getFiringParadigm())).orElse(NoopFiringStrategy.INSTANCE);
        }
        return firingStrategy;
    }

    private void invalidateFiringStrategy() {
        firingStrategy = null;
    }

    public LaserGun getGun() {
        return gun;
    }

    public int getCooldown() {
        return cooldown.get();
    }

    public void setCooldown(int ticks) {
        cooldown.set(ticks);
        setDirty();
    }

    public EnumFacing getRotation() {
        return facing.get();
    }

    public void setRotation(EnumFacing dir) {
        facing.set(dir);
        setDirty();
    }

    public NBTTagCompound serializeItemNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        gun.serNBT(tag);
        return tag;
    }

    public void deserializeItemNbt(@Nullable NBTTagCompound tag) {
        if (tag != null) {
            gun.deserNBT(tag);
            invalidateFiringStrategy();
            setDirty();
        }
    }

    private static class LaserTurretGun implements LaserGun, IEnergyStorage, ISerializable {

        private TileLaserTurret tile;
        private ItemStack core = ItemStack.EMPTY;
        private ItemStack energy = ItemStack.EMPTY;
        private ItemStack[] mods;
        private float heat;
        private boolean heatLocked;

        LaserTurretGun(TileLaserTurret tile, int modCount) {
            this.tile = tile;
            this.mods = new ItemStack[modCount];
            Arrays.fill(mods, ItemStack.EMPTY);
        }

        @Override
        public ItemStack getCore() {
            return core;
        }

        @Override
        public void setCore(ItemStack stack) {
            this.core = stack;
            tile.invalidateFiringStrategy();
            tile.setDirty();
        }

        @Override
        public ItemStack getEnergyCell() {
            return energy;
        }

        @Override
        public void setEnergyCell(ItemStack stack) {
            this.energy = stack;
            tile.setDirty();
        }

        @Override
        public ItemStack getModifier(int slot) {
            return mods[slot];
        }

        @Override
        public void setModifier(int slot, ItemStack stack) {
            this.mods[slot] = stack;
            tile.setDirty();
        }

        @Override
        public int getModifierCount() {
            return mods.length;
        }

        @Override
        public float getOverheat() {
            return heat;
        }

        @Override
        public void setOverheat(float ticks) {
            this.heat = ticks;
            tile.setDirty();
        }

        void tickHeat() {
            if (heat > 0F) {
                if (heat <= 1F) {
                    heat = 0F;
                    heatLocked = false;
                } else {
                    --heat;
                }
                tile.setDirty();
            }
        }

        @Override
        public boolean isHeatLocked() {
            return heatLocked;
        }

        @Override
        public void setHeatLocked(boolean heatLocked) {
            this.heatLocked = heatLocked;
            tile.setDirty();
        }

        @Override
        public boolean areBuildsEqual(LaserGun other) {
            if (!(other instanceof LaserTurretGun)) {
                return false;
            }
            LaserTurretGun otherTurret = (LaserTurretGun)other;
            if (!ItemUtils.matchesWithWildcard(core, otherTurret.core) || mods.length != otherTurret.mods.length) {
                return false;
            }
            for (int i = 0; i < mods.length; i++) {
                if (!ItemUtils.matchesWithWildcard(mods[i], otherTurret.mods[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int result = OptUtils.capability(energy, CapabilityEnergy.ENERGY)
                    .map(e -> e.receiveEnergy(maxReceive, simulate)).orElse(0);
            if (result != 0 && !simulate) {
                tile.setDirty();
            }
            return result;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int result = OptUtils.capability(energy, CapabilityEnergy.ENERGY)
                    .map(e -> e.extractEnergy(maxExtract, simulate)).orElse(0);
            if (result != 0 && !simulate) {
                tile.setDirty();
            }
            return result;
        }

        @Override
        public int getEnergyStored() {
            return OptUtils.capability(energy, CapabilityEnergy.ENERGY)
                    .map(IEnergyStorage::getEnergyStored).orElse(0);
        }

        @Override
        public int getMaxEnergyStored() {
            return OptUtils.capability(energy, CapabilityEnergy.ENERGY)
                    .map(IEnergyStorage::getMaxEnergyStored).orElse(0);
        }

        @Override
        public boolean canExtract() {
            return OptUtils.capability(energy, CapabilityEnergy.ENERGY)
                    .map(IEnergyStorage::canExtract).orElse(false);
        }

        @Override
        public boolean canReceive() {
            return OptUtils.capability(energy, CapabilityEnergy.ENERGY)
                    .map(IEnergyStorage::canReceive).orElse(false);
        }

        @Override
        public void serBytes(ByteUtils.Writer data) {
            data.writeItemStack(core).writeItemStack(energy);
            for (ItemStack mod : mods) {
                data.writeItemStack(mod);
            }
            data.writeFloat(heat).writeBool(heatLocked);
        }

        @Override
        public void deserBytes(ByteUtils.Reader data) {
            core = data.readItemStack();
            energy = data.readItemStack();
            for (int i = 0; i < mods.length; i++) {
                mods[i] = data.readItemStack();
            }
            heat = data.readFloat();
            heatLocked = data.readBool();
        }

        @Override
        public void serNBT(NBTTagCompound tag) {
            tag.setTag("Core", core.serializeNBT());
            tag.setTag("EnergyCell", energy.serializeNBT());
            NBTTagList modsDto = new NBTTagList();
            for (ItemStack mod : mods) {
                modsDto.appendTag(mod.serializeNBT());
            }
            tag.setTag("Modifiers", modsDto);
            tag.setFloat("Heat", heat);
            tag.setBoolean("HeatLock", heatLocked);
        }

        @Override
        public void deserNBT(NBTTagCompound tag) {
            core = new ItemStack(tag.getCompoundTag("Core"));
            energy = new ItemStack(tag.getCompoundTag("EnergyCell"));
            NBTTagList modsDto = tag.getTagList("Modifiers", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < mods.length; i++) {
                mods[i] = new ItemStack(modsDto.getCompoundTagAt(i));
            }
            heat = tag.getFloat("Heat");
            heatLocked = tag.getBoolean("HeatLock");
        }

    }

    @RegisterTile(Algane.MOD_ID)
    public static class Basic extends TileLaserTurret {

        public Basic() {
            super(BlockLaserTurret.Tier.BASIC.modifierCount);
        }

    }

    @RegisterTile(Algane.MOD_ID)
    public static class Advanced extends TileLaserTurret {

        public Advanced() {
            super(BlockLaserTurret.Tier.ADVANCED.modifierCount);
        }

    }

    @RegisterTile(Algane.MOD_ID)
    public static class Elite extends TileLaserTurret {

        public Elite() {
            super(BlockLaserTurret.Tier.ELITE.modifierCount);
        }

    }

    @RegisterTile(Algane.MOD_ID)
    public static class Ultimate extends TileLaserTurret {

        public Ultimate() {
            super(BlockLaserTurret.Tier.ULTIMATE.modifierCount);
        }

    }

}
