package net.alshanex.equinox.event;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.datagen.EntityTagGenerator;
import net.alshanex.equinox.fame.*;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.item.Orb;
import net.alshanex.equinox.network.*;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = EquinoxMod.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player){
            if(!event.getObject().getCapability(FallenFameProvider.FALLEN_FAME).isPresent()){
                event.addCapability(new ResourceLocation(EquinoxMod.MODID, "properties/fallen"), new FallenFameProvider());
            }
            if(!event.getObject().getCapability(CelestialFameProvider.CELESTIAL_FAME).isPresent()){
                event.addCapability(new ResourceLocation(EquinoxMod.MODID, "properties/celestial"), new CelestialFameProvider());
            }
            if(!event.getObject().getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).isPresent()){
                event.addCapability(new ResourceLocation(EquinoxMod.MODID, "properties/umbrakith"), new UmbrakithFameProvider());
            }
            if(!event.getObject().getCapability(SolarianFameProvider.SOLARIAN_FAME).isPresent()){
                event.addCapability(new ResourceLocation(EquinoxMod.MODID, "properties/solarian"), new SolarianFameProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(oldStore -> {
            event.getEntity().getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });

        event.getOriginal().getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(oldStore -> {
            event.getEntity().getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });
        event.getOriginal().getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(oldStore -> {
            event.getEntity().getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });
        event.getOriginal().getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(oldStore -> {
            event.getEntity().getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.register(FallenFame.class);
        event.register(CelestialFame.class);
        event.register(UmbrakithFame.class);
        event.register(SolarianFame.class);
    }

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

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if(event.getSource().getEntity() != null){
            if(event.getSource().getEntity() instanceof ServerPlayer player){

                //Fallen interactions
                if(EUtils.hasItemInOrbSlot(player, ModItems.CORRUPTED_ORB.get())){
                    if(event.getEntity().getType().is(EntityTagGenerator.CELESTIAL_FACTION_ENTITIES)){
                        player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                            fame.addFame(1);
                            ModPackets.sendToPlayer(new SyncFallenFamePackage(fame.getFame()), player);
                        });
                    }
                    if(event.getEntity().getType().is(EntityTagGenerator.CELESTIAL_FACTION_BOSSES)){
                        player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                            fame.addFame(5);
                            ModPackets.sendToPlayer(new SyncFallenFamePackage(fame.getFame()), player);
                        });
                    }
                    if(event.getEntity() instanceof ServerPlayer killedPlayer){
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.BLESSED_ORB.get())){
                            player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                                fame.addFame(10);
                                ModPackets.sendToPlayer(new SyncFallenFamePackage(fame.getFame()), player);
                            });
                            killedPlayer.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                                fame.subFame(5);
                                ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), killedPlayer);
                            });
                        }
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.OBSCURE_ORB.get()) || EUtils.hasItemInOrbSlot(killedPlayer, ModItems.PLASMATIC_ORB.get())){
                            player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                                fame.addFame(3);
                                ModPackets.sendToPlayer(new SyncFallenFamePackage(fame.getFame()), player);
                            });
                            if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.OBSCURE_ORB.get())){
                                killedPlayer.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                                    fame.subFame(1);
                                    ModPackets.sendToPlayer(new SyncUmbrakithFamePackage(fame.getFame()), killedPlayer);
                                });
                            }
                            if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.PLASMATIC_ORB.get())){
                                killedPlayer.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                                    fame.subFame(1);
                                    ModPackets.sendToPlayer(new SyncSolarianFamePackage(fame.getFame()), killedPlayer);
                                });
                            }
                        }
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.CORRUPTED_ORB.get())){
                            player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                                fame.subFame(5);
                                ModPackets.sendToPlayer(new SyncFallenFamePackage(fame.getFame()), player);
                            });
                        }
                    }
                }

                //Celestial interactions
                if(EUtils.hasItemInOrbSlot(player, ModItems.BLESSED_ORB.get())){
                    if(event.getEntity().getType().is(EntityTagGenerator.FALLEN_FACTION_ENTITIES)){
                        player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                            fame.addFame(1);
                            ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), player);
                        });
                    }
                    if(event.getEntity().getType().is(EntityTagGenerator.FALLEN_FACTION_BOSSES)){
                        player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                            fame.addFame(5);
                            ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), player);
                        });
                    }
                    if(event.getEntity() instanceof ServerPlayer killedPlayer){
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.CORRUPTED_ORB.get())){
                            player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                                fame.addFame(10);
                                ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), player);
                            });
                            killedPlayer.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                                fame.subFame(5);
                                ModPackets.sendToPlayer(new SyncFallenFamePackage(fame.getFame()), killedPlayer);
                            });
                        }
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.OBSCURE_ORB.get()) || EUtils.hasItemInOrbSlot(killedPlayer, ModItems.PLASMATIC_ORB.get())){
                            player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                                fame.addFame(3);
                                ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), player);
                            });
                            if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.OBSCURE_ORB.get())){
                                killedPlayer.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                                    fame.subFame(1);
                                    ModPackets.sendToPlayer(new SyncUmbrakithFamePackage(fame.getFame()), killedPlayer);
                                });
                            }
                            if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.PLASMATIC_ORB.get())){
                                killedPlayer.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                                    fame.subFame(1);
                                    ModPackets.sendToPlayer(new SyncSolarianFamePackage(fame.getFame()), killedPlayer);
                                });
                            }
                        }
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.BLESSED_ORB.get())){
                            player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                                fame.subFame(5);
                                ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), player);
                            });
                        }
                    }
                }

                //Umbrakith interactions
                if(EUtils.hasItemInOrbSlot(player, ModItems.OBSCURE_ORB.get())){
                    if(event.getEntity().getType().is(EntityTagGenerator.SOLARIAN_FACTION_ENTITIES)){
                        player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                            fame.addFame(1);
                            ModPackets.sendToPlayer(new SyncUmbrakithFamePackage(fame.getFame()), player);
                        });
                    }
                    if(event.getEntity().getType().is(EntityTagGenerator.SOLARIAN_FACTION_BOSSES)){
                        player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                            fame.addFame(5);
                            ModPackets.sendToPlayer(new SyncUmbrakithFamePackage(fame.getFame()), player);
                        });
                    }
                    if(event.getEntity() instanceof ServerPlayer killedPlayer){
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.PLASMATIC_ORB.get())){
                            player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                                fame.addFame(10);
                                ModPackets.sendToPlayer(new SyncUmbrakithFamePackage(fame.getFame()), player);
                            });
                            killedPlayer.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                                fame.subFame(5);
                                ModPackets.sendToPlayer(new SyncSolarianFamePackage(fame.getFame()), killedPlayer);
                            });
                        }
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.BLESSED_ORB.get()) || EUtils.hasItemInOrbSlot(killedPlayer, ModItems.CORRUPTED_ORB.get())){
                            player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                                fame.addFame(3);
                                ModPackets.sendToPlayer(new SyncUmbrakithFamePackage(fame.getFame()), player);
                            });
                            if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.BLESSED_ORB.get())){
                                killedPlayer.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                                    fame.subFame(1);
                                    ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), killedPlayer);
                                });
                            }
                            if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.CORRUPTED_ORB.get())){
                                killedPlayer.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                                    fame.subFame(1);
                                    ModPackets.sendToPlayer(new SyncFallenFamePackage(fame.getFame()), killedPlayer);
                                });
                            }
                        }
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.OBSCURE_ORB.get())){
                            player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                                fame.subFame(5);
                                ModPackets.sendToPlayer(new SyncUmbrakithFamePackage(fame.getFame()), player);
                            });
                        }
                    }
                }

                //Solarian interactions
                if(EUtils.hasItemInOrbSlot(player, ModItems.PLASMATIC_ORB.get())){
                    if(event.getEntity().getType().is(EntityTagGenerator.UMBRAKITH_FACTION_ENTITIES)){
                        player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                            fame.addFame(1);
                            ModPackets.sendToPlayer(new SyncSolarianFamePackage(fame.getFame()), player);
                        });
                    }
                    if(event.getEntity().getType().is(EntityTagGenerator.UMBRAKITH_FACTION_BOSSES)){
                        player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                            fame.addFame(5);
                            ModPackets.sendToPlayer(new SyncSolarianFamePackage(fame.getFame()), player);
                        });
                    }
                    if(event.getEntity() instanceof ServerPlayer killedPlayer){
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.OBSCURE_ORB.get())){
                            player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                                fame.addFame(10);
                                ModPackets.sendToPlayer(new SyncSolarianFamePackage(fame.getFame()), player);
                            });
                            killedPlayer.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                                fame.subFame(5);
                                ModPackets.sendToPlayer(new SyncUmbrakithFamePackage(fame.getFame()), killedPlayer);
                            });
                        }
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.BLESSED_ORB.get()) || EUtils.hasItemInOrbSlot(killedPlayer, ModItems.CORRUPTED_ORB.get())){
                            player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                                fame.addFame(3);
                                ModPackets.sendToPlayer(new SyncSolarianFamePackage(fame.getFame()), player);
                            });
                            if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.BLESSED_ORB.get())){
                                killedPlayer.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                                    fame.subFame(1);
                                    ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), killedPlayer);
                                });
                            }
                            if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.CORRUPTED_ORB.get())){
                                killedPlayer.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                                    fame.subFame(1);
                                    ModPackets.sendToPlayer(new SyncFallenFamePackage(fame.getFame()), killedPlayer);
                                });
                            }
                        }
                        if(EUtils.hasItemInOrbSlot(killedPlayer, ModItems.PLASMATIC_ORB.get())){
                            player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                                fame.subFame(5);
                                ModPackets.sendToPlayer(new SyncSolarianFamePackage(fame.getFame()), player);
                            });
                        }
                    }
                }
            }
        }
    }
}
