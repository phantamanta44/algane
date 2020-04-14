package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.capability.StatelessCapabilitySerializer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;

@SuppressWarnings("NotNullFieldNotInitialized")
public class AlganeCaps {

    @CapabilityInject(LaserGun.class)
    public static Capability<LaserGun> LASER_GUN;

    @CapabilityInject(LaserGunCore.class)
    public static Capability<LaserGunCore> LASER_GUN_CORE;

    @CapabilityInject(LaserGunModifier.class)
    public static Capability<LaserGunModifier> LASER_GUN_MOD;

    @InitMe
    public static void init() {
        CapabilityManager.INSTANCE.register(
                LaserGun.class, new StatelessCapabilitySerializer<>(), LaserGun.Impl::new);
        CapabilityManager.INSTANCE.register(
                LaserGunCore.class, new StatelessCapabilitySerializer<>(), LaserGunCore.Impl::new);
        CapabilityManager.INSTANCE.register(
                LaserGunModifier.class, new StatelessCapabilitySerializer<>(), LaserGunModifier.Impl::new);
    }

}
