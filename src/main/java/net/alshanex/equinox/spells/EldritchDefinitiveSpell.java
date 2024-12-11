package net.alshanex.equinox.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

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

        entity.addEffect(new MobEffectInstance(EffectRegistry.ELDRITCH_DEFINITIVE.get(), 600, 0, false, false));

        for(int i = 0; i < 2; i++){
            double randomX = entity.getX() + (Math.random() * 2 * radius - radius);
            double randomZ = entity.getZ() + (Math.random() * 2 * radius - radius);
            double randomY = entity.getY();

            EldritchClone clone = new EldritchClone(level, entity);

            if (!level.isClientSide) {
                MagicManager.spawnParticles(level, ParticleTypes.POOF, randomX, randomY, randomZ, 25, .4, .8, .4, .03, false);
            }

            clone.setPos(randomX, randomY, randomZ);
            level.addFreshEntity(clone);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
