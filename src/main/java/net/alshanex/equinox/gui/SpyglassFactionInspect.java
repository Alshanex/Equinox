package net.alshanex.equinox.gui;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.datagen.EntityTagGenerator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraft.client.Minecraft;

public class SpyglassFactionInspect implements IGuiOverlay {
    public static SpyglassFactionInspect instance = new SpyglassFactionInspect();

    public final static ResourceLocation CELESTIAL_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/celestial_icon.png");
    public final static ResourceLocation FALLEN_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/fallen_icon.png");
    public final static ResourceLocation UMBRAKITH_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/umbrakith_icon.png");
    public final static ResourceLocation SOLARIAN_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/solarian_icon.png");

    static final int ICON_WIDTH = 20;
    static final int ICON_HEIGHT = 17;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        var player = Minecraft.getInstance().player;

        EquinoxMod.LOGGER.debug("He llegado a la condicion");

        if(player != null && !player.isScoping()){
            return;
        }

        EquinoxMod.LOGGER.debug("He pasado la condicion");

        int posXFirstIcon = screenWidth / 2 - ICON_WIDTH * 3 - ICON_WIDTH / 2;
        int posXSecondIcon = screenWidth / 2 - ICON_WIDTH - ICON_WIDTH / 2;
        int posXThirdIcon = screenWidth / 2 + ICON_WIDTH / 2;
        int posXFourthIcon = screenWidth / 2 - ICON_WIDTH * 2 - ICON_WIDTH / 2;
        int posY = screenHeight - ICON_HEIGHT * 2;

        Entity entityLookingAt = getTargetedEntity(player);

        if(entityLookingAt != null){
            EquinoxMod.LOGGER.debug("He llegado a la seleccion de iconos");
            if(entityLookingAt.getType().is(EntityTagGenerator.SOLARIAN_FACTION_BOSSES) || entityLookingAt.getType().is(EntityTagGenerator.SOLARIAN_FACTION_ENTITIES)){
                guiGraphics.blit(SOLARIAN_ICON, posXFirstIcon, posY, 0, 0, ICON_WIDTH, ICON_HEIGHT, 20, 17);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.FALLEN_FACTION_ENTITIES) || entityLookingAt.getType().is(EntityTagGenerator.FALLEN_FACTION_BOSSES)){
                guiGraphics.blit(FALLEN_ICON, posXSecondIcon, posY, 0, 0, ICON_WIDTH, ICON_HEIGHT, 20, 17);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.CELESTIAL_FACTION_BOSSES) || entityLookingAt.getType().is(EntityTagGenerator.CELESTIAL_FACTION_ENTITIES)){
                guiGraphics.blit(CELESTIAL_ICON, posXThirdIcon, posY, 0, 0, ICON_WIDTH, ICON_HEIGHT, 20, 17);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.UMBRAKITH_FACTION_ENTITIES) || entityLookingAt.getType().is(EntityTagGenerator.UMBRAKITH_FACTION_BOSSES)){
                EquinoxMod.LOGGER.debug("He llegado a la parte de dibujar");
                guiGraphics.blit(UMBRAKITH_ICON, posXFourthIcon, posY, 0, 0, ICON_WIDTH, ICON_HEIGHT, 20, 17);
            }
        }

    }

    private Entity getTargetedEntity(Player player) {
        Minecraft mc = Minecraft.getInstance();

        double maxDistance = 50.0;
        Vec3 startVec = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getViewVector(1.0f);
        Vec3 endVec = startVec.add(lookVec.scale(maxDistance));

        HitResult hitResult = mc.level.clip(new ClipContext(
                startVec,
                endVec,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        if (hitResult instanceof EntityHitResult entityHitResult) {
            return entityHitResult.getEntity();
        }

        return null;
    }
}
