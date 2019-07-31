package xyz.phanta.algane.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import xyz.phanta.algane.init.AlganeBlocks;
import xyz.phanta.algane.init.AlganeItems;
import xyz.phanta.algane.recipe.LaserUpgradeRecipe;

@JEIPlugin
public class AlganeJei implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry registry) {
        registry.registerSubtypeInterpreter(AlganeItems.LASER_GUN, g -> Integer.toString(g.getMetadata()));
        registry.registerSubtypeInterpreter(AlganeBlocks.TURRET.getItemBlock(), g -> Integer.toString(g.getMetadata()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(LaserUpgradeRecipe.class, new LaserUpgradeRecipeWrapper.Factory(registry.getJeiHelpers()),
                VanillaRecipeCategoryUid.CRAFTING);
    }

}
