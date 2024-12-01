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

public class CelestialFameProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<CelestialFame> CELESTIAL_FAME = CapabilityManager.get(new CapabilityToken<CelestialFame>() {});

    private CelestialFame fame = null;
    private final LazyOptional<CelestialFame> optional = LazyOptional.of(this::createCelestialFame);

    private CelestialFame createCelestialFame() {
        if(this.fame == null){
            this.fame = new CelestialFame();
        }

        return this.fame;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CELESTIAL_FAME){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createCelestialFame().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCelestialFame().loadNBTData(nbt);
    }
}
