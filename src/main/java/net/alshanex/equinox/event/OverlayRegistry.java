package net.alshanex.equinox.event;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.gui.SpyglassFactionInspect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EquinoxMod.MODID, value = Dist.CLIENT)
public class OverlayRegistry {
    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        EquinoxMod.LOGGER.debug("He llegado al registro de overlays");
        event.registerAbove(VanillaGuiOverlay.SPYGLASS.id(), "faction_inspection", SpyglassFactionInspect.instance);
    }
}
