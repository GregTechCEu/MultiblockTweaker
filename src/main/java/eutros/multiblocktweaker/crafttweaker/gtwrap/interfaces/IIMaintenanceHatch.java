package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

/**
 * A tile entity with maintenance hatch.
 *
 * @zenClass mods.gregtech.multiblock.IIMaintenanceHatch
 * @see IMultiblockPart
 */
@ZenClass("mods.gregtech.multiblock.IIMaintenanceHatch")
@ZenRegister
public interface IIMaintenanceHatch {

    /**
     * @return true if this is a Full-Auto Maintenance Hatch, false otherwise.
     */
    @ZenMethod
    @ZenGetter("isFullAuto")
    boolean isFullAuto();

    /**
     * Sets this Maintenance Hatch as being duct taped
     * @param isTaped is the state of the hatch being taped or not
     */
    @ZenMethod
    @ZenSetter("setTaped")
    void setTaped(boolean isTaped);

    /**
     * Stores maintenance data to this MetaTileEntity
     * @param maintenanceProblems is the byte value representing the problems
     * @param timeActive is the int value representing the total time the parent multiblock has been active
     */
    @ZenMethod
    void storeMaintenanceData(byte maintenanceProblems, int timeActive);

    /**
     *
     * @return whether this maintenance hatch has maintenance data
     */
    @ZenMethod
    @ZenGetter("hasMaintenanceData")
    boolean hasMaintenanceData();

    /**
     * reads this MetaTileEntity's maintenance data
     * @return Tuple of Byte, Integer corresponding to the maintenance problems, and total time active
     */
    @ZenMethod
    int[] readMaintenanceData();


    /**
     *
     * @return //TODO
     */
    @ZenMethod
    @ZenGetter("durationMultiplier")
    double getDurationMultiplier();


    /**
     *
     * @return //TODO
     */
    @ZenMethod
    @ZenGetter("timeMultiplier")
    double getTimeMultiplier();


    /**
     *
     * @return //TODO
     */
    @ZenMethod
    @ZenGetter("startWithoutProblems")
    boolean startWithoutProblems();
}
