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

        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "draugr"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "royal_draugr"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "elite_draugr"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "skeleton_creeper"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "acolyte"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "reaper"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "ghoul"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "nightmare"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "revenant"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "lich"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "wraith"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "corrupted_pillager"));
        tag(FALLEN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("graveyard", "corrupted_vindicator"));

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isFallenFactionBoss)
                .forEach(entityType -> tag(FALLEN_FACTION_BOSSES).add(entityType));

        tag(FALLEN_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "aptrgangr"));
        tag(FALLEN_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "maledictus"));

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isCelestialFaction)
                .forEach(entityType -> tag(CELESTIAL_FACTION_ENTITIES).add(entityType));

        tag(CELESTIAL_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "koboleton"));

        tag(CELESTIAL_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "ancient_ancient_remnant"));
        tag(CELESTIAL_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "ancient_remnant"));
        tag(CELESTIAL_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "modern_remnant"));
        tag(CELESTIAL_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "kobolediator"));
        tag(CELESTIAL_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "wadjet"));

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isUmbrakithFaction)
                .forEach(entityType -> tag(UMBRAKITH_FACTION_ENTITIES).add(entityType));

        tag(UMBRAKITH_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "deepling"));
        tag(UMBRAKITH_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "deepling_brute"));
        tag(UMBRAKITH_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "deepling_angler"));
        tag(UMBRAKITH_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "deepling_priest"));
        tag(UMBRAKITH_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "deepling_warlock"));

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isUmbrakithFactionBoss)
                .forEach(entityType -> tag(UMBRAKITH_FACTION_BOSSES).add(entityType));

        tag(UMBRAKITH_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "coral_golem"));
        tag(UMBRAKITH_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "coralssus"));
        tag(UMBRAKITH_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "the_leviathan"));
        tag(UMBRAKITH_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "the_baby_leviathan"));

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isSolarianFaction)
                .forEach(entityType -> tag(SOLARIAN_FACTION_ENTITIES).add(entityType));

        tag(SOLARIAN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "ignited_revenant"));
        tag(SOLARIAN_FACTION_ENTITIES)
                .addOptional(new ResourceLocation("cataclysm", "ignited_berserker"));

        tag(SOLARIAN_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "netherite_monstrosity"));
        tag(SOLARIAN_FACTION_BOSSES)
                .addOptional(new ResourceLocation("cataclysm", "ignis"));
    }

    private static TagKey<EntityType<?>> createKey(final String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(EquinoxMod.MODID, name));
    }
}
