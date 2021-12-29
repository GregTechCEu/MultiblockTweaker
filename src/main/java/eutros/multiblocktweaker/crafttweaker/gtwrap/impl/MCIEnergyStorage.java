package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIEnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class MCIEnergyStorage implements IIEnergyStorage {
    private final IEnergyStorage inner;

    public MCIEnergyStorage(IEnergyStorage inner) {
        this.inner = inner;
    }

    @Override
    public IEnergyStorage getInternal() {
        return inner;
    }
}
