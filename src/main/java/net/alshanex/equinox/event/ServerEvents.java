package net.alshanex.equinox.event;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.HealingAoe;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.datagen.EntityTagGenerator;
import net.alshanex.equinox.fame.*;
import net.alshanex.equinox.item.ModItems;
import net.alshanex.equinox.item.Orb;
import net.alshanex.equinox.network.*;
import net.alshanex.equinox.util.EUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkVeinBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
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
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.player instanceof ServerPlayer player){
            if(EUtils.hasItemInOrbSlot(player, ModItems.PLASMATIC_ORB.get())){
                player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                    if(fame.getFame() >= 700){
                        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 0, false, false));
                    }
                });
            }
            if(EUtils.hasItemInOrbSlot(player, ModItems.OBSCURE_ORB.get())){
                player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                    if(fame.getFame() >= 700){
                        BlockPos currentBlockPos = player.blockPosition();
                        BlockPos blockBelowPos = player.blockPosition().below();
                        Block blockBelow = player.level().getBlockState(blockBelowPos).getBlock();
                        Block currentBlock = player.level().getBlockState(currentBlockPos).getBlock();
                        if(EUtils.isSculkBlock(blockBelow) || currentBlock instanceof SculkVeinBlock){
                            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2, 1, false, false));
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, 0, false, false));
                            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 0, false, false));
                        }
                        if(player.hasEffect(MobEffects.BLINDNESS) || player.hasEffect(MobEffects.DARKNESS)){
                            player.removeEffect(MobEffects.DARKNESS);
                            player.removeEffect(MobEffects.BLINDNESS);
                        }
                    }
                    if(fame.getFame() >= 900){
                        if(!player.level().isClientSide){
                            double radius = 10.0;

                            AABB expandedArea = player.getBoundingBox().inflate(radius, radius, radius);

                            player.level().getEntitiesOfClass(Warden.class, expandedArea).forEach(warden -> {
                                if (warden.getTarget() == player) {
                                    warden.setTarget(null);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttackEvent(LivingHurtEvent event){
        if(event.getEntity() != null){
            if(event.getEntity() instanceof ServerPlayer player){
                if(EUtils.hasItemInOrbSlot(player, ModItems.CORRUPTED_ORB.get())){
                    player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                        if(fame.getFame() >= 700){
                            if(event.getSource().getEntity() instanceof LivingEntity entity){
                                double random = Math.random();

                                if (random < 0.05) {
                                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
                                }

                                random = Math.random();

                                if (random < 0.05) {
                                    entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
                                }
                            }
                        }
                    });
                }
                if(EUtils.hasItemInOrbSlot(player, ModItems.PLASMATIC_ORB.get())){
                    player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                        if(fame.getFame() >= 900){
                            if(event.getSource().typeHolder().is(ISSDamageTypes.FIRE_MAGIC) || event.getSource().typeHolder().is(ISSDamageTypes.FIRE_FIELD)) {
                                event.setCanceled(true);
                            }
                        }
                    });
                }
            }
        }
        if(event.getSource().getEntity() != null){
            if(event.getSource().getEntity() instanceof ServerPlayer player){
                if(EUtils.hasItemInOrbSlot(player, ModItems.CORRUPTED_ORB.get())){
                    player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                        if(fame.getFame() >= 500){
                            double random = Math.random();

                            if (random < 0.05) {
                                event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
                            }

                            random = Math.random();

                            if (random < 0.05) {
                                event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
                            }
                        }
                    });
                }
                if(EUtils.hasItemInOrbSlot(player, ModItems.BLESSED_ORB.get())){
                    player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                        if(fame.getFame() >= 500){
                            double random = Math.random();

                            if (random < 0.05) {
                                HealingAoe aoeEntity = new HealingAoe(player.level());
                                aoeEntity.setOwner(player);
                                aoeEntity.setCircular();
                                aoeEntity.setRadius(3);
                                aoeEntity.setDuration(100);
                                aoeEntity.setDamage(event.getAmount() * .05f);
                                aoeEntity.setPos(player.position());
                                player.level().addFreshEntity(aoeEntity);

                                TargetedAreaEntity visualEntity = TargetedAreaEntity.createTargetAreaEntity(player.level(), player.position(), 3, 0xc80000);
                                visualEntity.setDuration(100);
                                visualEntity.setOwner(aoeEntity);
                            }
                        }
                    });
                }
                if(EUtils.hasItemInOrbSlot(player, ModItems.OBSCURE_ORB.get())){
                    player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                        if(fame.getFame() >= 500){
                            double random = Math.random();

                            if (random <= 0.5) {
                                event.getEntity().addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
                            }
                        }
                    });
                }
                if(EUtils.hasItemInOrbSlot(player, ModItems.PLASMATIC_ORB.get())){
                    player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                        if(fame.getFame() >= 500){
                            double random = Math.random();

                            if (random <= 0.5) {
                                event.getEntity().setRemainingFireTicks(80);
                            }
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if(event.getEntity() != null){
            if(event.getEntity() instanceof ServerPlayer player){
                if(EUtils.hasItemInOrbSlot(player, ModItems.BLESSED_ORB.get())){
                    player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                        if(fame.getFame() >= 700){
                            event.setCanceled(true);
                            player.setHealth(player.getMaxHealth());
                            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 4, false, false));
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 0, false, false));
                            fame.subFame((int) (fame.getFame() * .25));
                            ModPackets.sendToPlayer(new SyncCelestialFamePackage(fame.getFame()), player);
                        }
                    });
                }
            }
        }

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
