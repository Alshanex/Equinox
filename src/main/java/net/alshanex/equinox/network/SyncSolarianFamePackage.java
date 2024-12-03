package net.alshanex.equinox.network;

import net.alshanex.equinox.fame.SolarianFameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSolarianFamePackage {
    private final int fame;

    public SyncSolarianFamePackage(int fame) {
        this.fame = fame;
    }

    public SyncSolarianFamePackage(FriendlyByteBuf buf) {
        this.fame = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(fame);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(SolarianFameProvider.SOLARIAN_FAME).ifPresent(fame -> {
                    fame.setFame(this.fame);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
