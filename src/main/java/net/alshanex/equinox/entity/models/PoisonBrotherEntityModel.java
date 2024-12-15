package net.alshanex.equinox.entity.models;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.alshanex.equinox.EquinoxMod;
import net.minecraft.resources.ResourceLocation;

public class PoisonBrotherEntityModel extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation(EquinoxMod.MODID, "textures/entity/poison_brother.png");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return TEXTURE;
    }
}
