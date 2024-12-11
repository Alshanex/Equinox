package net.alshanex.equinox.entity;

import net.alshanex.equinox.EquinoxMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class EldritchCloneRenderer extends LivingEntityRenderer<EldritchClone, PlayerModel<EldritchClone>> {

    private static final ResourceLocation CUSTOM_SKIN = new ResourceLocation(EquinoxMod.MODID, "textures/entity/eldritch_clone.png");

    public EldritchCloneRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);

        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()));

        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }
    @Override
    public ResourceLocation getTextureLocation(EldritchClone entity) {
        return CUSTOM_SKIN;
    }

}
