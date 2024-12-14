package net.alshanex.equinox.util;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.alshanex.equinox.EquinoxMod;
import net.minecraft.resources.ResourceLocation;

public class SpellCastAnimator {
    public static ResourceLocation ANIMATION_RESOURCE = new ResourceLocation(EquinoxMod.MODID, "animation");

    public static final AnimationHolder SHRIEK_ANIMATION = new AnimationHolder("equinox:shriek_animation", false, true);
}
