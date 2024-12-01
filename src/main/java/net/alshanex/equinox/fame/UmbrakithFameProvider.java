package net.alshanex.equinox.fame;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UmbrakithFameProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<UmbrakithFame> UMBRAKITH_FAME = CapabilityManager.get(new CapabilityToken<UmbrakithFame>() {});

    private UmbrakithFame fame = null;
    private final LazyOptional<UmbrakithFame> optional = LazyOptional.of(this::createUmbrakithFame);

    private UmbrakithFame createUmbrakithFame() {
        if(this.fame == null){
            this.fame = new UmbrakithFame();
        }

        return this.fame;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == UMBRAKITH_FAME){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createUmbrakithFame().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createUmbrakithFame().loadNBTData(nbt);
    }
}
