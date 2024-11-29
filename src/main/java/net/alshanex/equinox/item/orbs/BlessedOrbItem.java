package net.alshanex.equinox.item.orbs;

import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.alshanex.equinox.item.UniqueOrb;
import net.alshanex.equinox.registry.ExternalAttributesRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class BlessedOrbItem extends UniqueOrb {
    public BlessedOrbItem(){
        super(SpellRarity.LEGENDARY, SpellDataRegistryHolder.of(
                //new SpellDataRegistryHolder(ExampleSpellRegistry.ICE_CHAMBER, 5),
                //new SpellDataRegistryHolder(ExampleSpellRegistry.ICE_AGE, 10)
        ), 0, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(ExternalAttributesRegistry.SPELL_SCHOOL_HOLY, new AttributeModifier(UUID.fromString("667ad88g-901g-4691-b2a2-3664e47076d7"), "Weapon modifier", 2, AttributeModifier.Operation.ADDITION));
            builder.put(AttributeRegistry.HOLY_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("667ad88g-901g-4691-b2a2-3664e42026d3"), "Weapon modifier", .10, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.HOLY_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("667ad88g-901g-4691-b2a2-3664e22527d3"), "Weapon modifier", .10, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.BLOOD_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("667ad88g-901g-4691-b2a7-3664e21427d3"), "Weapon modifier", -.10, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(UUID.fromString("667ad88f-904r-4691-b2a2-3664e42026d3"), "Weapon modifier", 100, AttributeModifier.Operation.ADDITION));
            return builder.build();
        });
    }
}
