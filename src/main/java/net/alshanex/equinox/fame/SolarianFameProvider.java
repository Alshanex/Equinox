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

public class SolarianFameProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<SolarianFame> SOLARIAN_FAME = CapabilityManager.get(new CapabilityToken<SolarianFame>() {});

    private SolarianFame fame = null;
    private final LazyOptional<SolarianFame> optional = LazyOptional.of(this::createSolarianFame);

    private SolarianFame createSolarianFame() {
        if(this.fame == null){
            this.fame = new SolarianFame();
        }

        return this.fame;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == SOLARIAN_FAME){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createSolarianFame().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSolarianFame().loadNBTData(nbt);
    }
}
