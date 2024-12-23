package net.alshanex.equinox.setup;

import io.redspace.ironsspellbooks.entity.spells.firebolt.FireboltRenderer;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.renderers.BloodBrotherEntityRenderer;
import net.alshanex.equinox.entity.renderers.EldritchCloneRenderer;
import net.alshanex.equinox.entity.renderers.LightRootRenderer;
import net.alshanex.equinox.entity.renderers.PoisonBrotherEntityRenderer;
import net.alshanex.equinox.registry.EntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EquinoxMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.ELDRITCH_CLONE.get(), EldritchCloneRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BOUNCING_FIREBOLT.get(), FireboltRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_BROTHER.get(), BloodBrotherEntityRenderer::new);
        event.registerEntityRenderer(EntityRegistry.POISON_BROTHER.get(), PoisonBrotherEntityRenderer::new);
        event.registerEntityRenderer(EntityRegistry.LIGHT_ROOT.get(), LightRootRenderer::new);
    }
}
