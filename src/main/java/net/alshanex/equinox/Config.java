package net.alshanex.equinox;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.util.RitualRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = EquinoxMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> RITUAL_RECIPES = BUILDER
            .comment("List of ritual recipes. Format: 'centralItem;input1,input2,input3,input4;output'")
            .defineListAllowEmpty(
                    "ritualRecipes",
                    List.of(
                            "minecraft:diamond;minecraft:apple,minecraft:stone,minecraft:stick,minecraft:iron_ingot;minecraft:emerald",
                            "minecraft:diamond;irons_spellbooks:blood_vial,minecraft:spider_eye,minecraft:fermented_spider_eye;minecraft:apple",
                            "minecraft:diamond;irons_spellbooks:blood_vial;minecraft:apple"
                    ),
                    obj -> obj instanceof String
            );

    public static Map<RitualRecipe, Item> ritualRecipes = new HashMap<>();

    static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    public static void onLoad(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ritualRecipes.clear();
            loadRitualRecipes();
        });
    }

    private static void loadRitualRecipes() {
        List<? extends String> recipesConfig = RITUAL_RECIPES.get();
        for (String recipe : recipesConfig) {
            String[] parts = recipe.split(";");
            if (parts.length != 3) {
                EquinoxMod.LOGGER.debug("Invalid recipe format: " + recipe);
                continue;
            }

            Item centralItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0]));
            if (centralItem == null) {
                EquinoxMod.LOGGER.debug("Invalid central item: " + parts[0]);
                continue;
            }

            String[] inputs = parts[1].split(",");
            Set<Item> inputSet = Arrays.stream(inputs)
                    .map(input -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(input)))
                    .collect(Collectors.toSet());

            if (inputSet.size() > 4 || inputSet.isEmpty()) {
                EquinoxMod.LOGGER.debug("Invalid input items in recipe: " + parts[1]);
                continue;
            }

            Item outputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[2]));
            if (outputItem == null) {
                EquinoxMod.LOGGER.debug("Invalid output item: " + parts[2]);
                continue;
            }

            RitualRecipe ritualRecipe = new RitualRecipe(centralItem, inputSet);
            ritualRecipes.put(ritualRecipe, outputItem);
        }

        List<Item> fallenOrbRecipeItems = Arrays.asList(ItemRegistry.BLOOD_VIAL.get(), Items.WITHER_SKELETON_SKULL, ItemRegistry.BLOOD_RUNE.get(), ItemRegistry.ARCANE_ESSENCE.get());
        Set<Item> setFallen = new HashSet<>(fallenOrbRecipeItems);
        RitualRecipe fallenOrbRecipe = new RitualRecipe(ModItems.EMPTY_ORB.get(), setFallen);
        ritualRecipes.put(fallenOrbRecipe, ModItems.CORRUPTED_ORB.get());

        List<Item> celestialOrbRecipeItems = Arrays.asList(ItemRegistry.DIVINE_PEARL.get(), ItemRegistry.PROTECTION_RUNE.get(), ItemRegistry.HOLY_RUNE.get(), ItemRegistry.ARCANE_ESSENCE.get());
        Set<Item> setCelestial = new HashSet<>(celestialOrbRecipeItems);
        RitualRecipe celestialOrbRecipe = new RitualRecipe(ModItems.EMPTY_ORB.get(), setCelestial);
        ritualRecipes.put(celestialOrbRecipe, ModItems.BLESSED_ORB.get());

        List<Item> umbrakithOrbRecipeItems = Arrays.asList(ItemRegistry.ELDRITCH_PAGE.get(), Items.SCULK_CATALYST, Items.ECHO_SHARD, ItemRegistry.ARCANE_ESSENCE.get());
        Set<Item> setUmbrakith = new HashSet<>(umbrakithOrbRecipeItems);
        RitualRecipe umbrakithOrbRecipe = new RitualRecipe(ModItems.EMPTY_ORB.get(), setUmbrakith);
        ritualRecipes.put(umbrakithOrbRecipe, ModItems.OBSCURE_ORB.get());

        List<Item> solarianOrbRecipeItems = Arrays.asList(ItemRegistry.FIRE_RUNE.get(), Items.BLAZE_POWDER, ItemRegistry.CINDER_ESSENCE.get(), ItemRegistry.ARCANE_ESSENCE.get());
        Set<Item> setSolarian = new HashSet<>(solarianOrbRecipeItems);
        RitualRecipe solarianOrbRecipe = new RitualRecipe(ModItems.EMPTY_ORB.get(), setSolarian);
        ritualRecipes.put(solarianOrbRecipe, ModItems.OBSCURE_ORB.get());
    }
}
