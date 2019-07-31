package xyz.phanta.algane.constant;

import net.minecraft.util.ResourceLocation;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.block.BlockLaserTurret;
import xyz.phanta.algane.item.ItemLaserCore;
import xyz.phanta.algane.item.ItemLaserGun;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;

import java.util.Arrays;

public class LangConst {

    public static final String ITEM_LASER_GUN = "laser_gun";
    public static final String ITEM_LASER_CORE = "laser_core";
    public static final String ITEM_LASER_MOD = "laser_mod";
    public static final String ITEM_MISC = "misc";

    public static final String BLOCK_LASER_GUN_TABLE = "laser_workbench";
    public static final String BLOCK_LASER_TURRET = "laser_turret";

    public static final String INV_LASER_GUN_TABLE = "laser_workbench";
    public static final String INV_MODIFIER_TABLE = "mod_workbench";
    public static final String INV_LASER_TURRET = "laser_turret";

    private static final String GUI_KEY = Algane.MOD_ID + ".gui.";
    public static final String GUI_LASER_GUN_TABLE = GUI_KEY + "laser_workbench";
    public static final String GUI_MODIFIER_TABLE = GUI_KEY + "mod_workbench";
    public static final String GUI_LASER_TURRET = GUI_KEY + "laser_turret";

    public static final ResourceLocation ENTITY_SHOCK_ORB = Algane.INSTANCE.newResourceLocation("shock_orb");

    private static final String TT_KEY = Algane.MOD_ID + ".tooltip.";
    public static final String TT_ENERGY = TT_KEY + "energy";
    public static final String TT_HEAT = TT_KEY + "heat";
    public static final String TT_MOD_POWER = TT_KEY + "mod_power";
    public static final String TT_MOD_EFF = TT_KEY + "mod_efficiency";
    public static final String TT_MOD_HEAT = TT_KEY + "mod_overheat";
    public static final String TT_MOD_WEIGHT = TT_KEY + "mod_weight";
    public static final String TT_STAT_DAMAGE = TT_KEY + "stat_damage";
    public static final String TT_STAT_ENERGY = TT_KEY + "stat_energy";
    public static final String TT_STAT_HEAT = TT_KEY + "stat_overheat";
    public static final String TT_FRACTION = TT_KEY + "fraction";
    private static final String[] TT_MODES = Arrays.stream(LaserGunCore.FiringParadigm.VALUES)
            .map(m -> TT_KEY + "mode_" + m.name().toLowerCase()).toArray(String[]::new);
    public static final String TT_RATE_DAMAGE = TT_KEY + "rate_damage";
    public static final String TT_RATE_FIRERATE = TT_KEY + "rate_firerate";
    public static final String TT_RATE_RANGE = TT_KEY + "rate_range";
    public static final String TT_KNOCKBACK = TT_KEY + "knockback";
    public static final String TT_SPLASH = TT_KEY + "splash";

    public static String getTooltipFireMode(LaserGunCore.FiringParadigm mode) {
        return TT_MODES[mode.ordinal()];
    }

    private static final String TT_BTN_KEY = TT_KEY + "button.";
    public static final String TT_BTN_INSTALL = TT_BTN_KEY + "install";
    public static final String TT_BTN_AUGMENT = TT_BTN_KEY + "augment";

    private static final String STATUS_KEY = Algane.MOD_ID + ".status.";

    private static final String STATUS_GUN_KEY = STATUS_KEY + "gun.";
    public static final String STATUS_GUN_OVERHEAT = STATUS_GUN_KEY + "overheat";
    public static final String STATUS_GUN_MISSING_CORE = STATUS_GUN_KEY + "missing_core";
    public static final String STATUS_GUN_MISSING_ENERGY = STATUS_GUN_KEY + "missing_energy";

    private static final String MISC_KEY = Algane.MOD_ID + ".misc.";
    public static final String MISC_STAT_DAMAGE = MISC_KEY + "stat_damage";
    public static final String MISC_STAT_ENERGY = MISC_KEY + "stat_energy";
    public static final String MISC_STAT_HEAT = MISC_KEY + "stat_overheat";

    private static final String LASER_TIER_KEY = MISC_KEY + "laser_tier.";
    private static final String[] LASER_TIER_NAMES = Arrays.stream(ItemLaserGun.Tier.VALUES)
            .map(t -> LASER_TIER_KEY + t.name().toLowerCase()).toArray(String[]::new);

    public static String getLaserTierName(ItemLaserGun.Tier tier) {
        return LASER_TIER_NAMES[tier.ordinal()];
    }

    public static String getLaserTierName(BlockLaserTurret.Tier tier) {
        return LASER_TIER_NAMES[tier.ordinal()];
    }

    private static final String LASER_CORE_KEY = MISC_KEY + "laser_core.";
    private static final String[] LASER_CORE_NAMES = Arrays.stream(ItemLaserCore.Type.VALUES)
            .map(t -> LASER_CORE_KEY + t.name().toLowerCase()).toArray(String[]::new);

    public static String getLaserCoreName(ItemLaserCore.Type type) {
        return LASER_CORE_NAMES[type.ordinal()];
    }

    private static final String LASER_POSTFIX_KEY = MISC_KEY + "laser_postfix.";
    public static final String LASER_POSTFIX_TURRET = LASER_POSTFIX_KEY + "turret";

    public static final String DMG_SRC_LASER = "alganeLaser";
    public static final String DMG_SRC_ASMD = "alganeAsmd";
    public static final String DMG_SRC_ORB = "alganeOrb";
    public static final String DMG_SRC_COMBO = "alganeShockCombo";
    public static final String DMG_SRC_GAUSS = "alganeGauss";

}
