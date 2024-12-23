package net.alshanex.equinox.entity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.alshanex.equinox.entity.LightRootEntity;
import net.alshanex.equinox.entity.models.LightRootModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class LightRootRenderer extends GeoLivingEntityRenderer<LightRootEntity> {
    public LightRootRenderer(EntityRendererProvider.Context context) {
        super(context, new LightRootModel());
    }

    @Override
    public void preRender(PoseStack poseStack, LightRootEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        var rooted = animatable.getFirstPassenger();

        if (rooted != null) {
            float scale = rooted.getBbWidth() / 0.6f; //.6 is the default player bb width
            poseStack.scale(scale, scale, scale);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
