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

import java.util.*;

public class SpyglassFactionInspect implements IGuiOverlay {
    public static SpyglassFactionInspect instance = new SpyglassFactionInspect();

    public final static ResourceLocation CELESTIAL_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/celestial_icon.png");
    public final static ResourceLocation FALLEN_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/fallen_icon.png");
    public final static ResourceLocation UMBRAKITH_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/umbrakith_icon.png");
    public final static ResourceLocation SOLARIAN_ICON = new ResourceLocation(EquinoxMod.MODID, "textures/gui/solarian_icon.png");
    private final int pixelsPerLetter = 6;


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
        List<Integer> YPositions = Arrays.asList(posYFirstIcon, posYSecondIcon, posYThirdIcon, posYFourthIcon);

        List<ResourceLocation> currentIcons = new ArrayList<>();

        HashMap<ResourceLocation, Integer> textColors = new HashMap<>();
        textColors.put(CELESTIAL_ICON, 0xF2F0AF);
        textColors.put(FALLEN_ICON, 0xAB2F2F);
        textColors.put(SOLARIAN_ICON, 0xE69224);
        textColors.put(UMBRAKITH_ICON, 0x248B84);

        HashMap<ResourceLocation, String> texts = new HashMap<>();
        texts.put(CELESTIAL_ICON, "CELESTIAL");
        texts.put(FALLEN_ICON, "FALLEN");
        texts.put(SOLARIAN_ICON, "SOLARIAN");
        texts.put(UMBRAKITH_ICON, "UMBRAKITH");

        Entity entityLookingAt = getTargetedEntity(player);

        if(entityLookingAt != null){
            if(entityLookingAt.getType().is(EntityTagGenerator.SOLARIAN_FACTION_BOSSES) || entityLookingAt.getType().is(EntityTagGenerator.SOLARIAN_FACTION_ENTITIES)){
                currentIcons.add(SOLARIAN_ICON);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.FALLEN_FACTION_ENTITIES) || entityLookingAt.getType().is(EntityTagGenerator.FALLEN_FACTION_BOSSES)){
                currentIcons.add(FALLEN_ICON);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.CELESTIAL_FACTION_BOSSES) || entityLookingAt.getType().is(EntityTagGenerator.CELESTIAL_FACTION_ENTITIES)){
                currentIcons.add(CELESTIAL_ICON);
            }
            if(entityLookingAt.getType().is(EntityTagGenerator.UMBRAKITH_FACTION_ENTITIES) || entityLookingAt.getType().is(EntityTagGenerator.UMBRAKITH_FACTION_BOSSES)){
                currentIcons.add(UMBRAKITH_ICON);
            }
        }

        for(int i = 0; i < currentIcons.size(); i++){
            guiGraphics.blit(currentIcons.get(i), posX, YPositions.get(i), 0, 0, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

            int textX = posX + (ICON_WIDTH / 2) - (texts.get(currentIcons.get(i)).length() * pixelsPerLetter) / 2;
            int textY = YPositions.get(i) + ICON_HEIGHT + 3;
            guiGraphics.drawString(Minecraft.getInstance().font, texts.get(currentIcons.get(i)), textX, textY, textColors.get(currentIcons.get(i)), false);
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
