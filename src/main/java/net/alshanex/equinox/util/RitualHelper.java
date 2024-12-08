package net.alshanex.equinox.util;

import io.redspace.ironsspellbooks.block.pedestal.PedestalTile;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.alshanex.equinox.Config;
import net.alshanex.equinox.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public class RitualHelper {

    public static boolean pedestalChecker(LivingEntity caster, int range) {
        var block = EUtils.raycastBlockEntity(caster.level(), caster, range);
        if(block != null){
            if (block instanceof PedestalTile) {
                return true;
            } else if (caster instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.equinox.not_valid_pedestal").withStyle(ChatFormatting.RED)));
            }
        }
        return false;
    }

    public static boolean isValidRecipe(Level level, LivingEntity caster, PedestalTile centralPedestal) {

        Map<RitualRecipe, Item> recipes = RitualHelper.getRitualRecipes();

        List<PedestalTile> surroundingPedestals = RitualHelper.getSurroundingPedestals(centralPedestal, level);

        Set<Item> pedestalSet = RitualHelper.getItemsFromPedestals(surroundingPedestals);
        RitualRecipe recipe = new RitualRecipe(centralPedestal.getHeldItem().getItem(), pedestalSet);

        if(recipes.get(recipe) != null){
            return true;
        } else {
            if (caster instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.equinox.not_valid_orb_recipe").withStyle(ChatFormatting.RED)));
            }
            return false;
        }
    }

    public static List<PedestalTile> getSurroundingPedestals(PedestalTile centralPedestal, Level level){
        BlockPos origin = centralPedestal.getBlockPos();

        BlockPos[] offsets = {
                origin.north(2),
                origin.south(2),
                origin.east(2),
                origin.west(2)
        };

        List<PedestalTile> pedestals = new ArrayList<>();

        for (BlockPos pos : offsets) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof PedestalTile pedestal) {
                if(pedestal.getHeldItem() != ItemStack.EMPTY){
                    pedestals.add(pedestal);
                }
            }
        }
        return pedestals;
    }

    public static ParticleOptions getParticleForRitual(Item orb){
        if(orb == ModItems.CORRUPTED_ORB.get()){
            return ParticleRegistry.BLOOD_PARTICLE.get();
        } else if (orb == ModItems.BLESSED_ORB.get()){
            return ParticleRegistry.WISP_PARTICLE.get();
        } else if (orb == ModItems.PLASMATIC_ORB.get()){
            return ParticleRegistry.FIRE_PARTICLE.get();
        } else if (orb == ModItems.OBSCURE_ORB.get()){
            return ParticleRegistry.FIRE_PARTICLE.get();
        } else {
            return ParticleTypes.ASH;
        }
    }

    public static Set<Item> getItemsFromPedestals(List<PedestalTile> surroundingPedestals){
        List<Item> pedestalItems = new ArrayList<>();

        for (PedestalTile pedestal : surroundingPedestals) {
            pedestalItems.add(pedestal.getHeldItem().getItem());
        }

        return new HashSet<>(pedestalItems);
    }

    public static Item getOrbForRecipe(Level level, PedestalTile centerPedestal) {

        Map<RitualRecipe, Item> recipes = RitualHelper.getRitualRecipes();

        List<PedestalTile> surroundingPedestals = RitualHelper.getSurroundingPedestals(centerPedestal, level);

        Set<Item> pedestalSet = RitualHelper.getItemsFromPedestals(surroundingPedestals);
        RitualRecipe recipe = new RitualRecipe(centerPedestal.getHeldItem().getItem(), pedestalSet);

        return recipes.get(recipe);
    }

    public static PedestalTile getPedestal(LivingEntity caster, double distance){
        var block = EUtils.raycastBlockEntity(caster.level(), caster, distance);
        if(block != null){
            if(block instanceof PedestalTile){
                return (PedestalTile) block;
            }
        }
        return null;
    }

    public static Map<RitualRecipe, Item> getRitualRecipes(){
        Config.loadConfigRecipes();
        Map<RitualRecipe, Item> ritualRecipes = Config.ritualRecipes;

        List<Item> fallenOrbRecipeItems = Arrays.asList(ItemRegistry.BLOOD_VIAL.get(), Items.WITHER_SKELETON_SKULL, ItemRegistry.BLOOD_RUNE.get(), ItemRegistry.ARCANE_ESSENCE.get());
        Set<Item> setFallen = new HashSet<>(fallenOrbRecipeItems);
        RitualRecipe fallenOrbRecipe = new RitualRecipe(ModItems.EMPTY_ORB.get(), setFallen);
        ritualRecipes.put(fallenOrbRecipe, ModItems.CORRUPTED_ORB.get());

        List<Item> celestialOrbRecipeItems = Arrays.asList(ItemRegistry.DIVINE_PEARL.get(), ItemRegistry.PROTECTION_RUNE.get(), ItemRegistry.HOLY_RUNE.get(), ItemRegistry.ARCANE_ESSENCE.get());
        Set<Item> setCelestial = new HashSet<>(celestialOrbRecipeItems);
        RitualRecipe celestialOrbRecipe = new RitualRecipe(ModItems.EMPTY_ORB.get(), setCelestial);
        ritualRecipes.put(celestialOrbRecipe, ModItems.BLESSED_ORB.get());

        List<Item> umbrakithOrbRecipeItems = Arrays.asList(ItemRegistry.ELDRITCH_PAGE.get(), Items.SCULK_CATALYST, Items.ECHO_SHARD, ItemRegistry.ARCANE_ESSENCE.get());
        Set<Item> setUmbrakith = new HashSet<>(umbrakithOrbRecipeItems);
        RitualRecipe umbrakithOrbRecipe = new RitualRecipe(ModItems.EMPTY_ORB.get(), setUmbrakith);
        ritualRecipes.put(umbrakithOrbRecipe, ModItems.OBSCURE_ORB.get());

        List<Item> solarianOrbRecipeItems = Arrays.asList(ItemRegistry.FIRE_RUNE.get(), Items.BLAZE_POWDER, ItemRegistry.CINDER_ESSENCE.get(), ItemRegistry.ARCANE_ESSENCE.get());
        Set<Item> setSolarian = new HashSet<>(solarianOrbRecipeItems);
        RitualRecipe solarianOrbRecipe = new RitualRecipe(ModItems.EMPTY_ORB.get(), setSolarian);
        ritualRecipes.put(solarianOrbRecipe, ModItems.OBSCURE_ORB.get());

        return ritualRecipes;
    }
}
