package net.alshanex.equinox;

import net.alshanex.equinox.util.RitualRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
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
                            "minecraft:diamond;minecraft:apple,minecraft:stone,minecraft:stick,minecraft:iron_ingot;minecraft:nether_star",
                            "equinox:empty_orb;irons_spellbooks:blood_vial,minecraft:spider_eye,minecraft:fermented_spider_eye;equinox:corrupted_orb",
                            "minecraft:diamond;irons_spellbooks:blood_vial;equinox:corrupted_orb"
                    ),
                    obj -> obj instanceof String
            );

    public static Map<RitualRecipe, Item> ritualRecipes = new HashMap<>();

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static void loadConfigRecipes() {
        ritualRecipes.clear();
        loadRitualRecipes();
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
    }
}
