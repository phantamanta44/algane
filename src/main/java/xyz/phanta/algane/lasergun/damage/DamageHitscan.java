package xyz.phanta.algane.lasergun.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import xyz.phanta.algane.constant.LangConst;

import javax.annotation.Nullable;

public class DamageHitscan extends EntityDamageSource {

    private final ItemStack weapon;

    public static DamageHitscan laser(@Nullable EntityLivingBase owner, ItemStack weapon) {
        return new DamageHitscan(LangConst.DMG_SRC_LASER, owner, weapon);
    }

    public static DamageHitscan asmd(@Nullable EntityLivingBase owner, ItemStack weapon) {
        return new DamageHitscan(LangConst.DMG_SRC_ASMD, owner, weapon);
    }

    public static DamageHitscan gauss(@Nullable EntityLivingBase owner, ItemStack weapon) {
        return new DamageHitscan(LangConst.DMG_SRC_GAUSS, owner, weapon);
    }

    protected DamageHitscan(String name, @Nullable EntityLivingBase owner, ItemStack weapon) {
        super(name, owner);
        this.weapon = weapon;
        setProjectile();
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity) {
        if (damageSourceEntity == null) {
            return new TextComponentTranslation("death.attack." + damageType, entity.getDisplayName());
        } else if (weapon.isEmpty() || !weapon.hasDisplayName()) {
            return new TextComponentTranslation("death.attack." + damageType + ".player",
                    entity.getDisplayName(), damageSourceEntity.getDisplayName());
        } else {
            return new TextComponentTranslation("death.attack." + damageType + ".item",
                    entity.getDisplayName(), damageSourceEntity.getDisplayName(), weapon.getTextComponent());
        }
    }

}
