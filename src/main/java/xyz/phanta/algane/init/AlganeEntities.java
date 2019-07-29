package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import xyz.phanta.algane.Algane;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.entity.EntityShockOrb;

public class AlganeEntities {

    private static int nextId = 0;

    @InitMe
    public static void init() {
        EntityRegistry.registerModEntity(LangConst.ENTITY_SHOCK_ORB, EntityShockOrb.class,
                LangConst.ENTITY_SHOCK_ORB.getPath(), getNextId(), Algane.INSTANCE, 64, 2, true);
    }

    private static int getNextId() {
        return nextId++;
    }

}
