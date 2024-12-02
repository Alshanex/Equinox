package net.alshanex.equinox.event;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.gui.overlays.FactionOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event){
        EquinoxMod.LOGGER.debug("He llegado al metodo");
        event.registerAboveAll("faction_fame", FactionOverlay.instance);
    }
}
