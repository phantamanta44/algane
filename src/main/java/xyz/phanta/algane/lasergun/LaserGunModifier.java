package xyz.phanta.algane.lasergun;

public interface LaserGunModifier {

    float getPowerMod();

    float getEfficiencyMod();

    float getHeatMod();

    default float computeWeight() {
        float a = getPowerMod(), b = getEfficiencyMod(), c = getHeatMod();
        return (float)Math.sqrt(a * a + b * b + c * c);
    }

    class Impl implements LaserGunModifier {

        @Override
        public float getPowerMod() {
            return 0F;
        }

        @Override
        public float getEfficiencyMod() {
            return 0F;
        }

        @Override
        public float getHeatMod() {
            return 0F;
        }

    }

}
