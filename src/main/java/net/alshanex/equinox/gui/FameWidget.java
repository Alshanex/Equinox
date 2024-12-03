package net.alshanex.equinox.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.alshanex.equinox.fame.CelestialFameProvider;
import net.alshanex.equinox.fame.FallenFameProvider;
import net.alshanex.equinox.fame.SolarianFameProvider;
import net.alshanex.equinox.fame.UmbrakithFameProvider;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class FameWidget extends AbstractWidget {
    private final ResourceLocation texture;
    int currentFame = 0;
    int u = 0;
    private final int v, textureWidth, textureHeight;
    private final int subTextureWidth, subTextureHeight;
    private String text;
    private int textColor;

    public FameWidget(int x, int y, ResourceLocation texture, int u, int v,
                      int subTextureWidth, int subTextureHeight,
                      int textureWidth, int textureHeight,
                      String text, int textColor) {
        super(x, y, subTextureWidth, subTextureHeight, null);
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.subTextureWidth = subTextureWidth;
        this.subTextureHeight = subTextureHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.text = text;
        this.textColor = textColor;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var player = Minecraft.getInstance().player;

        if(EUtils.noItemInOrbSlot(player)){ return;}

        getCurrentFame(player);

        guiGraphics.blit(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight(), u, v, subTextureWidth, subTextureHeight, textureWidth, textureHeight);

        text = String.valueOf(currentFame);
        int textX = getX() + 2 * (subTextureWidth / 3);
        int textY = getY() + 5;
        guiGraphics.drawString(Minecraft.getInstance().font, text, textX, textY, textColor, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }

    private void getCurrentFame(Player player){
        if(EUtils.hasItemInOrbSlotLocal(player, ModItems.PLASMATIC_ORB.get())){
            player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                this.currentFame = fame.getFame();
                this.u = -1 + subTextureWidth * 4;
                this.textColor = 0xE69224;
            });
        }
        if(EUtils.hasItemInOrbSlotLocal(player, ModItems.BLESSED_ORB.get())){
            player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                this.currentFame = fame.getFame();
                this.u = 2 + subTextureWidth;
                this.textColor = 0xF2F0AF;
            });
        }
        if(EUtils.hasItemInOrbSlotLocal(player, ModItems.CORRUPTED_ORB.get())){
            player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                this.currentFame = fame.getFame();
                this.u = 3 + subTextureWidth * 2;
                this.textColor = 0xAB2F2F;
            });
        }
        if(EUtils.hasItemInOrbSlotLocal(player, ModItems.OBSCURE_ORB.get())){
            player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                this.currentFame = fame.getFame();
                this.u = 4 + subTextureWidth * 3;
                this.textColor = 0x248B84;
            });
        }
    }
}
