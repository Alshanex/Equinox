package net.alshanex.equinox.util;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.block.pedestal.PedestalBlock;
import io.redspace.ironsspellbooks.block.pedestal.PedestalTile;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.compat.Curios;
import net.alshanex.equinox.fame.SolarianFameProvider;
import net.alshanex.equinox.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

    public static Boolean isSculkBlock(Block block){
        return block instanceof SculkBlock || block instanceof SculkCatalystBlock || block instanceof SculkVeinBlock
                || block instanceof SculkSensorBlock || block instanceof SculkShriekerBlock;
    }

    public static Set<Block> nonSwappableBlocks(){
        Set<Block> excludedBlocks = new HashSet<>();

        for (Block block : ForgeRegistries.BLOCKS) {
            if (block instanceof TrapDoorBlock || block instanceof DoorBlock || block instanceof SignBlock || block instanceof BedBlock
                    || block instanceof AbstractBannerBlock || block instanceof AbstractCandleBlock || block == Blocks.GLASS_PANE || block instanceof StainedGlassPaneBlock
                    || block instanceof AbstractChestBlock<?> || block instanceof BarrierBlock || block instanceof AbstractSkullBlock
                    || block instanceof CoralBlock || block instanceof CoralFanBlock || block instanceof CoralPlantBlock
                    || block instanceof CoralWallFanBlock || block instanceof PressurePlateBlock || block instanceof RailBlock
                    || block instanceof BeehiveBlock || block instanceof BellBlock || block instanceof BrewingStandBlock
                    || block instanceof ButtonBlock || block instanceof CampfireBlock || block instanceof ChorusPlantBlock
                    || block instanceof StructureBlock || block instanceof CakeBlock || block instanceof CommandBlock
                    || block instanceof ShulkerBoxBlock || block instanceof EnchantmentTableBlock || block instanceof CactusBlock
                    || block instanceof BeaconBlock || block instanceof FlowerPotBlock || block instanceof EndGatewayBlock
                    || block instanceof EndPortalBlock || block instanceof EndRodBlock || block instanceof NetherPortalBlock
                    || block instanceof EndPortalFrameBlock || block instanceof RedStoneWireBlock || block instanceof HopperBlock
                    || block instanceof ComparatorBlock || block instanceof RepeaterBlock || block instanceof VineBlock
                    || block instanceof SpawnerBlock || block instanceof LadderBlock || block instanceof RespawnAnchorBlock
                    || block instanceof LeverBlock || block instanceof DaylightDetectorBlock || block instanceof ChorusFlowerBlock
                    || block instanceof WaterlilyBlock || block instanceof SculkShriekerBlock || block instanceof TripWireHookBlock
                    || block instanceof SculkSensorBlock || block instanceof DragonEggBlock || block instanceof TripWireBlock
                    || block instanceof TallSeagrassBlock || block instanceof  KelpPlantBlock || block instanceof TwistingVinesBlock
                    || block instanceof BambooStalkBlock || block instanceof BambooSaplingBlock || block instanceof SugarCaneBlock) {
                excludedBlocks.add(block);
            }
        }

        return excludedBlocks;
    }

    public static Set<Block> swappableBlocks(){
        Set<Block> excludedBlocks = new HashSet<>();

        for (Block block : ForgeRegistries.BLOCKS) {
            if (block instanceof TorchBlock || block instanceof CarpetBlock || block instanceof SnowLayerBlock
                    || block instanceof BushBlock || block instanceof TallGrassBlock || block instanceof GlowLichenBlock) {
                excludedBlocks.add(block);
            }
        }

        return excludedBlocks;
    }

    public static float getTentaclesSpellPower(int spellLevel, @Nullable Entity sourceEntity) {

        double entitySpellPowerModifier = 1;
        double entitySchoolPowerModifier = 1;

        float configPowerModifier = (float) ServerConfigs.getSpellConfig(SpellRegistry.SCULK_TENTACLES_SPELL.get()).powerMultiplier();

        if (sourceEntity instanceof LivingEntity livingEntity) {
            entitySpellPowerModifier = (float) livingEntity.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            entitySchoolPowerModifier = SpellRegistry.SCULK_TENTACLES_SPELL.get().getSchoolType().getPowerFor(livingEntity);
        }

        return (float) ((8 + 3 * (spellLevel - 1)) * entitySpellPowerModifier * entitySchoolPowerModifier * configPowerModifier);
    }

    public static float getFireboltSpellPower(int spellLevel, @Nullable Entity sourceEntity) {

        double entitySpellPowerModifier = 1;
        double entitySchoolPowerModifier = 1;

        float configPowerModifier = (float) ServerConfigs.getSpellConfig(net.alshanex.equinox.registry.SpellRegistry.BOUNCING_FIREBOLT.get()).powerMultiplier();

        if (sourceEntity instanceof LivingEntity livingEntity) {
            entitySpellPowerModifier = (float) livingEntity.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            entitySchoolPowerModifier = SpellRegistry.SCULK_TENTACLES_SPELL.get().getSchoolType().getPowerFor(livingEntity);
        }

        return (float) ((12 + (spellLevel - 1)) * entitySpellPowerModifier * entitySchoolPowerModifier * configPowerModifier);
    }

    public static Vec3 getRandomPositionWithinRadius(double radius) {
        double angle = Math.random() * 2 * Math.PI;
        double distance = Math.random() * radius;
        double x = distance * Math.cos(angle);
        double z = distance * Math.sin(angle);
        return new Vec3(x, 0, z);
    }
}
