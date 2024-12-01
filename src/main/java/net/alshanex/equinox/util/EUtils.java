package net.alshanex.equinox.util;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.alshanex.equinox.compat.Curios;
import net.alshanex.equinox.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
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
}
