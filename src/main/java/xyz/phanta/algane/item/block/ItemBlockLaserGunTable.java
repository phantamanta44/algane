package xyz.phanta.algane.item.block;

import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemBlockStated;
import net.minecraft.item.ItemStack;
import xyz.phanta.algane.block.BlockLaserGunTable;

public class ItemBlockLaserGunTable extends L9ItemBlockStated implements ParameterizedItemModel.IParamaterized {

    public ItemBlockLaserGunTable(BlockLaserGunTable block) {
        super(block);
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("type", getBlock().getStates().get(stack.getMetadata()).get(BlockLaserGunTable.TYPE).name());
    }

}
