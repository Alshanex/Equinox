package net.alshanex.equinox.entity.renderers;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.alshanex.equinox.entity.models.BloodBrotherEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BloodBrotherEntityRenderer extends AbstractSpellCastingMobRenderer {
    public BloodBrotherEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BloodBrotherEntityModel());
    }
}
