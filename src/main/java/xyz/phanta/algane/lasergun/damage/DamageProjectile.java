package xyz.phanta.algane.lasergun.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSourceIndirect;

import javax.annotation.Nullable;

public class DamageProjectile extends EntityDamageSourceIndirect {

    private DamageProjectile(String name, Entity source, @Nullable EntityLivingBase owner) {
        super(name, source, owner);
        setProjectile();
    }

}
