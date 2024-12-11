package net.alshanex.equinox.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
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
        if(!pLivingEntity.hasEffect(MobEffects.INVISIBILITY)){
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20, 0, false, false));
        }
        pLivingEntity.level().getEntities(pLivingEntity, pLivingEntity.getBoundingBox().inflate(10, 4, 10), (target) -> !DamageSources.isFriendlyFireBetween(target, pLivingEntity) && Utils.hasLineOfSight(pLivingEntity.level(), pLivingEntity, target, true)).forEach(target -> {
            if (target instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(pLivingEntity) < 10 * 10 && !DamageSources.isFriendlyFireBetween(target, pLivingEntity)) {
                if(!livingEntity.hasEffect(MobEffects.DARKNESS)){
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20, 0, false, false));
                }
            }
        });
    }
}
