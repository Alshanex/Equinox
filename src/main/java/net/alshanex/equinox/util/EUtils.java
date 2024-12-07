package net.alshanex.equinox.util;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.block.pedestal.PedestalBlock;
import io.redspace.ironsspellbooks.block.pedestal.PedestalTile;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.alshanex.equinox.compat.Curios;
import net.alshanex.equinox.fame.SolarianFameProvider;
import net.alshanex.equinox.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;

public class EUtils {
    public static boolean hasItemInOrbSlot(ServerPlayer player, Item itemToCheck) {
        return CuriosApi.getCuriosInventory(player).map(inventory ->
                inventory.getStacksHandler(Curios.ORB_SLOT).map(stacksHandler -> {
                    for (int i = 0; i < stacksHandler.getSlots(); i++) {
                        if (stacksHandler.getStacks().getStackInSlot(i).is(itemToCheck)) {
                            return true;
                        }
                    }
                    return false;
                }).orElse(false)
        ).orElse(false);
    }

    public static boolean hasItemInOrbSlotLocal(Player player, Item itemToCheck) {
        return CuriosApi.getCuriosInventory(player).map(inventory ->
                inventory.getStacksHandler(Curios.ORB_SLOT).map(stacksHandler -> {
                    for (int i = 0; i < stacksHandler.getSlots(); i++) {
                        if (stacksHandler.getStacks().getStackInSlot(i).is(itemToCheck)) {
                            return true;
                        }
                    }
                    return false;
                }).orElse(false)
        ).orElse(false);
    }

    public static boolean noItemInOrbSlot(Player player) {
        return !(hasItemInOrbSlotLocal(player, ModItems.PLASMATIC_ORB.get()) || hasItemInOrbSlotLocal(player, ModItems.BLESSED_ORB.get())
                || hasItemInOrbSlotLocal(player, ModItems.CORRUPTED_ORB.get()) || hasItemInOrbSlotLocal(player, ModItems.OBSCURE_ORB.get()));
    }

    public static boolean isSpellRejected(ServerPlayer player, SchoolType spellSchoolType){
        if(EUtils.hasItemInOrbSlot(player, ModItems.CORRUPTED_ORB.get()) && spellSchoolType == SchoolRegistry.HOLY.get()){
            player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("tooltip.equinox.reject_holy_spells")
                    .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
            return true;
        }
        if(EUtils.hasItemInOrbSlot(player, ModItems.BLESSED_ORB.get()) && spellSchoolType == SchoolRegistry.BLOOD.get()){
            player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("tooltip.equinox.reject_blood_spells")
                    .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
            return true;
        }
        if(EUtils.hasItemInOrbSlot(player, ModItems.OBSCURE_ORB.get()) && spellSchoolType == SchoolRegistry.FIRE.get()){
            player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("tooltip.equinox.reject_fire_spells")
                    .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
            return true;
        }
        if(EUtils.hasItemInOrbSlot(player, ModItems.PLASMATIC_ORB.get()) && spellSchoolType == SchoolRegistry.ELDRITCH.get()){
            player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("tooltip.equinox.reject_eldritch_spells")
                    .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
            return true;
        }
        return false;
    }

    public static boolean isFallenFaction(EntityType entityType){
        return entityType == EntityType.SKELETON_HORSE || entityType == EntityType.SKELETON ||
                entityType == EntityType.WITHER_SKELETON || entityType == EntityType.ZOMBIFIED_PIGLIN ||
                entityType == EntityType.ZOMBIE || entityType == EntityType.DROWNED || entityType == EntityType.HUSK ||
                entityType == EntityType.ZOMBIE_VILLAGER || entityType == EntityType.ZOGLIN || entityType == EntityType.STRAY ||
                entityType == EntityRegistry.SUMMONED_SKELETON.get() || entityType == EntityRegistry.SUMMONED_ZOMBIE.get() ||
                entityType == EntityRegistry.CATACOMBS_ZOMBIE.get() || entityType == EntityRegistry.NECROMANCER.get() ||
                entityType == EntityRegistry.KEEPER.get();
    }

    public static boolean isFallenFactionBoss(EntityType entityType){
        return entityType == EntityRegistry.DEAD_KING.get() || entityType == EntityType.WITHER;
    }

    public static boolean isCelestialFaction(EntityType entityType){
        return entityType == EntityType.VILLAGER || entityType == EntityType.IRON_GOLEM || entityType == EntityType.ALLAY
                || entityType == EntityRegistry.PRIEST.get();
    }

    public static boolean isUmbrakithFaction(EntityType entityType){
        return entityType == EntityType.WITCH || entityType == EntityType.SILVERFISH || entityType == EntityType.PHANTOM
                || entityType == EntityType.GUARDIAN;
    }

    public static boolean isUmbrakithFactionBoss(EntityType entityType){
        return entityType == EntityType.WARDEN || entityType == EntityType.ELDER_GUARDIAN;
    }

    public static boolean isSolarianFaction(EntityType entityType){
        return entityType == EntityType.BLAZE || entityType == EntityType.PIGLIN || entityType == EntityType.PIGLIN_BRUTE ||
                entityType == EntityType.GHAST || entityType == EntityType.MAGMA_CUBE ||
                entityType == EntityRegistry.APOTHECARIST.get() || entityType == EntityRegistry.PYROMANCER.get();
    }

    public static BlockEntity raycastBlockEntity(Level level, LivingEntity entity, double distance) {

        Vec3 start = entity.getEyePosition(1.0F);

        Vec3 direction = entity.getViewVector(1.0F);

        Vec3 end = start.add(direction.scale(distance));

        BlockHitResult hitResult = level.clip(new net.minecraft.world.level.ClipContext(
                start,
                end,
                net.minecraft.world.level.ClipContext.Block.COLLIDER,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                entity
        ));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockEntity blockEntity = level.getBlockEntity(blockPos);

            if (blockEntity != null) {
                return blockEntity;
            }
        }

        return null;
    }
}
