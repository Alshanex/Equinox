package net.alshanex.equinox.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.alshanex.equinox.entity.EldritchClone;
import net.alshanex.equinox.util.CylinderParticleManager;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EldritchDefinitiveSpellEffect extends MagicMobEffect {
    private int tickCounter = 0;

    public EldritchDefinitiveSpellEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        tickCounter++;

        CylinderParticleManager.spawnParticles(pLivingEntity.level(), pLivingEntity, 1, ParticleTypes.SCULK_SOUL, CylinderParticleManager.ParticleDirection.UPWARD, 1, 1, 0);
        pLivingEntity.level().getEntities(pLivingEntity, pLivingEntity.getBoundingBox().inflate(5, 4, 5), (target) -> !DamageSources.isFriendlyFireBetween(target, pLivingEntity) && Utils.hasLineOfSight(pLivingEntity.level(), pLivingEntity, target, true)).forEach(target -> {
            if (target instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(pLivingEntity) < 5 * 5 && !DamageSources.isFriendlyFireBetween(target, pLivingEntity)) {
                if(!livingEntity.hasEffect(MobEffects.DARKNESS)){
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20, 0, false, false));
                }
            }
        });

        if (!pLivingEntity.level().isClientSide() && tickCounter >= 20) {
            spawnAnotherClone(pLivingEntity);
            tickCounter = 0;
        }
    }

    private void spawnAnotherClone(LivingEntity pLivingEntity){
        pLivingEntity.level().getEntitiesOfClass(LivingEntity.class, pLivingEntity.getBoundingBox().inflate(10, 5, 10), (target) -> !DamageSources.isFriendlyFireBetween(target, pLivingEntity) && Utils.hasLineOfSight(pLivingEntity.level(), pLivingEntity, target, true) && (target instanceof Mob || target instanceof ServerPlayer)).forEach(target -> {
            List<EldritchClone> cloneList = pLivingEntity.level().getEntitiesOfClass(EldritchClone.class, pLivingEntity.getBoundingBox().inflate(20, 10, 20), (clone) -> clone.getSummoner() != null && clone.getSummoner() == pLivingEntity && clone.getCachedTarget() != null && clone.getCachedTarget() == target).stream().toList();
            if(cloneList.isEmpty()){
                Vec3 randomOffset = EUtils.getRandomPositionWithinRadius(10);
                Vec3 spawn = pLivingEntity.position().add(randomOffset);

                spawn = Utils.moveToRelativeGroundLevel(pLivingEntity.level(), spawn, 8);
                if (!pLivingEntity.level().getBlockState(BlockPos.containing(spawn).below()).isAir()) {
                    EldritchClone clone = new EldritchClone(pLivingEntity.level(), pLivingEntity, target);

                    clone.setPos(spawn.x, spawn.y, spawn.z);
                    clone.finalizeSpawn((ServerLevel) pLivingEntity.level(), pLivingEntity.level().getCurrentDifficultyAt(pLivingEntity.blockPosition()), MobSpawnType.TRIGGERED, null, null);
                    pLivingEntity.level().addFreshEntity(clone);
                    clone.getAngerManagement().increaseAnger(target, 150);
                    clone.setAttackTarget(target);
                }
            }
        });
    }
}
