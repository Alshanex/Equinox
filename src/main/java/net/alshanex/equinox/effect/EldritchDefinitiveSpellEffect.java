package net.alshanex.equinox.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.alshanex.equinox.util.CylinderParticleManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class EldritchDefinitiveSpellEffect extends MagicMobEffect {

    public EldritchDefinitiveSpellEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        CylinderParticleManager.spawnParticles(pLivingEntity.level(), pLivingEntity, 1, ParticleTypes.SCULK_SOUL, CylinderParticleManager.ParticleDirection.UPWARD, 1, 1, 0);
        pLivingEntity.level().getEntities(pLivingEntity, pLivingEntity.getBoundingBox().inflate(5, 4, 5), (target) -> !DamageSources.isFriendlyFireBetween(target, pLivingEntity) && Utils.hasLineOfSight(pLivingEntity.level(), pLivingEntity, target, true)).forEach(target -> {
            if (target instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(pLivingEntity) < 5 * 5 && !DamageSources.isFriendlyFireBetween(target, pLivingEntity)) {
                if(!livingEntity.hasEffect(MobEffects.DARKNESS)){
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20, 0, false, false));
                }
            }
        });
    }
}
