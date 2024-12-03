package net.alshanex.equinox.network;

import net.alshanex.equinox.fame.CelestialFameProvider;
import net.alshanex.equinox.fame.UmbrakithFame;
import net.alshanex.equinox.fame.UmbrakithFameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncUmbrakithFamePackage {
    private final int fame;

    public SyncUmbrakithFamePackage(int fame) {
        this.fame = fame;
    }

    public SyncUmbrakithFamePackage(FriendlyByteBuf buf) {
        this.fame = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(fame);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(UmbrakithFameProvider.UMBRAKITH_FAME).ifPresent(fame -> {
                    fame.setFame(this.fame);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
