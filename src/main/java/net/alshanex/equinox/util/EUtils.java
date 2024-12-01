package net.alshanex.equinox.util;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.alshanex.equinox.compat.Curios;
import net.alshanex.equinox.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

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
}
