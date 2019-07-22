package xyz.phanta.algane.entity;

import io.github.phantamanta44.libnine.util.LazyConstant;
import io.github.phantamanta44.libnine.util.tuple.IPair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.lasergun.damage.DamageProjectile;
import xyz.phanta.algane.util.LasingUtils;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class EntityLaserBolt extends Entity {

    private static final DataParameter<Integer> COLOUR = EntityDataManager.createKey(EntityLaserBolt.class, DataSerializers.VARINT);

    private float damage;
    private float speed;
    private float range;
    @Nullable
    private LazyConstant<EntityLivingBase> ownerSupplier;

    public EntityLaserBolt(World world, Vec3d pos, Vec3d vel, float damage, float range, @Nullable EntityLivingBase owner) {
        super(world);
        this.posX = pos.x;
        this.posY = pos.y;
        this.posZ = pos.z;
        this.motionX = vel.x;
        this.motionY = vel.y;
        this.motionZ = vel.z;
        this.damage = damage;
        this.speed = (float)vel.length();
        this.range = range;
        this.ownerSupplier = new LazyConstant<>(() -> owner);
    }

    @Deprecated
    public EntityLaserBolt(World world) {
        super(world);
    }

    public void init(int colour) {
        dataManager.set(COLOUR, colour);
    }

    @Nullable
    public EntityLivingBase getOwner() {
        return ownerSupplier != null ? ownerSupplier.get() : null;
    }

    @Override
    protected void entityInit() {
        dataManager.register(COLOUR, 0);
    }

    @Override
    public void onUpdate() {
        Vec3d oldPos = getPositionVector();
        lastTickPosX = oldPos.x;
        lastTickPosY = oldPos.y;
        lastTickPosZ = oldPos.z;
        if (speed > 0) {
            Vec3d newPos = oldPos.add(motionX, motionY, motionZ);
            RayTraceResult trace = LasingUtils.traceLaser(world, oldPos, newPos);
            if (trace != null) {
                newPos = trace.hitVec;
                setDead();
            }
            EntityLivingBase owner = getOwner();
            Optional<IPair<EntityLivingBase, Double>> hitOpt = LasingUtils.getEntitiesOnLine(world, oldPos, newPos)
                    .filter(e -> e != owner)
                    .map(e -> IPair.of(e, e.getDistanceSq(oldPos.x, oldPos.y, oldPos.z)))
                    .min(Comparator.comparing(IPair::getB));
            if (hitOpt.isPresent()) {
                IPair<EntityLivingBase, Double> hit = hitOpt.get();
                double factor = hit.getB() / speed;
                newPos = oldPos.add(motionX * factor, motionY * factor, motionZ * factor);
                if (!world.isRemote) {
                    hit.getA().attackEntityFrom(DamageProjectile.bolt(this), damage);
                }
                setDead();
            } else {
                range -= speed;
                if (range <= 0) {
                    setDead();
                }
            }
            posX = newPos.x;
            posY = newPos.y;
            posZ = newPos.z;
        }
        Algane.PROXY.spawnParticleBolt(world, oldPos, dataManager.get(COLOUR));
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        damage = tag.getFloat("BoltDamage");
        speed = tag.getFloat("BoltSpeed");
        range = tag.getFloat("BoltRange");
        dataManager.set(COLOUR, tag.getInteger("BoltColour"));
        if (tag.hasKey("Owner")) {
            UUID ownerId = UUID.fromString(tag.getString("Owner"));
            ownerSupplier = new LazyConstant<>(() -> world.getPlayerEntityByUUID(ownerId));
        } else {
            ownerSupplier = null;
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setFloat("BoltDamage", damage);
        tag.setFloat("BoltSpeed", speed);
        tag.setFloat("BoltRange", range);
        tag.setInteger("BoltColour", dataManager.get(COLOUR));
        EntityLivingBase owner = getOwner();
        if (owner != null) {
            tag.setString("Owner", owner.getUniqueID().toString());
        }
    }

}
