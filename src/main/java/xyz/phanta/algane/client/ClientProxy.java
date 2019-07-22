package xyz.phanta.algane.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.phanta.algane.CommonProxy;
import xyz.phanta.algane.client.particle.ParticleLaserBolt;

public class ClientProxy extends CommonProxy {

    @Override
    public void spawnParticleBolt(World world, Vec3d pos, int colour) {
        if (world.isRemote) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLaserBolt(world, pos, colour));
        }
    }

}
