package xyz.phanta.algane.integration.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import xyz.phanta.algane.recipe.LaserUpgradeRecipe;

import javax.annotation.Nullable;

public class LaserUpgradeRecipeWrapper implements IShapedCraftingRecipeWrapper {

    private final LaserUpgradeRecipe recipe;
    private final IJeiHelpers helpers;

    private LaserUpgradeRecipeWrapper(LaserUpgradeRecipe recipe, IJeiHelpers helpers) {
        this.recipe = recipe;
        this.helpers = helpers;
    }

    @Override
    public int getWidth() {
        return recipe.getRecipeWidth();
    }

    @Override
    public int getHeight() {
        return recipe.getRecipeHeight();
    }

    @Override
    public void getIngredients(IIngredients ings) {
        ings.setInputLists(
                VanillaTypes.ITEM, helpers.getStackHelper().expandRecipeItemStackInputs(recipe.getIngredients()));
        ings.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return recipe.getRegistryName();
    }

    public static class Factory implements IRecipeWrapperFactory<LaserUpgradeRecipe> {

        private final IJeiHelpers helpers;

        public Factory(IJeiHelpers helpers) {
            this.helpers = helpers;
        }

        @Override
        public IRecipeWrapper getRecipeWrapper(LaserUpgradeRecipe recipe) {
            return new LaserUpgradeRecipeWrapper(recipe, helpers);
        }

    }

}
