package xyz.phanta.algane.constant;

import xyz.phanta.algane.Algane;

public class LangConst {

    public static final String ITEM_LASER_GUN = "laser_gun";
    public static final String ITEM_LASER_CORE = "laser_core";
    public static final String ITEM_LASER_MOD = "laser_mod";
    public static final String ITEM_MISC = "misc";

    public static final String BLOCK_LASER_GUN_TABLE = "laser_workbench";

    public static final String INV_LASER_GUN_TABLE = "laser_workbench";
    public static final String INV_MODIFIER_TABLE = "mod_workbench";

    private static final String GUI_KEY = Algane.MOD_ID + ".gui.";
    public static final String GUI_LASER_GUN_TABLE = GUI_KEY + "laser_workbench";
    public static final String GUI_MODIFIER_TABLE = GUI_KEY + "mod_workbench";

    private static final String TT_KEY = Algane.MOD_ID + ".tooltip.";
    public static final String TT_ENERGY = TT_KEY + "energy";
    public static final String TT_MOD_POWER = TT_KEY + "mod_power";
    public static final String TT_MOD_EFF = TT_KEY + "mod_efficiency";
    public static final String TT_MOD_HEAT = TT_KEY + "mod_overheat";
    public static final String TT_MOD_WEIGHT = TT_KEY + "mod_weight";
    public static final String TT_FRACTION = TT_KEY + "fraction";

    private static final String STATUS_KEY = Algane.MOD_ID + ".status.";

    private static final String STATUS_GUN_KEY = STATUS_KEY + "gun.";
    public static final String STATUS_GUN_OVERHEAT = STATUS_GUN_KEY + "overheat";
    public static final String STATUS_GUN_MISSING_CORE = STATUS_GUN_KEY + "missing_core";
    public static final String STATUS_GUN_MISSING_ENERGY = STATUS_GUN_KEY + "missing_energy";

    private static final String MISC_KEY = Algane.MOD_ID + ".misc.";
    public static final String MISC_STAT_DAMAGE = MISC_KEY + "stat_damage";
    public static final String MISC_STAT_ENERGY = MISC_KEY + "stat_energy";
    public static final String MISC_STAT_HEAT = MISC_KEY + "stat_overheat";

}
