package net.alshanex.equinox.event;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.item.Orb;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;

@Mod.EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void getBonusSpells(SpellSelectionManager.SpellSelectionEvent event) {
        var player = event.getEntity();
        CuriosApi.getCuriosInventory(player).ifPresent(a -> {
            var list = a.findCurios(item -> item != null && ISpellContainer.isSpellContainer(item) && item.getItem() instanceof Orb);
            for (var i : list) {
                var spellContainer = i.stack() != null ? ISpellContainer.get(i.stack()) : null;
                if (spellContainer != null) {
                    var spells = spellContainer.getAllSpells();
                    if (spells != null && !Arrays.stream(spells).toList().isEmpty()) {
                        int initialIndex = event.getManager().getSpellCount();
                        for(int spellIndex = initialIndex; spellIndex < initialIndex + spells.length; spellIndex++){
                            var spell = spells[spellIndex - initialIndex];
                            if (spell == null || spell.getSpell() == null) {
                                return;
                            }
                            event.addSelectionOption(new SpellData(spell.getSpell(), spell.getLevel(), true), i.stack().getItem().getDescriptionId(), spellIndex);
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerCastEvent(SpellPreCastEvent event){
        var entity = event.getEntity();
        if(entity instanceof ServerPlayer player && !player.level().isClientSide()){
            boolean isSpellRejected = EUtils.isSpellRejected(player, event.getSchoolType());
            if(isSpellRejected){
                float backfireDamage = player.getMaxHealth() * .10f;
                player.hurt(player.level().damageSources().drown(), backfireDamage);
                event.setCanceled(true);
            }
        }
    }
}
