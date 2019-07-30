package xyz.phanta.algane.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.init.AlganeSounds;
import xyz.phanta.algane.lasergun.damage.DamageBlast;

import javax.annotation.Nullable;

public class EntityShockOrb extends EntityFireball {

    private static final float EXPLODE_RADIUS = 3F;
    private static final float MAX_KNOCKBACK = 3F;

    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityShockOrb.class, DataSerializers.FLOAT);

    public EntityShockOrb(World world, Vec3d pos, Vec3d accel, @Nullable EntityLivingBase owner) {
        super(world, pos.x, pos.y - 0.5D, pos.z, accel.x, accel.y, accel.z);
        if (owner != null) {
            this.shootingEntity = owner;
        }
    }

    public EntityShockOrb(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        dataManager.register(DAMAGE, 0F);
    }

    public EntityShockOrb init(float damage) {
        setDamage(damage);
        return this;
    }

    public float getDamage() {
        return dataManager.get(DAMAGE);
    }

    public void setDamage(float damage) {
        dataManager.set(DAMAGE, damage);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!world.isRemote) {
            float damage = getDamage();
            detonate(EXPLODE_RADIUS, damage, DamageBlast.shockOrb(this, shootingEntity));
            world.playSound(null, posX, posY, posZ, AlganeSounds.GUN_ORB_DETONATE, SoundCategory.MASTER, 1F, 1F);
            Algane.PROXY.spawnParticleShockBlast(world, getPositionVector(), EXPLODE_RADIUS, Math.min(damage, 50F), 0xD400E7);
        }
    }

    public void detonate(float radius, float damage, DamageSource damageSrc) {
        float radiusSq = radius * radius;
        for (EntityLivingBase hit : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(
                posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius))) {
            float dx = (float)(hit.posX - posX), dz = (float)(hit.posZ - posZ);
            float distSq = dx * dx + dz * dz;
            if (distSq <= radiusSq) {
                float capped = Math.max(distSq, 0.5F);
                hit.knockBack(hit, MAX_KNOCKBACK / capped, -dx, -dz);
                hit.attackEntityFrom(damageSrc, damage / capped);
            }
        }
        setDead();
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

}
