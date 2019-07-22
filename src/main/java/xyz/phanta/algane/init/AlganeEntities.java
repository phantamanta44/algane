package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.entity.EntityLaserBolt;

public class AlganeEntities {

    private static int nextEntityId = 0;

    @InitMe
    public static void init() {
        EntityRegistry.registerModEntity(
                LangConst.ENTITY_LASER_BOLT, EntityLaserBolt.class, LangConst.ENTITY_LASER_BOLT.getPath(),
                getNextId(), Algane.INSTANCE, 64, 1, false);
    }

    private static int getNextId() {
        return nextEntityId++;
    }

}
