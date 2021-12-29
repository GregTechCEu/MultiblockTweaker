package eutros.multiblocktweaker.gregtech.recipes;


import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.*;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.*;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.recipes.MatchingMode;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.common.ConfigHolder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CustomMultiblockRecipeLogic extends MultiblockRecipeLogic implements IRecipeLogic {

    private int recipeFEt;
    private int recipeMANAt;
    private AspectList aspectOutputs;

    /**
     * This property will set the chance of the inputs being consumed when a recipe is started.
     */
    public final CustomMultiblock multiblock;

    public CustomMultiblockRecipeLogic(TileControllerCustom controller) {
        super(controller);
        this.multiblock = controller.multiblock;
    }

    // FUNCTIONS

    private void logFailure(String func, Throwable t) {
        CraftTweakerAPI.logError(String.format("Couldn't run %s function of %s.", func, getMetaTile().getMultiblock()), t);
    }

    @Override
    protected int[] runOverclockingLogic(@NotNull Recipe recipe, boolean negativeEU, int maxOverclocks) {
        if (multiblock.runOverclockingLogic != null) {
            try {
                return multiblock.runOverclockingLogic.run(this, new MCRecipe(recipe), negativeEU, maxOverclocks);
            } catch (RuntimeException t) {
                logFailure("runOverclockingLogic", t);
                multiblock.runOverclockingLogic = null;
            }
        }
        return super.runOverclockingLogic(recipe, negativeEU, maxOverclocks);
    }

    @Override
    protected boolean setupAndConsumeRecipeInputs(Recipe recipe, IItemHandlerModifiable importInventory) {
        if (recipe instanceof CustomRecipe) {
            boolean available = true;
            for (Map.Entry<Aspect, Integer> entry : ((CustomRecipe) recipe).aspectOutputs.aspects.entrySet()) {
                available = false;
                for (IAspectTank tank : getOutAspectHatch()) {
                    if (tank.getInternal() == null || tank.getInternal() == entry.getKey()) {
                        available = true;
                        break;
                    }
                }
                if (!available) {
                    break;
                }
            }
            if(available && super.setupAndConsumeRecipeInputs(recipe, importInventory)) {
                return ((CustomRecipe) recipe).matches(true,
                        GTUtility.itemHandlerToList(importInventory),
                        GTUtility.fluidHandlerToList(this.getInputTank()),
                        getInAspectHatch().stream().map(IAspectStack.class::cast).collect(Collectors.toList()),
                        MatchingMode.DEFAULT);
            }
            return false;
        }
        return super.setupAndConsumeRecipeInputs(recipe, importInventory);
    }

    @Override
    protected Recipe findRecipe(long maxVoltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, MatchingMode mode) {
        RecipeMap<?> recipeMap = getRecipeMap();
        if (recipeMap instanceof RecipeMapMultiblock) {
            return ((RecipeMapMultiblock)recipeMap).findRecipe(maxVoltage, 0, 0,
                    GTUtility.itemHandlerToList(inputs),
                    GTUtility.fluidHandlerToList(fluidInputs),
                    getInAspectHatch().stream().map(IAspectStack.class::cast).collect(Collectors.toList()),
                    mode);
        }
        return super.findRecipe(maxVoltage, inputs, fluidInputs, mode);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        if (this.progressTime > 0) {
            nbt.setInteger("RecipeFE", recipeFEt);
            nbt.setInteger("RecipeMANA", recipeMANAt);
            aspectOutputs.writeToNBT(nbt, "AspectOutputs");
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        super.deserializeNBT(compound);
        if (this.progressTime > 0) {
            recipeFEt = compound.getInteger("RecipeFE");
            recipeMANAt = compound.getInteger("RecipeMANA");
            aspectOutputs.readFromNBT(compound,"AspectOutputs");
        }
    }

    @Override
    public void trySearchNewRecipe() {
        super.trySearchNewRecipe(); // protected.
    }

    @Override
    public void trySearchNewRecipeCombined() {
        super.trySearchNewRecipeCombined();
    }

    @Override
    public void trySearchNewRecipeDistinct() {
        super.trySearchNewRecipeDistinct();
    }

    @Override
    public boolean checkPreviousRecipeDistinct(IIItemHandlerModifiable previousBus) {
        return super.checkPreviousRecipeDistinct(previousBus.getInner());
    }

    @Override
    public boolean prepareRecipeDistinct(IRecipe recipe) {
        return super.prepareRecipeDistinct(recipe.getInner());
    }

    @Override
    public void performMaintenanceMufflerOperations() {
        super.performMaintenanceMufflerOperations();
    }

    @Override
    public void updateWorkable() {
        boolean result = true;
        if (multiblock.updateWorktableFunction != null) {
            try {
                result = multiblock.updateWorktableFunction.run(this);
            } catch (RuntimeException t) {
                logFailure("updateWorktable", t);
                multiblock.updateWorktableFunction = null;
            }
        }
        if (result) {
            super.updateWorkable();
        }
    }

    @Override
    protected void setupRecipe(Recipe recipe) {
        boolean result = true;
        if (multiblock.setupRecipeFunction != null) {
            try {
                result = multiblock.setupRecipeFunction.run(this, new MCRecipe(recipe));
            } catch (RuntimeException t) {
                logFailure("setupRecipe", t);
                multiblock.setupRecipeFunction = null;
            }
        }
        if (result) {
            superSetupRecipe(new MCRecipe(recipe));
        }
    }

    @Override
    protected void completeRecipe() {
        boolean result = true;
        if (multiblock.completeRecipeFunction != null) {
            try {
                result = multiblock.completeRecipeFunction.run(this);
            } catch (RuntimeException t) {
                logFailure("completeRecipe", t);
                multiblock.completeRecipeFunction = null;
            }
        }
        if (result) {
            superCompleteRecipe();
        }
    }

    @Override
    protected void updateRecipeProgress() {
        boolean failed = false;
        if (recipeFEt != 0) {
            IEnergyStorage energyStorage = getFEHatch().getInternal();
            if (recipeFEt > 0) {
                int left = recipeFEt - energyStorage.extractEnergy(recipeFEt, false);
                if (left > 0) {
                    failed = true;
                }
            } else {
                energyStorage.receiveEnergy(recipeFEt, false);
            }
        }
        if (!failed && recipeMANAt != 0) {
            IEnergyStorage energyStorage = getMANAHatch().getInternal();
            if (recipeFEt > 0) {
                int left = recipeMANAt - energyStorage.extractEnergy(recipeMANAt, false);
                if (left > 0) {
                    failed = true;
                }
            } else {
                energyStorage.receiveEnergy(recipeMANAt, false);
            }
        }
        if (failed) {
            this.hasNotEnoughEnergy = true;
            if (this.progressTime >= 2) {
                if (ConfigHolder.machines.recipeProgressLowEnergy) {
                    this.progressTime = 1;
                } else {
                    this.progressTime = Math.max(1, this.progressTime - 2);
                }
            }
        } else {
            super.updateRecipeProgress();
        }
    }

    // CT EXPOSED

    @Override
    public void superSetupRecipe(IRecipe recipe) {
        super.setupRecipe(recipe.getInner());
        if (recipe.getInner() instanceof  CustomRecipe) {
            CustomRecipe customRecipe = (CustomRecipe) recipe.getInner();
            this.recipeFEt = customRecipe.FEt;
            this.recipeMANAt = customRecipe.MANAt;
            this.aspectOutputs = customRecipe.aspectOutputs.copy();
        }
    }

    @Override
    public void superCompleteRecipe() {
        super.completeRecipe();
        for (Map.Entry<Aspect, Integer> entry : this.aspectOutputs.aspects.entrySet()) {
            int fill = entry.getValue();
            for (IAspectTank tank : getOutAspectHatch()) {
                if (tank.getInternal() == null || tank.getInternal() == entry.getKey()) {
                    int cost = Math.min(fill, tank.getCapacity() - tank.getAmount());
                    tank.setAmount(tank.getAmount() + cost);
                    fill -= cost;
                    if (fill == 0) break;
                }
            }
        }
        this.recipeFEt = 0;
        this.recipeMANAt = 0;
        this.aspectOutputs = null;
    }

    @Override
    public void superUpdateWorkable() {
        super.updateWorkable();
    }

    @Override
    public int parallelRecipesPerformed() {
        return parallelRecipesPerformed;
    }

    @Override
    public void ParallelRecipesPerformed(int amount) {
        setParallelRecipesPerformed(amount);
    }

    @Override
    public long overclockVoltage() {
        return getOverclockVoltage();
    }

    @Override
    public void overclockVoltage(long overclockVoltage) {
        setOverclockVoltage(overclockVoltage);
    }

    @Override
    public int progressTime() {
        return progressTime;
    }

    @Override
    public void progressTime(int progressTime) {
        this.progressTime = progressTime;
    }

    @Override
    public int maxProgressTime() {
        return getMaxProgress();
    }

    @Override
    public void maxProgressTime(int maxProgressTime) {
        setMaxProgress(maxProgressTime);
    }

    @Override
    public int recipeEUt() {
        return getRecipeEUt();
    }

    @Override
    public void recipeEUt(int eut) {
        this.recipeEUt = eut;
    }

    @Override
    public int recipeFEt() {
        return this.recipeFEt;
    }

    @Override
    public void recipeFEt(int FEt) {
        this.recipeFEt = FEt;
    }

    @Override
    public int recipeMANAt() {
        return this.recipeMANAt;
    }

    @Override
    public void recipeMANAt(int MANAt) {
        this.recipeMANAt = MANAt;
    }

    @Override
    public IAspectStack[] aspectOutputs() {
        if (this.aspectOutputs == null) return null;
        return this.aspectOutputs.aspects.entrySet().stream().map(entry->new MCAspectStack(entry.getKey(), entry.getValue())).toArray(IAspectStack[]::new);
    }

    @Override
    public void aspectOutputs(IAspectStack[] aspectOutputs) {
        if (aspectOutputs == null) this.aspectOutputs = null;
        else {
            this.aspectOutputs = new AspectList();
            for (IAspectStack aspectOutput : aspectOutputs) {
                if (aspectOutput.getInternal() == null) continue;
                this.aspectOutputs.merge(aspectOutput.getInternal(), aspectOutput.getAmount());
            }
        }
    }

    @Override
    public ILiquidStack[] fluidOutputs() {
        return this.fluidOutputs == null ? null : this.fluidOutputs.stream().map(CraftTweakerMC::getILiquidStack).toArray(ILiquidStack[]::new);
    }

    @Override
    public void fluidOutputs(ILiquidStack[] fluidOutputs) {
        if (fluidOutputs == null) this.fluidOutputs = null;
        else {
            this.fluidOutputs = Arrays.stream(fluidOutputs).map(CraftTweakerMC::getLiquidStack).collect(Collectors.toList());
        }
    }

    @Override
    public IItemStack[] itemOutputs() {
        return this.itemOutputs == null ? null : this.itemOutputs.stream().map(CraftTweakerMC::getIItemStack).toArray(IItemStack[]::new);
    }

    @Override
    public void itemOutputs(IItemStack[] itemOutputs) {
        if (itemOutputs == null) this.itemOutputs = null;
        else {
            this.itemOutputs = NonNullList.create();
            Arrays.stream(itemOutputs).map(CraftTweakerMC::getItemStack).forEach(this.itemOutputs::add);
        }
    }

    @Override
    public boolean wasActiveAndNeedsUpdate() {
        return wasActiveAndNeedsUpdate;
    }

    @Override
    public void wasActiveAndNeedsUpdate(boolean wasActiveAndNeedsUpdate) {
        this.wasActiveAndNeedsUpdate = wasActiveAndNeedsUpdate;
    }

    @Override
    public boolean isOutputsFull() {
        return this.isOutputsFull;
    }

    @Override
    public void isOutputsFull(boolean isOutputsFull) {
        this.isOutputsFull = isOutputsFull;
    }

    @Override
    public boolean invalidInputsForRecipes() {
        return this.invalidInputsForRecipes;
    }

    @Override
    public void invalidInputsForRecipes(boolean invalidInputsForRecipes) {
        this.invalidInputsForRecipes = invalidInputsForRecipes;
    }

    @Override
    public int getLastRecipeIndex() {
        return lastRecipeIndex;
    }

    @Override
    public void setLstRecipeIndex(int index) {
        this.lastRecipeIndex = index;
    }

    @Override
    public IRecipe getPreviousIRecipe() {
        return previousRecipe == null ? null : new MCRecipe(previousRecipe);
    }

    @Override
    public void setPreviousIRecipe(IRecipe previousRecipe) {
        this.previousRecipe = previousRecipe == null ? null : previousRecipe.getInner();
    }

    @Override
    public IControllerTile getMetaTile() {
        return new MCControllerTile((TileControllerCustom) metaTileEntity);
    }

    @Override
    public void setProgress(int val) {
        progressTime = val;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }

    @Override
    public boolean hasNotEnoughEnergy() {
        return isHasNotEnoughEnergy();
    }

    @Override
    public void hasNotEnoughEnergy(boolean hasNotEnoughEnergy) {
        this.hasNotEnoughEnergy = hasNotEnoughEnergy;
    }

    @Override
    public long getEnergyInputPerSecond() {
        return super.getEnergyInputPerSecond();
    }

    @Override
    public long getEnergyStored() {
        return super.getEnergyStored();
    }

    @Override
    public long getEnergyCapacity() {
        return super.getEnergyCapacity();
    }

    @Override
    public int[] calculateOverclock(IRecipe recipe) {
        return super.calculateOverclock(recipe.getInner());
    }

    @Override
    public boolean drawEnergy(int recipeEUt) {
        return super.drawEnergy(recipeEUt);
    }

    @Override
    public long getMaxVoltage() {
        return super.getMaxVoltage();
    }

    @Override
    public IIItemHandlerModifiable getCurrentDistinctInputBus() {
        return new MCIItemHandlerModifiable(super.currentDistinctInputBus);
    }

    @Override
    public List<IIItemHandlerModifiable> getInvalidatedInputList() {
        return super.invalidatedInputList.stream().map(MCIItemHandlerModifiable::new).collect(Collectors.toList());
    }

    @Override
    public List<IIItemHandlerModifiable> getInputBus() {
        return super.getInputBuses().stream().map(MCIItemHandlerModifiable::new).collect(Collectors.toList());
    }

    @Override
    public IIItemHandlerModifiable getInInventory() {
        return new MCIItemHandlerModifiable(super.getInputInventory());
    }

    @Override
    public IIItemHandlerModifiable getOutInventory() {
        return new MCIItemHandlerModifiable(super.getOutputInventory());
    }

    @Override
    public IIMultipleTankHandler getInTank() {
        return new MCIMultipleTankHandler(super.getInputTank());
    }

    @Override
    public IIMultipleTankHandler getOutTank() {
        return new MCIMultipleTankHandler(super.getOutputTank());
    }

    @Override
    public IIEnergyContainer getEnergyHatch() {
        return new MCIEnergyContainer(super.getEnergyContainer());
    }

    @Override
    public IIEnergyStorage getFEHatch() {
        TileControllerCustom controller = (TileControllerCustom)this.metaTileEntity;
        return controller.getFEHatch();
    }

    @Override
    public IIEnergyStorage getMANAHatch() {
        TileControllerCustom controller = (TileControllerCustom)this.metaTileEntity;
        return controller.getMANAHatch();
    }

    @Override
    public List<IAspectTank> getInAspectHatch() {
        TileControllerCustom controller = (TileControllerCustom)this.metaTileEntity;
        return controller.getInAspectHatch();
    }

    @Override
    public List<IAspectTank> getOutAspectHatch() {
        TileControllerCustom controller = (TileControllerCustom)this.metaTileEntity;
        return controller.getOutAspectHatch();
    }
}
