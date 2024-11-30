package net.alshanex.equinox.setup;

import net.alshanex.equinox.EquinoxMod;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.render.*;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.item.Orb;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = EquinoxMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            ModItems.getOrbItems().stream().filter(item -> item.get() instanceof Orb).forEach((item) -> CuriosRendererRegistry.register(item.get(), SpellBookCurioRenderer::new));
        });
    }
}
