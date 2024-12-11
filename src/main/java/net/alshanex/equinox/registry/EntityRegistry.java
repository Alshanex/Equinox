package net.alshanex.equinox.registry;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.EldritchClone;
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
            ENTITIES.register("eldritch_clone", () -> EntityType.Builder.<EldritchClone>of(EldritchClone::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(EquinoxMod.MODID, "eldritch_clone").toString()));
}
