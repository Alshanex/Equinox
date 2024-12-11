package net.alshanex.equinox.setup;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.EldritchCloneRenderer;
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
    }
}
