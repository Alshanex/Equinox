package net.alshanex.equinox.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.BloodBrotherEntity;
import net.alshanex.equinox.entity.PoisonBrotherEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class FallenBrothersSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(EquinoxMod.MODID, "fallen_brothers");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(150)
            .build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.summon_count", 2));
    }

    public FallenBrothersSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 30;
        this.baseManaCost = 150;

    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_START.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_FINISH.get());
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int summonTime = 20 * 60 * 2;
        float radius = 1.5f + .185f * spellLevel;
        for (int i = 0; i < spellLevel; i++) {
            PoisonBrotherEntity poisonBrother = new PoisonBrotherEntity(world, entity, true);
            BloodBrotherEntity bloodBrother = new BloodBrotherEntity(world, entity, true);
            poisonBrother.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(poisonBrother.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
            bloodBrother.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(bloodBrother.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
            poisonBrother.addEffect(new MobEffectInstance(MobEffectRegistry.RAISE_DEAD_TIMER.get(), summonTime, 0, false, false, false));
            bloodBrother.addEffect(new MobEffectInstance(MobEffectRegistry.RAISE_DEAD_TIMER.get(), summonTime, 0, false, false, false));
            var yrot = 6.281f / spellLevel * i + entity.getYRot() * Mth.DEG_TO_RAD;
            Vec3 spawn = Utils.moveToRelativeGroundLevel(world, entity.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 0, radius * Mth.sin(yrot))), 10);
            poisonBrother.setPos(spawn.x, spawn.y, spawn.z);
            bloodBrother.setPos(-spawn.x, spawn.y, -spawn.z);
            poisonBrother.setYRot(entity.getYRot());
            bloodBrother.setYRot(entity.getYRot());
            poisonBrother.setOldPosAndRot();
            bloodBrother.setOldPosAndRot();
            world.addFreshEntity(poisonBrother);
            world.addFreshEntity(bloodBrother);
        }

        int effectAmplifier = spellLevel - 1;
        if (entity.hasEffect(MobEffectRegistry.RAISE_DEAD_TIMER.get()))
            effectAmplifier += entity.getEffect(MobEffectRegistry.RAISE_DEAD_TIMER.get()).getAmplifier() + 1;
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.RAISE_DEAD_TIMER.get(), summonTime, effectAmplifier, false, false, true));

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
}
