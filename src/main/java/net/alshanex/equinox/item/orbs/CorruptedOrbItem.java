package net.alshanex.equinox.item.orbs;

import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.alshanex.equinox.item.UniqueOrb;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class CorruptedOrbItem extends UniqueOrb {
    public CorruptedOrbItem(){
        super(SpellRarity.LEGENDARY, SpellDataRegistryHolder.of(
                //new SpellDataRegistryHolder(ExampleSpellRegistry.ICE_CHAMBER, 5),
                //new SpellDataRegistryHolder(ExampleSpellRegistry.ICE_AGE, 10)
        ), 0, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(AttributeRegistry.BLOOD_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", .10, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.BLOOD_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", .10, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.HOLY_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", -.10, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", 100, AttributeModifier.Operation.ADDITION));
            return builder.build();
        });
    }
}
