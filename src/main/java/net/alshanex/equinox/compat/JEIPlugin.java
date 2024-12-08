package net.alshanex.equinox.compat;

import mezz.jei.api.*;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import net.alshanex.equinox.Config;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.util.RitualRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JEIPlugin implements IModPlugin{
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(EquinoxMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RitualRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<RitualRecipe> ritualList = new ArrayList<>(Config.ritualRecipes.keySet());

        registration.addRecipes(RitualRecipeCategory.RITUAL_RECIPE_TYPE, ritualList);
    }
}
