package net.alshanex.equinox.fame;

import net.minecraft.nbt.CompoundTag;

public class CelestialFame {
    private int fame;
    private final int MIN_FAME = 0;
    private final int MAX_FAME = 1000;

    public int getFame(){
        return fame;
    }

    public void addFame(int add){
        this.fame = Math.min(fame + add, MAX_FAME);
    }

    public void subFame(int sub){
        this.fame = Math.max(fame - sub, MIN_FAME);
    }

    public void setFame(int fame){
        this.fame = fame;
    }

    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("celestialFame", fame);
    }

    public void loadNBTData(CompoundTag nbt){
        fame = nbt.getInt("celestialFame");
    }

    public void copyFrom(CelestialFame source) {
        this.fame = source.fame;
    }
}
