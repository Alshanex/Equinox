package net.alshanex.equinox.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public class ExternalAttributesRegistry {
    public static final ResourceLocation SPELL_SCHOOL_HOLY_ID =
            new ResourceLocation("additional_attributes", "spell_school_holy");
    public static final ResourceLocation SPELL_SCHOOL_BLOOD_ID =
            new ResourceLocation("additional_attributes", "spell_school_blood");
    public static final ResourceLocation SPELL_SCHOOL_ELDRITCH_ID =
            new ResourceLocation("additional_attributes", "spell_school_eldritch");
    public static final ResourceLocation SPELL_SCHOOL_FIRE_ID =
            new ResourceLocation("additional_attributes", "spell_school_fire");

    public static Attribute SPELL_SCHOOL_HOLY;
    public static Attribute SPELL_SCHOOL_BLOOD;
    public static Attribute SPELL_SCHOOL_ELDRITCH;
    public static Attribute SPELL_SCHOOL_FIRE;

    public static void init() {
        SPELL_SCHOOL_HOLY = ForgeRegistries.ATTRIBUTES.getValue(SPELL_SCHOOL_HOLY_ID);
        SPELL_SCHOOL_BLOOD = ForgeRegistries.ATTRIBUTES.getValue(SPELL_SCHOOL_BLOOD_ID);
        SPELL_SCHOOL_ELDRITCH = ForgeRegistries.ATTRIBUTES.getValue(SPELL_SCHOOL_ELDRITCH_ID);
        SPELL_SCHOOL_FIRE = ForgeRegistries.ATTRIBUTES.getValue(SPELL_SCHOOL_FIRE_ID);
    }
}
