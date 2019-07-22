package xyz.phanta.algane.util;

import xyz.phanta.algane.lasergun.LaserGunModifier;

public class ModifierAccumulator implements LaserGunModifier {

    private float powerMod, effMod, heatMod;

    public ModifierAccumulator(float powerMod, float effMod, float heatMod) {
        this.powerMod = powerMod;
        this.effMod = effMod;
        this.heatMod = heatMod;
    }

    public ModifierAccumulator(LaserGunModifier initial) {
        this(initial.getPowerMod(), initial.getEfficiencyMod(), initial.getHeatMod());
    }

    public ModifierAccumulator() {
        this(0F, 0F, 0F);
    }

    @Override
    public float getPowerMod() {
        return powerMod;
    }

    @Override
    public float getEfficiencyMod() {
        return effMod;
    }

    @Override
    public float getHeatMod() {
        return heatMod;
    }

    public void accumulate(LaserGunModifier mod) {
        addPowerMod(mod.getPowerMod());
        addEfficiencyMod(mod.getEfficiencyMod());
        addHeatMod(mod.getHeatMod());
    }

    public void addPowerMod(float offset) {
        powerMod = Math.min(powerMod + offset, 1F);
    }

    public void addEfficiencyMod(float offset) {
        effMod = Math.min(effMod + offset, 1F);
    }

    public void addHeatMod(float offset) {
        heatMod = Math.min(heatMod + offset, 1F);
    }

}
