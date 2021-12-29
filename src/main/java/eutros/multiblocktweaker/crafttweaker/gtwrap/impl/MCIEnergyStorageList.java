package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

public class MCIEnergyStorageList implements IEnergyStorage {
    private final List<IEnergyStorage> energyContainerList;

    public MCIEnergyStorageList(List<IEnergyStorage> energyContainerList) {
        this.energyContainerList = energyContainerList;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int left = maxReceive;
        for (IEnergyStorage energyStorage : energyContainerList) {
            if (!energyStorage.canReceive()) continue;
            left -= energyStorage.receiveEnergy(maxReceive, simulate);
            if (left == 0) break;
        }
        return maxReceive - left;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int left = maxExtract;
        for (IEnergyStorage energyStorage : energyContainerList) {
            if (!energyStorage.canExtract()) continue;
            left -= energyStorage.extractEnergy(maxExtract, simulate);
            if (left == 0) break;
        }
        return maxExtract - left;
    }

    @Override
    public int getEnergyStored() {
        return this.energyContainerList.stream().mapToInt(IEnergyStorage::getEnergyStored).sum();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.energyContainerList.stream().mapToInt(IEnergyStorage::getMaxEnergyStored).sum();
    }

    @Override
    public boolean canExtract() {
        return this.energyContainerList.stream().anyMatch(IEnergyStorage::canExtract);
    }

    @Override
    public boolean canReceive() {
        return this.energyContainerList.stream().anyMatch(IEnergyStorage::canReceive);
    }
}
