package net.alshanex.equinox.network;

import net.alshanex.equinox.fame.CelestialFameProvider;
import net.alshanex.equinox.fame.FallenFameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncCelestialFamePackage {
    private final int fame;

    public SyncCelestialFamePackage(int fame) {
        this.fame = fame;
    }

    public SyncCelestialFamePackage(FriendlyByteBuf buf) {
        this.fame = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(fame);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(CelestialFameProvider.CELESTIAL_FAME).ifPresent(fame -> {
                    fame.setFame(this.fame);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
