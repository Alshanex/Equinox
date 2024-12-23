package net.alshanex.equinox.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.guiding_bolt.GuidingBoltProjectile;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.LightRootEntity;
import net.alshanex.equinox.util.CylinderParticleManager;
import net.alshanex.equinox.util.SpellCastAnimator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class HeavenJudgementSpell extends AbstractSpell {
    private int i = 0;
    private float radius;
    private List<GuidingBoltProjectile> bolts;
    private List<BlockPos> positions;
    private boolean spawned = false;
    private LivingEntity target;

    private final ResourceLocation spellId = new ResourceLocation(EquinoxMod.MODID, "heaven_judgement");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getCastTime(spellLevel), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(50)
            .build();

    public HeavenJudgementSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
        this.castTime = 170 - 5;
        this.baseManaCost = 5;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return castTime + 5 * spellLevel;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
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
        return Optional.of(SoundEvents.EVOKER_PREPARE_ATTACK);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellCastAnimator.PRAY_ANIMATION;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 10, .35f);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!spawned && playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) world);
            if (target != null && !target.getType().is(ModTags.CANT_ROOT)) {
                spawned = true;
                this.target = target;
                Vec3 spawn = target.position();
                LightRootEntity rootEntity = new LightRootEntity(world, entity);
                rootEntity.setDuration(getCastTime(spellLevel));
                rootEntity.setTarget(target);
                rootEntity.moveTo(spawn);
                world.addFreshEntity(rootEntity);
                target.stopRiding();
                target.startRiding(rootEntity, true);

                bolts = new ArrayList<>();
                positions = new ArrayList<>();
                radius = Math.max(4f, target.getBbWidth() / 2 + 1f);
                positions = getCircleEdgePositions(target.blockPosition(), radius);
            }
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if (playerMagicData != null && (playerMagicData.getCastDurationRemaining() + 1) % 10 == 0 && target != null) {
            CylinderParticleManager.spawnParticles(level, target, 150, ParticleRegistry.WISP_PARTICLE.get(), CylinderParticleManager.ParticleDirection.UPWARD, radius + 1f, 1, 0);
        }

        if (playerMagicData != null && (playerMagicData.getCastDurationRemaining() + 1) % 20 == 0) {
            BlockPos pos = positions.get(i);
            GuidingBoltProjectile guidingBolt = new GuidingBoltProjectile(level, entity);
            bolts.add(guidingBolt);
            guidingBolt.setPos(pos.getCenter());
            guidingBolt.setDamage(0);
            level.addFreshEntity(guidingBolt);
            i++;
        }
    }

    @Override
    public void onServerCastComplete(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        for(GuidingBoltProjectile bolt : bolts){
            level.playSound(null, bolt.getX(), bolt.getY(), bolt.getZ(), SoundRegistry.GUIDING_BOLT_IMPACT.get(), SoundSource.PLAYERS, 2, Utils.random.nextIntBetweenInclusive(8, 12) * .1f);
            MagicManager.spawnParticles(level, ParticleHelper.WISP, bolt.getX(), bolt.getY(), bolt.getZ(), 25, 0, 0, 0, .18, true);

            bolt.discard();
        }
        target = null;
        spawned = false;
        bolts.clear();
        positions.clear();
        i = 0;
        radius = 0;
        super.onServerCastComplete(level, spellLevel, entity, playerMagicData, cancelled);
    }

    public static List<BlockPos> getCircleEdgePositions(BlockPos center, double radius) {
        List<BlockPos> positions = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            double angle = 2 * Math.PI * i / 8;
            int x = center.getX() + (int) Math.round(radius * Math.cos(angle));
            int z = center.getZ() + (int) Math.round(radius * Math.sin(angle));
            positions.add(new BlockPos(x, center.getY(), z));
        }
        return positions;
    }
}
