package net.alshanex.equinox.entity;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import net.alshanex.equinox.registry.EntityRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class LightRootEntity extends RootEntity {
    private float radius;
    List<Entity> trackingEntities = new ArrayList<>();

    public LightRootEntity(Level level, LivingEntity owner, float radius) {
        this(EntityRegistry.LIGHT_ROOT.get(), level);
        setOwner(owner);
        this.radius = radius * 2;
    }

    public LightRootEntity(EntityType<? extends RootEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();

        int update = Math.max((int) (radius / 2), 2);
        if (tickCount % update == 0) {
            updateTrackingEntities();
        }
        var bb = this.getBoundingBox();
        for (Entity entity : trackingEntities) {
            if (entity != getOwner() && !DamageSources.isFriendlyFireBetween(getOwner(), entity)) {
                Vec3 center = new Vec3(bb.getCenter().x, bb.getCenter().y, bb.getCenter().z);
                float distance = (float) center.distanceTo(entity.position());
                if (distance > radius) {
                    continue;
                }
                float f = 1 - distance / radius;
                float scale = f * f * f * f * .25f;

                Vec3 diff = center.subtract(entity.position()).scale(scale);
                entity.push(diff.x, diff.y, diff.z);
                entity.fallDistance = 0;
            }
        }
    }

    private void updateTrackingEntities() {
        trackingEntities = level().getEntities(this, this.getBoundingBox().inflate(radius, 5, radius), (target) -> !(target instanceof Projectile));
    }
}
