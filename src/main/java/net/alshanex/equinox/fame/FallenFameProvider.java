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

public class FallenFameProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<FallenFame> FALLEN_FAME = CapabilityManager.get(new CapabilityToken<FallenFame>() {});

    private FallenFame fame = null;
    private final LazyOptional<FallenFame> optional = LazyOptional.of(this::createFallenFame);

    private FallenFame createFallenFame() {
        if(this.fame == null){
            this.fame = new FallenFame();
        }

        return this.fame;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == FALLEN_FAME){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createFallenFame().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createFallenFame().loadNBTData(nbt);
    }
}
