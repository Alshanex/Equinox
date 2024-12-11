package net.alshanex.equinox.registry;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.spells.EldritchDefinitiveSpell;
import net.alshanex.equinox.spells.RitualSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SpellRegistry {
    private static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY, EquinoxMod.MODID);
    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final RegistryObject<AbstractSpell> RITUAL = registerSpell(new RitualSpell());
    public static final RegistryObject<AbstractSpell> ELDRITCH_DEFINITIVE = registerSpell(new EldritchDefinitiveSpell());
}
