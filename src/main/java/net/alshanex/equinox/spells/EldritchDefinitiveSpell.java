package net.alshanex.equinox.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.eldritch.AbstractEldritchSpell;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.entity.EldritchClone;
import net.alshanex.equinox.fame.UmbrakithFameProvider;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.registry.EffectRegistry;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@AutoSpellConfig
public class EldritchDefinitiveSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(EquinoxMod.MODID, "definitive_eldritch_spell");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(60)
            .build();

    public EldritchDefinitiveSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 3;
        this.castTime = 20;
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
        return Optional.of(SoundRegistry.VOID_TENTACLES_START.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.VOID_TENTACLES_FINISH.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        AtomicInteger umbrakithFame = new AtomicInteger();
        if(entity instanceof ServerPlayer player){
            if(EUtils.hasItemInOrbSlot(player, ModItems.OBSCURE_ORB.get())){
                player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                    umbrakithFame.set(fame.getFame());
                });
            }
        }
        if(umbrakithFame.get() >= 900){ return true;}
        if(entity instanceof ServerPlayer player){
            player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.equinox.cant_cast_spell_fame").withStyle(ChatFormatting.RED)));
        }
        return false;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        BlockPos center = entity.blockPosition();

        createSculk(center, level);

        entity.addEffect(new MobEffectInstance(EffectRegistry.ELDRITCH_DEFINITIVE.get(), 600, 0, false, false));

        summonWardens(level, entity, center);

        level.gameEvent(null, GameEvent.ENTITY_ROAR, center);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void summonWardens(Level level, LivingEntity entity, BlockPos center){
        level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(10, 4, 10), (target) -> !DamageSources.isFriendlyFireBetween(target, entity) && Utils.hasLineOfSight(level, entity, target, true)).forEach(target -> {
            if (target.distanceToSqr(entity) < 10 * 10 && !DamageSources.isFriendlyFireBetween(target, entity)) {
                Vec3 random = new Vec3(Utils.getRandomScaled(1), Utils.getRandomScaled(1), Utils.getRandomScaled(1));
                Vec3 spawn = entity.position().add(new Vec3(0, 0, 1.3).yRot(((6.281f / 5) * 5))).add(random);

                spawn = Utils.moveToRelativeGroundLevel(level, spawn, 8);
                if (!level.getBlockState(BlockPos.containing(spawn).below()).isAir()) {
                    EldritchClone clone = new EldritchClone(level, entity, target);

                    clone.setPos(spawn.x, spawn.y, spawn.z);
                    clone.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(center), MobSpawnType.TRIGGERED, null, null);
                    level.addFreshEntity(clone);
                    clone.getAngerManagement().increaseAnger(target, 150);
                    clone.setAttackTarget(target);
                }
            }
        });
    }

    private void createSculk(BlockPos center, Level level){
        int radius = 10;
        Set<Block> excludedBlocks = EUtils.nonSwappableBlocks();
        Set<Block> swappableBlocks = EUtils.swappableBlocks();

        for (int x = (int) Math.floor(-radius); x <= Math.ceil(radius); x++) {
            for (int y = -6; y <= 6; y++) {
                for (int z = (int) Math.floor(-radius); z <= Math.ceil(radius); z++) {
                    BlockPos currentPos = center.offset(x, y, z);
                    var blockState = level.getBlockState(currentPos);

                    double distance = Math.sqrt(x * x + z * z);

                    if (distance <= radius && !excludedBlocks.contains(blockState.getBlock()) && !swappableBlocks.contains(blockState.getBlock())
                            && blockState.getFluidState().getType() == Fluids.EMPTY && !blockState.isAir()) {

                        for (Direction direction : Direction.values()) {
                            BlockPos adjacentPos = currentPos.relative(direction);
                            var adjacentState = level.getBlockState(adjacentPos);

                            if (adjacentState.isAir() || swappableBlocks.contains(adjacentState.getBlock())) {
                                BlockState sculkSurface = Blocks.SCULK_VEIN.defaultBlockState()
                                        .setValue(MultifaceBlock.getFaceProperty(direction.getOpposite()), true);

                                level.setBlockAndUpdate(adjacentPos, sculkSurface);
                            } else if (adjacentState.getBlock() == Blocks.SCULK_VEIN) {
                                BlockState updatedState = adjacentState.setValue(MultifaceBlock.getFaceProperty(direction.getOpposite()), true);
                                level.setBlockAndUpdate(adjacentPos, updatedState);
                            }
                        }
                    }
                }
            }
        }
    }
}
