package xyz.phanta.algane.lasergun.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSourceIndirect;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.entity.EntityLaserBolt;

import javax.annotation.Nullable;

public class DamageProjectile extends EntityDamageSourceIndirect {

    public static DamageProjectile bolt(EntityLaserBolt bolt) {
        return new DamageProjectile(LangConst.DMG_SRC_BOLT, bolt, bolt.getOwner());
    }

    private DamageProjectile(String name, Entity source, @Nullable EntityLivingBase owner) {
        super(name, source, owner);
        setProjectile();
    }

}
