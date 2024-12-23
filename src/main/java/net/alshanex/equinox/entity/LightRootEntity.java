package net.alshanex.equinox.entity;

import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import net.alshanex.equinox.registry.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class LightRootEntity extends RootEntity {
    public LightRootEntity(Level level, LivingEntity owner) {
        this(EntityRegistry.LIGHT_ROOT.get(), level);
        setOwner(owner);
    }

    public LightRootEntity(EntityType<? extends RootEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
