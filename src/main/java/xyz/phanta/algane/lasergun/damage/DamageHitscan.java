package xyz.phanta.algane.lasergun.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;

import javax.annotation.Nullable;

public class DamageHitscan extends EntityDamageSource {

    protected DamageHitscan(String name, @Nullable EntityLivingBase owner) {
        super(name, owner);
        setProjectile();
    }

}
