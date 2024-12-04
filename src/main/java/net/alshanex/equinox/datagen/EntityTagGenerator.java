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
        fallenOptionalEntities.put("eeeabsmobs", Arrays.asList("corpse", "corpse_villager", "immortal_skeleton", "immortal_knight", "immortal_shaman", "immortal_golem"));
        fallenOptionalEntities.put("iceandfire", Arrays.asList("dread_scuttler", "ghost", "dread_ghoul", "dread_knight", "dread_lich", "dread_beast", "dread_thrall"));
        fallenOptionalEntities.put("legendary_monsters", Arrays.asList("skeloraptor"));
        fallenOptionalEntities.put("twilightforest", Arrays.asList("skeleton_druid", "knight_phantom"));
        fallenOptionalEntities.put("netherexp", Arrays.asList("banshee", "vessel", "stampede"));
        fallenOptionalEntities.put("fromtheshadows", Arrays.asList("cleric"));
        fallenOptionalEntities.put("monsterplus", Arrays.asList("spectral_skeleton", "swamp_zombie", "overgrown_skeleton"));
        fallenOptionalEntities.put("soulsweapons", Arrays.asList("withered_demon", "remnant", "forlorn", "dark_sorcerer", "evil_forlorn"));
        fallenOptionalEntities.put("mutantmonsters", Arrays.asList("mutant_skeleton", "mutant_zombie"));

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
        fallenOptionalBosses.put("eeeabsmobs", Arrays.asList("corpse_warlock"));
        fallenOptionalBosses.put("legendary_monsters", Arrays.asList("skeletosaurus", "withered_abomination"));
        fallenOptionalBosses.put("soulsweapons", Arrays.asList("chaos_monarch", "accursed_lord_boss", "night_shade", "draugr_boss", "returning_knight"));

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
        celestialOptionalEntities.put("aether", Arrays.asList("valkyrie"));

        for (String key : celestialOptionalEntities.keySet()) {
            List<String> values = celestialOptionalEntities.get(key);
            for(String value : values){
                tag(CELESTIAL_FACTION_ENTITIES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        HashMap<String, List<String>> celestialOptionalBosses = new HashMap<>();
        celestialOptionalBosses.put("cataclysm", Arrays.asList("ancient_ancient_remnant", "ancient_remnant", "modern_remnant", "kobolediator", "wadjet"));
        celestialOptionalBosses.put("legendary_monsters", Arrays.asList("dune_sentinel"));
        celestialOptionalBosses.put("aether", Arrays.asList("valkyrie_queen"));
        celestialOptionalBosses.put("soulsweapons", Arrays.asList("day_stalker", "night_prowler", "moonknight"));

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
        umbrakithOptionalEntities.put("iceandfire", Arrays.asList("siren", "sea_serpent", "hydra"));
        umbrakithOptionalEntities.put("legendary_monsters", Arrays.asList("haunted_guard", "haunted_knight", "ambusher"));
        umbrakithOptionalEntities.put("aether", Arrays.asList("mimic"));
        umbrakithOptionalEntities.put("netherexp", Arrays.asList("apparition", "ecto_slab"));
        umbrakithOptionalEntities.put("fromtheshadows", Arrays.asList("froglin", "bulldrogioth", "nehemoth"));
        umbrakithOptionalEntities.put("monsterplus", Arrays.asList("crystal_zombie"));
        umbrakithOptionalEntities.put("soulsweapons", Arrays.asList("dark_sorcerer", "evil_forlorn"));
        umbrakithOptionalEntities.put("mutantmonsters", Arrays.asList("spider_pig"));
        umbrakithOptionalEntities.put("eldritch_end", Arrays.asList("aberration", "dendler"));
        umbrakithOptionalEntities.put("deeperdarker", Arrays.asList("sculk_centipede", "shriek_worm", "shattered", "stalker"));

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
        umbrakithOptionalBosses.put("eeeabsmobs", Arrays.asList("corpse_warlock", "guling_sentinel_heavy", "immortal_shaman", "immortal_executioner"));
        umbrakithOptionalBosses.put("legendary_monsters", Arrays.asList("ancient_guardian", "posessed_paladin"));
        umbrakithOptionalBosses.put("twilightforest", Arrays.asList("hydra"));
        umbrakithOptionalBosses.put("blue_skies", Arrays.asList("alchemist", "summoner"));
        umbrakithOptionalBosses.put("soulsweapons", Arrays.asList("day_stalker", "night_prowler", "chaos_monarch", "night_shade"));
        umbrakithOptionalBosses.put("eldritch_end", Arrays.asList("the_faceless"));
        umbrakithOptionalBosses.put("mowziesmobs", Arrays.asList("ferrous_wroughtnaut"));

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
        solarianOptionalEntities.put("legendary_monsters", Arrays.asList("lava_eater"));
        solarianOptionalEntities.put("twilightforest", Arrays.asList("fire_beetle", "carminite_ghastling", "carminite_ghastguard", "carminite_golem"));
        solarianOptionalEntities.put("aether", Arrays.asList("fire_minion"));
        solarianOptionalEntities.put("netherexp", Arrays.asList("apparition", "ecto_slab", "banshee", "vessel", "stampede"));
        solarianOptionalEntities.put("monsterplus", Arrays.asList("lava_squid", "mother_lava_squid"));

        for (String key : solarianOptionalEntities.keySet()) {
            List<String> values = solarianOptionalEntities.get(key);
            for(String value : values){
                tag(SOLARIAN_FACTION_ENTITIES)
                        .addOptional(new ResourceLocation(key, value));
            }
        }

        HashMap<String, List<String>> solarianOptionalBosses = new HashMap<>();
        solarianOptionalBosses.put("cataclysm", Arrays.asList("netherite_monstrosity", "ignis"));
        solarianOptionalBosses.put("eeeabsmobs", Arrays.asList("immortal_executioner"));
        solarianOptionalBosses.put("iceandfire", Arrays.asList("fire_dragon"));
        solarianOptionalBosses.put("twilightforest", Arrays.asList("ur_ghast"));
        solarianOptionalBosses.put("aether", Arrays.asList("sun_spirit"));
        solarianOptionalBosses.put("soulsweapons", Arrays.asList("day_stalker", "night_prowler", "accursed_lord_boss"));
        solarianOptionalBosses.put("mowziesmobs", Arrays.asList("umvuthi"));

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
