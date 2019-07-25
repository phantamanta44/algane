package xyz.phanta.algane.lasergun.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;
import xyz.phanta.algane.constant.LangConst;

import javax.annotation.Nullable;

public class DamageHitscan extends EntityDamageSource {

    public static DamageHitscan laser(@Nullable EntityLivingBase owner) {
        return new DamageHitscan(LangConst.DMG_SRC_LASER, owner);
    }

    protected DamageHitscan(String name, @Nullable EntityLivingBase owner) {
        super(name, owner);
        setProjectile();
    }

}
