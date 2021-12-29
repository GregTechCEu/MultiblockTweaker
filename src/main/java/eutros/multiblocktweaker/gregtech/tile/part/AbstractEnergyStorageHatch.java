package eutros.multiblocktweaker.gregtech.tile.part;

import eutros.multiblocktweaker.gregtech.tile.holders.AbstractHatchHolder;
import gregtech.api.metatileentity.MTETrait;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class AbstractEnergyStorageHatch extends AbstractTileModHatch<IEnergyStorage> implements IEnergyStorage {
    protected int energy;
    protected int capacity;
    public final int maxReceive;
    public final int maxExtract;

    public AbstractEnergyStorageHatch(ResourceLocation metaTileEntityId,
                                      int tier,
                                      boolean isOutputHatch,
                                      MultiblockAbility<IEnergyStorage> input_ability,
                                      MultiblockAbility<IEnergyStorage> output_ability,
                                      Class<? extends AbstractHatchHolder<?>> clazz,
                                      String unit) {
        super(metaTileEntityId, tier, isOutputHatch, input_ability, output_ability, clazz, unit);
        this.capacity = getCapacityByTier();
        this.maxReceive = !isOutputHatch ? getMaxTransferByTier() : 0;
        this.maxExtract = isOutputHatch ? 0 : getMaxTransferByTier();
        this.energy = 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data.setLong("mbt_hatch_energy", energy);
        return super.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        this.energy = data.getInteger("mbt_hatch_energy");
        super.readFromNBT(data);
    }

    @Override
    public int getAmount() {
        return getEnergyStored();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive()) {
            return 0;
        } else {
            int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));
            if (!simulate) {
                this.energy += energyReceived;
            }
            return energyReceived;
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) {
            return 0;
        } else {
            int energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));
            if (!simulate) {
                this.energy -= energyExtracted;
            }
            return energyExtracted;
        }
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }
}
