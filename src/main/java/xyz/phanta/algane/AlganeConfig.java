package xyz.phanta.algane;

import net.minecraftforge.common.config.Config;

@Config(modid = Algane.MOD_ID)
public class AlganeConfig {

    @Config.Comment("Simple laser core configuration.")
    public static final CoreSimple coreSimple = new CoreSimple();

    @Config.Comment("Repeater laser core configuration.")
    public static final CoreRepeater coreRepeater = new CoreRepeater();

    @Config.Comment("Shock rifle core configuration.")
    public static final CoreShock coreShock = new CoreShock();

    @Config.Comment("Orb projector core configuration.")
    public static final CoreOrb coreOrb = new CoreOrb();

    @Config.Comment("Tau cannon core configuration.")
    public static final CoreGauss coreGauss = new CoreGauss();

    @Config.Comment("World generation configuration.")
    public static final WorldGeneration worldGen = new WorldGeneration();

    public static class CoreSimple {

        @Config.Comment("The base amount of FE consumed in a single shot.")
        @Config.RangeInt(min = 0)
        public int baseEnergyUse = 800;

        @Config.Comment("The base damage dealt by a single shot.")
        @Config.RangeInt(min = 0)
        public double baseDamage = 4D;

        @Config.Comment("The base heat percentage added by a single shot.")
        @Config.RangeDouble(min = 0D, max = 100D)
        public double baseHeat = 32D;

        @Config.Comment("The maximum range for the weapon, in blocks.")
        @Config.RangeDouble(min = 1D, max = 64D)
        public double maxRange = 48D;

        @Config.Comment("The cooldown period between shots, in ticks.")
        @Config.RangeInt(min = 1)
        public int shotDelay = 12;

    }

    public static class CoreRepeater {

        @Config.Comment("The base amount of FE consumed in a single shot.")
        @Config.RangeInt(min = 0)
        public int baseEnergyUse = 300;

        @Config.Comment("The base damage dealt by a single shot.")
        @Config.RangeInt(min = 0)
        public double baseDamage = 1.5D;

        @Config.Comment("The base heat percentage added by a single shot.")
        @Config.RangeDouble(min = 0D, max = 100D)
        public double baseHeat = 6D;

        @Config.Comment("The maximum range for the weapon, in blocks.")
        @Config.RangeDouble(min = 1D, max = 64D)
        public double maxRange = 32D;

        @Config.Comment("The cooldown period between shots, in ticks.")
        @Config.RangeInt(min = 1)
        public int shotDelay = 2;

        @Config.Comment("The maximum drift, expressed as a slope. Set to 0 to disable.")
        @Config.RangeDouble(min = 0D, max = 1D)
        public double driftMaxSlope = 0.15D;

        @Config.Comment("The number of ticks of continuous firing required to reach maximum drift.")
        @Config.RangeInt(min = 1)
        public int driftMaxTime = 18;

    }

    public static class CoreShock {

        @Config.Comment("The base amount of FE consumed in a single shot.")
        @Config.RangeInt(min = 0)
        public int baseEnergyUse = 1500;

        @Config.Comment("The base damage dealt by a single shot.")
        @Config.RangeInt(min = 0)
        public double baseDamage = 5.5D;

        @Config.Comment("The base heat percentage added by a single shot.")
        @Config.RangeDouble(min = 0D, max = 100D)
        public double baseHeat = 50D;

        @Config.Comment("The maximum range for the weapon, in blocks.")
        @Config.RangeDouble(min = 1D, max = 64D)
        public double maxRange = 64D;

        @Config.Comment("The cooldown period between shots, in ticks.")
        @Config.RangeInt(min = 1)
        public int shotDelay = 18;

        @Config.Comment("The magnitude of knockback applied by a shot.")
        @Config.RangeInt(min = 0)
        public double knockbackFactor = 1.5D;

    }

    public static class CoreOrb {

        @Config.Comment("The base amount of FE consumed per charge tick.")
        @Config.RangeInt(min = 0)
        public int baseEnergyUse = 100;

        @Config.Comment({
                "The base damage added per charge tick.",
                "Note that the real damage dealt is reduced by distance from the center of the explosion."
        })
        @Config.RangeInt(min = 0)
        public double baseDamage = 0.25D;

        @Config.Comment("The base heat percentage added by a single shot.")
        @Config.RangeDouble(min = 0D, max = 100D)
        public double baseHeat = 64D;

        @Config.Comment("The cooldown period between shots, in ticks.")
        @Config.RangeInt(min = 1)
        public int shotDelay = 8;

        @Config.Comment("The magnitude of recoil applied by a shot.")
        @Config.RangeInt(min = 0)
        public double recoilFactor = 0.25D;

        @Config.Comment({
                "The magnitude of knockback applied by an explosion.",
                "Note that the real knockback dealt is reduced by distance from the center of the explosion."
        })
        @Config.RangeInt(min = 0)
        public double knockbackFactor = 1.5D;

        @Config.Comment({
                "Bonus damage multiplier applied for a shock combo.",
                "Total damage is computed as `comboMultiplier * (shockRifleDamage + shockOrbDamage)`"
        })
        @Config.RangeDouble(min = 1D)
        public double shockComboMultiplier = 4D;

    }

    public static class CoreGauss {

        @Config.Comment("The base amount of FE consumed on the first charge tick.")
        @Config.RangeInt(min = 0)
        public int baseEnergyUse = 100;

        @Config.Comment({
                "The scaling factor for energy cost over charge time.",
                "The instantaneous energy cost at tick `t` is `baseEnergy * (1 + costFactor * t)`",
                "The total energy cost after `T` ticks is `baseEnergy * (T + costFactor * (T^2 + T) / 2)`"
        })
        @Config.RangeDouble(min = 0D)
        public double costFactor = 0.08D;

        @Config.Comment({
                "The base damage scaling factor over charge time.",
                "The total damage after `T` ticks is `baseDamage * T^3`",
                "The default value is selected so that the tau cannon becomes energy-efficient after 40 ticks.",
                "(i.e. the damage-to-energy ratio surpasses that of linear damage scaling)"
        })
        @Config.RangeInt(min = 0)
        public double baseDamage = 0.00025625D;

        @Config.Comment("The base heat percentage added by a single shot.")
        @Config.RangeDouble(min = 0D, max = 100D)
        public double baseHeat = 75D;

        @Config.Comment("The maximum range for the weapon, in blocks.")
        @Config.RangeDouble(min = 1D, max = 64D)
        public double maxRange = 64D;

        @Config.Comment("The cooldown period between shots, in ticks.")
        @Config.RangeInt(min = 1)
        public int shotDelay = 20;

        @Config.Comment("The magnitude of recoil applied by a shot.")
        @Config.RangeInt(min = 0)
        public double recoilFactor = 0.32D;

    }

    public static class WorldGeneration {

        @Config.Comment({
                "The probability that a rare modifier kit will generate in a village blacksmith's chest.",
                "Set to 0 to disable."
        })
        @Config.RangeDouble(min = 0D, max = 1D)
        @Config.RequiresMcRestart
        public double lootProbBlacksmith = 0.05D;

        @Config.Comment({
                "The probability that a rare modifier kit will generate in a dungeon loot chest.",
                "Set to 0 to disable."
        })
        @Config.RangeDouble(min = 0D, max = 1D)
        @Config.RequiresMcRestart
        public double lootProbDungeon = 0.1D;

    }

}
