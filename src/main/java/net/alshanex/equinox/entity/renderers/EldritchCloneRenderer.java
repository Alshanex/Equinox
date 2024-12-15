package net.alshanex.equinox.entity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.alshanex.equinox.entity.EldritchClone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class EldritchCloneRenderer extends LivingEntityRenderer<EldritchClone, EntityModel<EldritchClone>> {

    private static final ResourceLocation WARDEN_TEXTURE = new ResourceLocation("minecraft", "textures/entity/warden/warden.png");


    public EldritchCloneRenderer(EntityRendererProvider.Context context) {
        super(context, new WardenModel<>(context.bakeLayer(ModelLayers.WARDEN)), 0.5F);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(EldritchClone entity) {
        return WARDEN_TEXTURE;
    }

    @Override
    public void render(EldritchClone entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        LivingEntity target = entity.getTarget();

        if (target != null && target instanceof Player player) {
            PlayerModel<Player> playerModel = getPlayerModel(player);

            if (playerModel != null) {
                copyModelPoses(playerModel, (PlayerModel<EldritchClone>) this.getModel());
            }
        }

        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private PlayerModel<Player> getPlayerModel(Player player) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super Player> renderer = dispatcher.getRenderer(player);

        if (renderer instanceof LivingEntityRenderer) {
            return (PlayerModel<Player>) ((LivingEntityRenderer<?, ?>) renderer).getModel();
        }
        return null;
    }

    private void copyModelPoses(PlayerModel<Player> sourceModel, PlayerModel<EldritchClone> targetModel) {
        targetModel.head.copyFrom(sourceModel.head);
        targetModel.body.copyFrom(sourceModel.body);
        targetModel.rightArm.copyFrom(sourceModel.rightArm);
        targetModel.leftArm.copyFrom(sourceModel.leftArm);
        targetModel.rightLeg.copyFrom(sourceModel.rightLeg);
        targetModel.leftLeg.copyFrom(sourceModel.leftLeg);
        targetModel.hat.copyFrom(sourceModel.hat);
    }
}
