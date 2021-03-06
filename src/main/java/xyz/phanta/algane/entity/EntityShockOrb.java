package xyz.phanta.algane.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.ItemStack;
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
import xyz.phanta.algane.AlganeConfig;
import xyz.phanta.algane.init.AlganeSounds;
import xyz.phanta.algane.lasergun.damage.DamageBlast;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public class EntityShockOrb extends EntityFireball {

    private static final float EXPLODE_RADIUS = 3F;

    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityShockOrb.class, DataSerializers.FLOAT);

    private final ItemStack srcWeapon;

    public EntityShockOrb(World world, Vec3d pos, Vec3d accel, @Nullable EntityLivingBase owner, ItemStack srcWeapon) {
        super(world, pos.x, pos.y - 0.5D, pos.z, accel.x, accel.y, accel.z);
        if (owner != null) {
            this.shootingEntity = owner;
        }
        this.srcWeapon = srcWeapon;
    }

    public EntityShockOrb(World world) {
        super(world);
        this.srcWeapon = ItemStack.EMPTY;
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
            detonate(EXPLODE_RADIUS, damage, DamageBlast.shockOrb(this, shootingEntity, srcWeapon));
            world.playSound(null, posX, posY, posZ, AlganeSounds.GUN_ORB_DETONATE, SoundCategory.MASTER, 1F, 1F);
            Algane.PROXY.spawnParticleShockBlast(world, getPositionVector(), EXPLODE_RADIUS, Math.min(damage, 50F), 0xD400E7);
        }
    }

    public void detonate(float radius, float damage, DamageSource damageSrc) {
        float radiusSq = radius * radius;
        for (Entity hit : world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(
                posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius),
                e -> e != null && !e.isImmuneToExplosions())) {
            float dx = (float)(hit.posX - posX), dy = (float)(hit.posY - posY), dz = (float)(hit.posZ - posZ);
            float distSq = dx * dx + dy * dy + dz * dz;
            if (distSq <= radiusSq) {
                float capped = Math.max(distSq, 0.5F);
                if (hit.attackEntityFrom(damageSrc, damage / capped)) {
                    AlganeUtils.applyImpulse(
                            hit, (float)AlganeConfig.coreOrb.knockbackFactor / capped, new Vec3d(-dx, -dy, -dz));
                }
            }
        }
        setDead();
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

}
