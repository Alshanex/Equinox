package net.alshanex.equinox.network;

import net.alshanex.equinox.EquinoxMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(EquinoxMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SyncCelestialFamePackage.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncCelestialFamePackage::new)
                .encoder(SyncCelestialFamePackage::toBytes)
                .consumerMainThread(SyncCelestialFamePackage::handle)
                .add();

        net.messageBuilder(SyncFallenFamePackage.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncFallenFamePackage::new)
                .encoder(SyncFallenFamePackage::toBytes)
                .consumerMainThread(SyncFallenFamePackage::handle)
                .add();

        net.messageBuilder(SyncUmbrakithFamePackage.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncUmbrakithFamePackage::new)
                .encoder(SyncUmbrakithFamePackage::toBytes)
                .consumerMainThread(SyncUmbrakithFamePackage::handle)
                .add();

        net.messageBuilder(SyncSolarianFamePackage.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncSolarianFamePackage::new)
                .encoder(SyncSolarianFamePackage::toBytes)
                .consumerMainThread(SyncSolarianFamePackage::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);

    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        if (sendToSource && entity instanceof ServerPlayer serverPlayer)
            sendToPlayer(message, serverPlayer);
    }
}
