package xyz.phanta.algane.lasergun.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class DamageBlast extends DamageHitscan {

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
