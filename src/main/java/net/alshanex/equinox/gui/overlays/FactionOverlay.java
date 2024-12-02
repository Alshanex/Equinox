package net.alshanex.equinox.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.fame.CelestialFameProvider;
import net.alshanex.equinox.fame.FallenFameProvider;
import net.alshanex.equinox.fame.SolarianFameProvider;
import net.alshanex.equinox.fame.UmbrakithFameProvider;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FactionOverlay implements IGuiOverlay {
    public static FactionOverlay instance = new FactionOverlay();

    public final static ResourceLocation TEXTURE = new ResourceLocation(EquinoxMod.MODID, "textures/gui/icons.png");

    static final int textureWidth = 213;
    static final int textureHeight = 54;
    static final int subTextureWidth = 52;
    static final int subTextureHeight = 18;

    int currentFame = 0;
    int u = 0;
    static final int v = 1;
    int textColor = 0;

    public void render(ForgeGui gui, GuiGraphics guiHelper, float partialTick, int screenWidth, int screenHeight) {
        var player = Minecraft.getInstance().player;
        var minecraft = Minecraft.getInstance();

        if (player == null || !(minecraft.screen instanceof InventoryScreen) || EUtils.noItemInOrbSlot(player)) {
            return;
        }

        getCurrentFame(player);

        InventoryScreen inventoryScreen = (InventoryScreen) minecraft.screen;
        int guiLeft = inventoryScreen.getGuiLeft();
        int guiTop = inventoryScreen.getGuiTop();
        int mannequinX = guiLeft + 51;

        int iconX = mannequinX - subTextureWidth / 2;
        int iconY = guiTop - subTextureHeight;

        guiHelper.blit(TEXTURE, iconX, iconY, u, v, subTextureWidth, subTextureHeight, textureWidth, textureHeight);

        String text = String.valueOf(currentFame);
        int textX = iconX + 2 * (subTextureWidth / 3);
        int textY = iconY + 4;

        guiHelper.drawString(minecraft.font, text, textX, textY, textColor);
    }

    private void getCurrentFame(Player player){
        if(EUtils.hasItemInOrbSlotLocal(player, ModItems.PLASMATIC_ORB.get())){
            player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                currentFame = fame.getFame();
                u = 4 + subTextureWidth * 4;
                textColor = 0xE69224;
            });
        }
        if(EUtils.hasItemInOrbSlotLocal(player, ModItems.BLESSED_ORB.get())){
            player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                currentFame = fame.getFame();
                u = 1 + subTextureWidth;
                textColor = 0xF2F0AF;
            });
        }
        if(EUtils.hasItemInOrbSlotLocal(player, ModItems.CORRUPTED_ORB.get())){
            player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                currentFame = fame.getFame();
                u = 2 + subTextureWidth * 2;
                textColor = 0x652929;
            });
        }
        if(EUtils.hasItemInOrbSlotLocal(player, ModItems.OBSCURE_ORB.get())){
            player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                currentFame = fame.getFame();
                u = 3 + subTextureWidth * 3;
                textColor = 0x248B84;
            });
        }
    }
}
