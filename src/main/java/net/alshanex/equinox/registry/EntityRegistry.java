package net.alshanex.equinox.registry;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EquinoxMod.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final RegistryObject<EntityType<EldritchClone>> ELDRITCH_CLONE =
            ENTITIES.register("eldritch_clone", () -> EntityType.Builder.<EldritchClone>of(EldritchClone::new, MobCategory.CREATURE)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(EquinoxMod.MODID, "eldritch_clone").toString()));

    public static final RegistryObject<EntityType<BouncingFirebolt>> BOUNCING_FIREBOLT =
            ENTITIES.register("bouncing_firebolt", () -> EntityType.Builder.<BouncingFirebolt>of(BouncingFirebolt::new, MobCategory.MISC)
                    .sized(.5f, .5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(EquinoxMod.MODID, "bouncing_firebolt").toString()));

    public static final RegistryObject<EntityType<MiniGhast>> MINI_GHAST =
            ENTITIES.register("mini_ghast", () -> EntityType.Builder.<MiniGhast>of(MiniGhast::new, MobCategory.MONSTER)
                    .sized(1.0F, 1.0F) // Tamaño de 1x1 bloque
                    .build(new ResourceLocation(EquinoxMod.MODID, "mini_ghast").toString())
    );

    public static final RegistryObject<EntityType<BloodBrotherEntity>> BLOOD_BROTHER =
            ENTITIES.register("blood_brother", () -> EntityType.Builder.<BloodBrotherEntity>of(BloodBrotherEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(EquinoxMod.MODID, "blood_brother").toString()));

    public static final RegistryObject<EntityType<PoisonBrotherEntity>> POISON_BROTHER =
            ENTITIES.register("poison_brother", () -> EntityType.Builder.<PoisonBrotherEntity>of(PoisonBrotherEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(EquinoxMod.MODID, "poison_brother").toString()));

    public static final RegistryObject<EntityType<LightRootEntity>> LIGHT_ROOT =
            ENTITIES.register("light_root", () -> EntityType.Builder.<LightRootEntity>of(LightRootEntity::new, MobCategory.MISC)
                    .sized(1, 1)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(EquinoxMod.MODID, "light_root").toString()));
}
