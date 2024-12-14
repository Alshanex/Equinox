package net.alshanex.equinox.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.world.entity.monster.Ghast;

public class MiniGhastRenderer extends GhastRenderer {
    public MiniGhastRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void scale(Ghast entity, PoseStack poseStack, float partialTickTime) {
        float scale = 0.5F;
        poseStack.scale(scale, scale, scale);
        super.scale(entity, poseStack, partialTickTime);
    }
}
