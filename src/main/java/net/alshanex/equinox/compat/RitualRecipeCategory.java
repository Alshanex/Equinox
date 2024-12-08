package net.alshanex.equinox.compat;

import io.redspace.ironsspellbooks.registries.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.alshanex.equinox.Config;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.util.RitualRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class RitualRecipeCategory implements IRecipeCategory <RitualRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(EquinoxMod.MODID, "ritual_casting");
    public static final ResourceLocation TEXTURE = new ResourceLocation(EquinoxMod.MODID, "textures/gui/ritual_recipe_gui.png");

    public static final RecipeType<RitualRecipe> RITUAL_RECIPE_TYPE =
            new RecipeType<>(UID, RitualRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public RitualRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 146);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.PEDESTAL_BLOCK.get()));
    }

    @Override
    public RecipeType<RitualRecipe> getRecipeType() {
        return RITUAL_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("ui.equinox.ritual_recipe_jei");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, RitualRecipe ritualRecipe, IFocusGroup iFocusGroup) {
        List<Pair<Integer, Integer>> inputCoordinates = new ArrayList<>();
        inputCoordinates.add(new Pair<>(80, 11));
        inputCoordinates.add(new Pair<>(50, 41));
        inputCoordinates.add(new Pair<>(110, 41));
        inputCoordinates.add(new Pair<>(80, 71));

        for(int i = 0; i < ritualRecipe.getIngredients().size(); i++){
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, inputCoordinates.get(i).getA(), inputCoordinates.get(i).getB()).addIngredients(ritualRecipe.getIngredients().get(i));
        }
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 80, 41).addIngredients(Ingredient.of(new ItemStack(ritualRecipe.getCentralItem())));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 80, 116).addItemStack(new ItemStack(Config.ritualRecipes.get(ritualRecipe)));
    }
}
