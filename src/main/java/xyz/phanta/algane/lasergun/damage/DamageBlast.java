package xyz.phanta.algane.lasergun.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.entity.EntityShockOrb;

import javax.annotation.Nullable;

public class DamageBlast extends DamageHitscan {

    public static DamageBlast shockOrb(EntityShockOrb orb, @Nullable EntityLivingBase owner) {
        return new DamageBlast(LangConst.DMG_SRC_ORB, orb.getPositionVector(), owner);
    }

    public static DamageBlast shockCombo(EntityShockOrb orb, @Nullable EntityLivingBase owner) {
        return new DamageBlast(LangConst.DMG_SRC_COMBO, orb.getPositionVector(), owner);
    }

    private final Vec3d origin;

    private DamageBlast(String name, Vec3d origin, @Nullable EntityLivingBase owner) {
        super(name, owner);
        this.origin = origin;
        setExplosion();
    }

    @Nullable
    @Override
    public Vec3d getDamageLocation() {
        return origin;
    }

}
