package net.alshanex.equinox.event;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.fame.CelestialFameProvider;
import net.alshanex.equinox.fame.FallenFameProvider;
import net.alshanex.equinox.fame.SolarianFameProvider;
import net.alshanex.equinox.fame.UmbrakithFameProvider;
import net.alshanex.equinox.gui.FameWidget;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GUIEventHandler {
    public final static ResourceLocation TEXTURE = new ResourceLocation(EquinoxMod.MODID, "textures/gui/icons.png");

    static final int textureWidth = 213;
    static final int textureHeight = 54;
    static final int subTextureWidth = 52;
    static final int subTextureHeight = 18;

    int currentFame = 0;
    int u = 0;
    static final int v = 1;
    int textColor = 0;

    @SubscribeEvent
    public void onInventoryGuiInit(ScreenEvent.Init.Post evt) {
        Screen screen = evt.getScreen();

        if (screen instanceof InventoryScreen && !(screen instanceof CreativeModeInventoryScreen)) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;

            int guiLeft = gui.getGuiLeft();
            int guiTop = gui.getGuiTop();

            int iconX = guiLeft + 49 - subTextureWidth / 2;
            int iconY = guiTop - subTextureHeight + 4;

            evt.addListener(new FameWidget(iconX, iconY, TEXTURE, u, v,
                    subTextureWidth, subTextureHeight,
                    textureWidth, textureHeight,
                    String.valueOf(currentFame), textColor));
        }
    }
}
