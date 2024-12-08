package net.alshanex.equinox.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.block.pedestal.PedestalTile;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import io.redspace.ironsspellbooks.network.spell.ClientboundParticleShockwave;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.Log;
import io.redspace.ironsspellbooks.util.ModTags;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.util.CylinderParticleManager;
import net.alshanex.equinox.util.EUtils;
import net.alshanex.equinox.util.RitualHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class RitualSpell extends AbstractSpell {
    private PedestalTile ritualPedestal;
    private ParticleOptions particles;
    private Item ritualResult;
    private List<PedestalTile> surroundingPedestals = new ArrayList<>();
    private int counter = 0;

    private final ResourceLocation spellId = new ResourceLocation(EquinoxMod.MODID, "orb_ritual");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(35)
            .build();

    public RitualSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 1;
        this.castTime = 200;
        this.baseManaCost = 5;
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        boolean isValidRitual = false;
        boolean validPedestal = RitualHelper.pedestalChecker(entity, 10);
        if(validPedestal){
            PedestalTile pedestal = RitualHelper.getPedestal(entity, 10);
            if(pedestal != null){
                isValidRitual = RitualHelper.isValidRecipe(level, entity, pedestal);
                if(isValidRitual){
                    this.ritualPedestal = pedestal;
                    this.ritualResult = RitualHelper.getOrbForRecipe(level, this.ritualPedestal);
                    this.particles = RitualHelper.getParticleForRitual(this.ritualResult);
                    this.surroundingPedestals = RitualHelper.getSurroundingPedestals(this.ritualPedestal, level);
                }
            }
        }

        return isValidRitual;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        this.counter++;
        if (playerMagicData != null && (playerMagicData.getCastDurationRemaining() + 1) % 10 == 0 && this.counter < 180) {
            for (PedestalTile pedestal : this.surroundingPedestals) {
                MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SchoolRegistry.EVOCATION.get().getTargetingColor(), 1f), pedestal.getBlockPos().getX() + .5f, pedestal.getBlockPos().getY() + 1f, pedestal.getBlockPos().getZ() + .5f, 1, 0, 0, 0, 0, true);
                CylinderParticleManager.spawnParticlesAtPos(level, pedestal.getBlockPos().above(), 30, this.particles, CylinderParticleManager.ParticleDirection.UPWARD, .5f, 1, 0);
            }
            MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SchoolRegistry.EVOCATION.get().getTargetingColor(), 1f), this.ritualPedestal.getBlockPos().getX() + .5f, this.ritualPedestal.getBlockPos().getY() + 1f, this.ritualPedestal.getBlockPos().getZ() + .5f, 1, 0, 0, 0, 0, true);
            CylinderParticleManager.spawnParticlesAtPos(level, this.ritualPedestal.getBlockPos().above(), 10, this.particles, CylinderParticleManager.ParticleDirection.UPWARD, .1f, 2, 0);
        }
        if(this.counter == 180){
            completeRitual(entity);
        }
    }

    @Override
    public void onServerCastComplete(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        this.ritualPedestal = null;
        this.ritualResult = null;
        this.particles = null;
        this.surroundingPedestals = new ArrayList<>();
        this.counter = 0;
        super.onServerCastComplete(level, spellLevel, entity, playerMagicData, cancelled);
    }

    public void completeRitual(LivingEntity caster) {
        for (PedestalTile pedestal : this.surroundingPedestals) {
            pedestal.setHeldItem(ItemStack.EMPTY);
            if(caster instanceof ServerPlayer player){
                ClientboundBlockEntityDataPacket updatedPacket = pedestal.getUpdatePacket();
                if(updatedPacket != null){
                    player.connection.send(updatedPacket);
                }
            }
        }
        this.ritualPedestal.setHeldItem(new ItemStack(this.ritualResult));
        if(caster instanceof ServerPlayer player){
            ClientboundBlockEntityDataPacket updatedPacket = this.ritualPedestal.getUpdatePacket();
            if(updatedPacket != null){
                player.connection.send(updatedPacket);
            }
        }
    }
}
