package net.alshanex.equinox.item.orbs;

import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.alshanex.equinox.item.UniqueOrb;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CorruptedOrbItem extends UniqueOrb {
    public CorruptedOrbItem(){
        super(SpellRarity.LEGENDARY, SpellDataRegistryHolder.of(
                //new SpellDataRegistryHolder(ExampleSpellRegistry.ICE_CHAMBER, 5),
                //new SpellDataRegistryHolder(ExampleSpellRegistry.ICE_AGE, 10)
        ), 0, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(AttributeRegistry.BLOOD_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", .03, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.BLOOD_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", .03, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.HOLY_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", -.50, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.LIGHTNING_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", -.25, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.ICE_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", -.25, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.ELDRITCH_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", -.25, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.FIRE_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", -.50, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.EVOCATION_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", -.50, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, lines, flag);
        var affinityData = AffinityData.getAffinityData(itemStack);
        var spell = affinityData.getSpell();
        if (spell != SpellRegistry.none()) {
            int i = TooltipsUtils.indexOfComponent(lines, "tooltip.irons_spellbooks.spellbook_spell_count");
            lines.add(i < 0 ? lines.size() : i+1, Component.translatable("tooltip.irons_spellbooks.enhance_spell_level", spell.getDisplayName(MinecraftInstanceHelper.instance.player()).withStyle(spell.getSchoolType().getDisplayName().getStyle())).withStyle(ChatFormatting.YELLOW));
        }
        lines.add(Component.translatable("tooltip.equinox.not_cast_holy_spells").withStyle(ChatFormatting.RED));
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        super.initializeSpellContainer(itemStack);
        AffinityData.setAffinityData(itemStack, SpellRegistry.BLOOD_SLASH_SPELL.get());
    }
}
