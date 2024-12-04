package net.alshanex.equinox.datagen;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EntityTagGenerator extends EntityTypeTagsProvider {
    public static TagKey<EntityType<?>> FALLEN_FACTION_ENTITIES = createKey("fallen_faction_entities");
    public static TagKey<EntityType<?>> FALLEN_FACTION_BOSSES = createKey("fallen_faction_bosses");
    public static TagKey<EntityType<?>> CELESTIAL_FACTION_ENTITIES = createKey("celestial_faction_entities");
    public static TagKey<EntityType<?>> CELESTIAL_FACTION_BOSSES = createKey("celestial_faction_bosses");
    public static TagKey<EntityType<?>> UMBRAKITH_FACTION_ENTITIES = createKey("umbrakith_faction_entities");
    public static TagKey<EntityType<?>> UMBRAKITH_FACTION_BOSSES = createKey("umbrakith_faction_bosses");
    public static TagKey<EntityType<?>> SOLARIAN_FACTION_ENTITIES = createKey("solarian_faction_entities");
    public static TagKey<EntityType<?>> SOLARIAN_FACTION_BOSSES = createKey("solarian_faction_bosses");

    public EntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, provider, EquinoxMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isFallenFaction)
                .forEach(entityType -> tag(FALLEN_FACTION_ENTITIES).add(entityType));
        HashMap<String, List<String>> fallenOptionalEntities = new HashMap<>();
        fallenOptionalEntities.put("cataclysm", Arrays.asList("draugr", "royal_draugr", "elite_draugr"));
        fallenOptionalEntities.put("graveyard", Arrays.asList("skeleton_creeper", "acolyte", "reaper", "reaper", "ghoul", "nightmare", "revenant", "lich", "wraith", "corrupted_pillager", "corrupted_vindicator"));
        fallenOptionalEntities.put("alexscaves", Arrays.asList("underzealot", "watcher", "corrodent", "vesper", "forsaken", "radgill", "brainiac"));

        for (String key : fallenOptionalEntities.keySet()) {
            List<String> values = fallenOptionalEntities.get(key);
            for(String value : values){
                tag(FALLEN_FACTION_ENTITIES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isFallenFactionBoss)
                .forEach(entityType -> tag(FALLEN_FACTION_BOSSES).add(entityType));

        HashMap<String, List<String>> fallenOptionalBosses = new HashMap<>();
        fallenOptionalBosses.put("cataclysm", Arrays.asList("aptrgangr", "maledictus"));

        for (String key : fallenOptionalBosses.keySet()) {
            List<String> values = fallenOptionalBosses.get(key);
            for(String value : values){
                tag(FALLEN_FACTION_BOSSES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isCelestialFaction)
                .forEach(entityType -> tag(CELESTIAL_FACTION_ENTITIES).add(entityType));

        HashMap<String, List<String>> celestialOptionalEntities = new HashMap<>();
        celestialOptionalEntities.put("cataclysm", Arrays.asList("koboleton"));

        for (String key : celestialOptionalEntities.keySet()) {
            List<String> values = celestialOptionalEntities.get(key);
            for(String value : values){
                tag(CELESTIAL_FACTION_ENTITIES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        HashMap<String, List<String>> celestialOptionalBosses = new HashMap<>();
        celestialOptionalBosses.put("cataclysm", Arrays.asList("ancient_ancient_remnant", "ancient_remnant", "modern_remnant", "kobolediator", "wadjet"));

        for (String key : celestialOptionalBosses.keySet()) {
            List<String> values = celestialOptionalBosses.get(key);
            for(String value : values){
                tag(CELESTIAL_FACTION_BOSSES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isUmbrakithFaction)
                .forEach(entityType -> tag(UMBRAKITH_FACTION_ENTITIES).add(entityType));

        HashMap<String, List<String>> umbrakithOptionalEntities = new HashMap<>();
        umbrakithOptionalEntities.put("cataclysm", Arrays.asList("deepling", "deepling_brute", "deepling_angler", "deepling_priest", "deepling_warlock"));
        umbrakithOptionalEntities.put("alexscaves", Arrays.asList("hullbreaker", "deep_one", "deep_one_mage", "deep_one_knight", "mine_guardian", "underzealot", "watcher", "corrodent", "vesper", "forsaken"));

        for (String key : umbrakithOptionalEntities.keySet()) {
            List<String> values = umbrakithOptionalEntities.get(key);
            for(String value : values){
                tag(UMBRAKITH_FACTION_ENTITIES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isUmbrakithFactionBoss)
                .forEach(entityType -> tag(UMBRAKITH_FACTION_BOSSES).add(entityType));

        HashMap<String, List<String>> umbrakithOptionalBosses = new HashMap<>();
        umbrakithOptionalBosses.put("cataclysm", Arrays.asList("coral_golem", "coralssus", "the_leviathan", "the_baby_leviathan"));

        for (String key : umbrakithOptionalBosses.keySet()) {
            List<String> values = umbrakithOptionalBosses.get(key);
            for(String value : values){
                tag(UMBRAKITH_FACTION_BOSSES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isSolarianFaction)
                .forEach(entityType -> tag(SOLARIAN_FACTION_ENTITIES).add(entityType));

        HashMap<String, List<String>> solarianOptionalEntities = new HashMap<>();
        solarianOptionalEntities.put("cataclysm", Arrays.asList("ignited_revenant", "ignited_berserker"));

        for (String key : solarianOptionalEntities.keySet()) {
            List<String> values = solarianOptionalEntities.get(key);
            for(String value : values){
                tag(SOLARIAN_FACTION_ENTITIES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        HashMap<String, List<String>> solarianOptionalBosses = new HashMap<>();
        solarianOptionalBosses.put("cataclysm", Arrays.asList("netherite_monstrosity", "ignis"));

        for (String key : solarianOptionalBosses.keySet()) {
            List<String> values = solarianOptionalBosses.get(key);
            for(String value : values){
                tag(SOLARIAN_FACTION_BOSSES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }
    }

    private static TagKey<EntityType<?>> createKey(final String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(EquinoxMod.MODID, name));
    }
}
