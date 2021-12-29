package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import net.minecraftforge.energy.IEnergyStorage;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * The container of the IEnergyStorage.
 *
 * @zenClass mods.multiblocktweaker.IEnergyStorage
 */
@ZenClass("mods.multiblocktweaker.IEnergyStorage")
@ZenRegister
public interface IIEnergyStorage {

    IEnergyStorage getInternal();

    /**
     * Adds energy to the storage. Returns quantity of energy that was accepted.
     *
     * @param maxReceive
     *            Maximum amount of energy to be inserted.
     * @param simulate
     *            If TRUE, the insertion will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
     */
    @ZenMethod
    default int receiveEnergy(int maxReceive, boolean simulate){
        return getInternal().receiveEnergy(maxReceive, simulate);
    }

    /**
     * Removes energy from the storage. Returns quantity of energy that was removed.
     *
     * @param maxExtract
     *            Maximum amount of energy to be extracted.
     * @param simulate
     *            If TRUE, the extraction will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
     */
    @ZenMethod
    default int extractEnergy(int maxExtract, boolean simulate) {
        return getInternal().extractEnergy(maxExtract, simulate);
    }

    /**
     *
     * @return the amount of energy currently stored.
     */
    @ZenMethod
    @ZenGetter("energyStored")
    default int getEnergyStored() {
        return getInternal().getEnergyStored();
    }

    /**
     *
     * @return the maximum amount of energy that can be stored.
     */
    @ZenMethod
    @ZenGetter("maxEnergyStored")
    default int getMaxEnergyStored() {
        return getInternal().getMaxEnergyStored();
    }

    /**
     *
     * @return if this storage can have energy extracted.
     * If this is false, then any calls to extractEnergy will return 0.
     */
    @ZenMethod
    @ZenGetter
    default boolean canExtract() {
        return getInternal().canExtract();
    }

    /**
     *
     * @return Used to determine if this storage can receive energy.
     * If this is false, then any calls to receiveEnergy will return 0.
     */
    @ZenMethod
    @ZenGetter
    default boolean canReceive() {
        return getInternal().canReceive();
    }
}
