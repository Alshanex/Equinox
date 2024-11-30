package net.alshanex.equinox.item;

import net.alshanex.equinox.EquinoxMod;
import net.alshanex.equinox.item.orbs.BlessedOrbItem;
import net.alshanex.equinox.item.orbs.CorruptedOrbItem;
import net.alshanex.equinox.item.orbs.ObscureOrbItem;
import net.alshanex.equinox.item.orbs.SolarOrbItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EquinoxMod.MODID);


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> CORRUPTED_ORB = ITEMS.register("corrupted_orb", CorruptedOrbItem::new);
    public static final RegistryObject<Item> BLESSED_ORB = ITEMS.register("blessed_orb", BlessedOrbItem::new);
    public static final RegistryObject<Item> SOLAR_ORB = ITEMS.register("solar_orb", SolarOrbItem::new);
    public static final RegistryObject<Item> OBSCURE_ORB = ITEMS.register("obscure_orb", ObscureOrbItem::new);

    public static Collection<RegistryObject<Item>> getOrbItems() {
        return ITEMS.getEntries();
    }
}
