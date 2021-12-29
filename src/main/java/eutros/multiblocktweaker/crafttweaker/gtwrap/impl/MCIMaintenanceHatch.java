package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMaintenanceHatch;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultiblockPart;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.capability.IMaintenanceHatch;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.common.metatileentities.electric.multiblockpart.MetaTileEntityMultiblockPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MCIMaintenanceHatch implements IIMaintenanceHatch {

    private final IMaintenanceHatch inner;

    public MCIMaintenanceHatch(IMaintenanceHatch inner) {
        this.inner = inner;
    }


    @Override
    public boolean isFullAuto() {
        return inner.isFullAuto();
    }

    @Override
    public void setTaped(boolean isTaped) {
        inner.setTaped(isTaped);
    }

    @Override
    public void storeMaintenanceData(byte maintenanceProblems, int timeActive) {
        inner.storeMaintenanceData(maintenanceProblems, timeActive);
    }

    @Override
    public boolean hasMaintenanceData() {
        return inner.hasMaintenanceData();
    }

    @Override
    public int[] readMaintenanceData() {
        return new int[]{inner.readMaintenanceData().getFirst().intValue(), inner.readMaintenanceData().getSecond()};
    }

    @Override
    public double getDurationMultiplier() {
        return inner.getDurationMultiplier();
    }

    @Override
    public double getTimeMultiplier() {
        return inner.getTimeMultiplier();
    }

    @Override
    public boolean startWithoutProblems() {
        return inner.startWithoutProblems();
    }
}