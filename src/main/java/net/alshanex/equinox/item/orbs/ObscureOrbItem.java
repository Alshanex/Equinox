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

public class ObscureOrbItem extends UniqueOrb {
    public ObscureOrbItem(){
        super(SpellRarity.LEGENDARY, SpellDataRegistryHolder.of(
                //new SpellDataRegistryHolder(ExampleSpellRegistry.ICE_CHAMBER, 5),
                //new SpellDataRegistryHolder(ExampleSpellRegistry.ICE_AGE, 10)
        ), 0, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(ExternalAttributesRegistry.SPELL_SCHOOL_ELDRITCH, new AttributeModifier(UUID.fromString("654ad54g-901g-4691-b2a2-3664e47076d7"), "Weapon modifier", 2, AttributeModifier.Operation.ADDITION));
            builder.put(AttributeRegistry.ELDRITCH_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("667ad88g-904g-4781-b5a2-3664e42026d3"), "Weapon modifier", .10, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.ELDRITCH_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("667ad88g-951g-4291-b6a2-3664e22527d3"), "Weapon modifier", .10, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(UUID.fromString("667ad88f-904r-4691-b2a2-3664e81436d3"), "Weapon modifier", 100, AttributeModifier.Operation.ADDITION));
            return builder.build();
        });
    }
}
