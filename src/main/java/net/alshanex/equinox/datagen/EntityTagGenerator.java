package net.alshanex.equinox.datagen;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EntityTagGenerator extends EntityTypeTagsProvider {
    public static TagKey<EntityType<?>> FALLEN_FACTION_ENTITIES = createKey("fallen_faction_entities");
    public static TagKey<EntityType<?>> CELESTIAL_FACTION_ENTITIES = createKey("celestial_faction_entities");
    public static TagKey<EntityType<?>> UMBRAKITH_FACTION_ENTITIES = createKey("umbrakith_faction_entities");
    public static TagKey<EntityType<?>> SOLARIAN_FACTION_ENTITIES = createKey("solarian_faction_entities");

    public EntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, provider, EquinoxMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isFallenFaction)
                .forEach(entityType -> tag(FALLEN_FACTION_ENTITIES).add(entityType));

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isCelestialFaction)
                .forEach(entityType -> tag(CELESTIAL_FACTION_ENTITIES).add(entityType));

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isUmbrakithFaction)
                .forEach(entityType -> tag(UMBRAKITH_FACTION_ENTITIES).add(entityType));

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(EUtils::isSolarianFaction)
                .forEach(entityType -> tag(SOLARIAN_FACTION_ENTITIES).add(entityType));
    }

    private static TagKey<EntityType<?>> createKey(final String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(EquinoxMod.MODID, name));
    }
}
