package net.alshanex.equinox.registry;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.effect.EldritchDefinitiveSpellEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, EquinoxMod.MODID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    public static final RegistryObject<MobEffect> ELDRITCH_DEFINITIVE = MOB_EFFECT_DEFERRED_REGISTER.register("eldritch_definitive", () -> new EldritchDefinitiveSpellEffect(MobEffectCategory.BENEFICIAL, 0x6c42f5));
}
