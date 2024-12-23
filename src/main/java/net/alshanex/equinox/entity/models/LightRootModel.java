package net.alshanex.equinox.entity.models;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.LightRootEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LightRootModel extends GeoModel<LightRootEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EquinoxMod.MODID, "textures/entity/light_root.png");
    private static final ResourceLocation MODEL = new ResourceLocation(IronsSpellbooks.MODID, "geo/root.geo.json");
    public static final ResourceLocation ANIMS = new ResourceLocation(IronsSpellbooks.MODID, "animations/root_animations.json");

    @Override
    public ResourceLocation getModelResource(LightRootEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(LightRootEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(LightRootEntity animatable) {
        return ANIMS;
    }
}
