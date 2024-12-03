package net.alshanex.equinox.network;

import net.alshanex.equinox.fame.FallenFameProvider;
import net.alshanex.equinox.fame.SolarianFameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncFallenFamePackage {
    private final int fame;

    public SyncFallenFamePackage(int fame) {
        this.fame = fame;
    }

    public SyncFallenFamePackage(FriendlyByteBuf buf) {
        this.fame = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(fame);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(FallenFameProvider.FALLEN_FAME).ifPresent(fame -> {
                    fame.setFame(this.fame);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
