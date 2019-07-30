package xyz.phanta.algane.event;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.algane.AlganeConfig;
import xyz.phanta.algane.init.AlganeItems;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LootTableHandler {

    private static final Set<ResourceLocation> DUNGEON = Stream.of(
            "abandoned_mineshaft", "desert_pyramid", "end_city_treasure", "igloo_chest", "jungle_temple",
            "nether_bridge", "simple_dungeon", "stronghold_corridor", "stronghold_crossing", "stronghold_library")
            .map(n -> new ResourceLocation("minecraft", "chests/" + n)).collect(Collectors.toSet());
    private static final ResourceLocation VILLAGE = new ResourceLocation("minecraft", "chests/village_blacksmith");
    private static final LootCondition[] NO_CONDS = new LootCondition[0];
    private static final int MAX_WEIGHT = 10000;

    @SubscribeEvent
    public void onLootGeneration(LootTableLoadEvent event) {
        if (event.getName().equals(VILLAGE) && AlganeConfig.worldGen.lootProbBlacksmith > 0) {
            int weight = (int)Math.ceil(MAX_WEIGHT * AlganeConfig.worldGen.lootProbBlacksmith);
            event.getTable().addPool(new LootPool(new LootEntry[] {
                    new LootEntryItem(AlganeItems.MISC, weight, 0, new LootFunction[] {
                            new SetMetadata(NO_CONDS, new RandomValueRange(2F))
                    }, NO_CONDS, "laser_mod"),
                    new LootEntryEmpty(MAX_WEIGHT - weight, 0, NO_CONDS, "no_laser_mod")
            }, NO_CONDS, new RandomValueRange(1F), new RandomValueRange(0F), "algane"));
        } else if (DUNGEON.contains(event.getName()) && AlganeConfig.worldGen.lootProbDungeon > 0) {
            int weight = (int)Math.ceil(MAX_WEIGHT * AlganeConfig.worldGen.lootProbDungeon);
            event.getTable().addPool(new LootPool(new LootEntry[] {
                    new LootEntryItem(AlganeItems.MISC, weight, 0, new LootFunction[] {
                            new SetMetadata(NO_CONDS, new RandomValueRange(2F))
                    }, NO_CONDS, "laser_mod"),
                    new LootEntryEmpty(MAX_WEIGHT - weight, 0, NO_CONDS, "no_laser_mod")
            }, NO_CONDS, new RandomValueRange(1F), new RandomValueRange(0F), "algane"));
        }
    }

}
