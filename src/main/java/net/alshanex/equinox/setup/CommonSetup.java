package net.alshanex.equinox.setup;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.BloodBrotherEntity;
import net.alshanex.equinox.entity.EldritchClone;
import net.alshanex.equinox.entity.PoisonBrotherEntity;
import net.alshanex.equinox.registry.EntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EquinoxMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.ELDRITCH_CLONE.get(), EldritchClone.createCloneAttributes().build());
        event.put(EntityRegistry.BLOOD_BROTHER.get(), BloodBrotherEntity.prepareAttributes().build());
        event.put(EntityRegistry.POISON_BROTHER.get(), PoisonBrotherEntity.prepareAttributes().build());
    }
}
