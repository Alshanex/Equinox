package net.alshanex.equinox.gui;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.datagen.EntityTagGenerator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Optional;

public class SpyglassFactionInspect implements IGuiOverlay {
    public static SpyglassFactionInspect instance = new SpyglassFactionInspect();

    public final static ResourceLocation CELESTIAL_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/celestial_icon.png");
    public final static ResourceLocation FALLEN_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/fallen_icon.png");
    public final static ResourceLocation UMBRAKITH_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/umbrakith_icon.png");
    public final static ResourceLocation SOLARIAN_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/solarian_icon.png");
    private final int pixelsPerLetter = 6;
    private final String solarian = "SOLARIAN";
    private final String fallen = "FALLEN";
    private final String celestial = "CELESTIAL";
    private final String umbrakith = "UMBRAKITH";


    static final int ICON_WIDTH = 20;
    static final int ICON_HEIGHT = 17;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        var player = Minecraft.getInstance().player;

        if(!(player != null && player.isScoping())){
            return;
        }

        int posYFirstIcon = screenHeight / 4 - ICON_HEIGHT * 3;
        int posYSecondIcon = (screenHeight / 4) * 2 - ICON_HEIGHT * 3;
        int posYThirdIcon = (screenHeight / 4) * 3 - ICON_HEIGHT * 3;
        int posYFourthIcon = screenHeight - ICON_HEIGHT * 3;
        int posX = (int) (ICON_WIDTH * 1.5);

        Entity entityLookingAt = getTargetedEntity(player);

        if(entityLookingAt != null){
            if(entityLookingAt.getType().is(EntityTagGenerator.SOLARIAN_FACTION_BOSSES) || entityLookingAt.getType().is(EntityTagGenerator.SOLARIAN_FACTION_ENTITIES)){
                guiGraphics.blit(SOLARIAN_ICON, posX, posYFirstIcon, 0, 0, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

                int textX = posX + (ICON_WIDTH / 2) - (solarian.length() * pixelsPerLetter) / 2;
                int textY = posYFirstIcon + ICON_HEIGHT + 3;
                int textColor = 0xE69224;
                guiGraphics.drawString(Minecraft.getInstance().font, solarian, textX, textY, textColor, false);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.FALLEN_FACTION_ENTITIES) || entityLookingAt.getType().is(EntityTagGenerator.FALLEN_FACTION_BOSSES)){
                guiGraphics.blit(FALLEN_ICON, posX, posYSecondIcon, 0, 0, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

                int textX = posX + (ICON_WIDTH / 2) - (fallen.length() * pixelsPerLetter) / 2;
                int textY = posYSecondIcon + ICON_HEIGHT + 3;
                int textColor = 0xAB2F2F;
                guiGraphics.drawString(Minecraft.getInstance().font, fallen, textX, textY, textColor, false);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.CELESTIAL_FACTION_BOSSES) || entityLookingAt.getType().is(EntityTagGenerator.CELESTIAL_FACTION_ENTITIES)){
                guiGraphics.blit(CELESTIAL_ICON, posX, posYThirdIcon, 0, 0, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

                int textX = posX + (ICON_WIDTH / 2) - (celestial.length() * pixelsPerLetter) / 2;
                int textY = posYThirdIcon + ICON_HEIGHT + 3;
                int textColor = 0xF2F0AF;
                guiGraphics.drawString(Minecraft.getInstance().font, celestial, textX, textY, textColor, false);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.UMBRAKITH_FACTION_ENTITIES) || entityLookingAt.getType().is(EntityTagGenerator.UMBRAKITH_FACTION_BOSSES)){
                guiGraphics.blit(UMBRAKITH_ICON, posX, posYFourthIcon, 0, 0, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

                int textX = posX + (ICON_WIDTH / 2) - (umbrakith.length() * pixelsPerLetter) / 2;
                int textY = posYFourthIcon + ICON_HEIGHT + 3;
                int textColor = 0x248B84;
                guiGraphics.drawString(Minecraft.getInstance().font, umbrakith, textX, textY, textColor, false);
            }
        }

    }

    private double getMaxRenderDistanceForEntity() {
        Minecraft mc = Minecraft.getInstance();

        int renderDistanceChunks = mc.options.getEffectiveRenderDistance();
        return renderDistanceChunks * 16;
    }

    private Entity getTargetedEntity(Player player) {
        Minecraft mc = Minecraft.getInstance();

        double maxDistance = getMaxRenderDistanceForEntity();
        Vec3 startVec = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getViewVector(1.0f);
        Vec3 endVec = startVec.add(lookVec.scale(maxDistance));

        HitResult blockHitResult = mc.level.clip(new ClipContext(
                startVec,
                endVec,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        double blockHitDistance = maxDistance;
        if (blockHitResult != null && blockHitResult.getType() == HitResult.Type.BLOCK) {
            blockHitDistance = blockHitResult.getLocation().distanceTo(startVec);
        }

        Vec3 finalEndVec = startVec.add(lookVec.scale(blockHitDistance));
        AABB searchBox = new AABB(startVec, finalEndVec).inflate(1.0);
        List<Entity> entities = mc.level.getEntities(player, searchBox, e -> e.isPickable() && e != player);

        Entity closestEntity = null;
        double closestDistance = blockHitDistance;

        for (Entity entity : entities) {
            AABB entityBox = entity.getBoundingBox().inflate(0.5);
            Optional<Vec3> hit = entityBox.clip(startVec, finalEndVec);
            if (hit.isPresent()) {
                double distance = hit.get().distanceTo(startVec);
                if (distance < closestDistance) {
                    closestEntity = entity;
                    closestDistance = distance;
                }
            }
        }

        return closestEntity;
    }
}
