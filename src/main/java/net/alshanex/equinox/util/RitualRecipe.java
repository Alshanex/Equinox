package net.alshanex.equinox.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;
import java.util.Set;

public class RitualRecipe {
    private final Item centralItem;
    private final Set<Item> inputItems;

    public RitualRecipe(Item centralItem, Set<Item> inputItems) {
        this.centralItem = centralItem;
        this.inputItems = inputItems;
    }

    public Item getCentralItem() {
        return centralItem;
    }

    public Set<Item> getInputItems() {
        return inputItems;
    }

    public NonNullList<Ingredient> getIngredients(){
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (Item item : getInputItems()) {
            if (item != null) {
                Ingredient ingredient = Ingredient.of(new ItemStack(item));
                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RitualRecipe that = (RitualRecipe) o;
        return Objects.equals(centralItem, that.centralItem) && Objects.equals(inputItems, that.inputItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centralItem, inputItems);
    }
}
