package eutros.multiblocktweaker.gregtech.tile.part;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import eutros.multiblocktweaker.gregtech.client.MBTTextures;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

@SuppressWarnings("InstantiationOfUtilityClass")
public class TileForgeEnergyHatch extends AbstractEnergyStorageHatch {
    public static final MultiblockAbility<IEnergyStorage> INPUT_FORGE_ENERGY = new MultiblockAbility<>("import_forge_energy");
    public static final MultiblockAbility<IEnergyStorage> OUTPUT_FORGE_ENERGY = new MultiblockAbility<>("export_forge_energy");


    public TileForgeEnergyHatch(ResourceLocation metaTileEntityId, int tier, boolean isOutputHatch) {
        super(metaTileEntityId, tier, isOutputHatch, INPUT_FORGE_ENERGY, OUTPUT_FORGE_ENERGY, null, "fe");

    }
    
    public int getCapacityByTier() {
        return (int) (GTValues.V[getTier()] * 16);
    }

    public int getMaxTransferByTier() {
        return (int) (GTValues.V[getTier()] * 4);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        T result = capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this) : null;
        return result == null ? super.getCapability(capability, side) : result;
    }

    @Override
    public void update() {
        super.update();
        World world = getWorld();
        if (world != null && !world.isRemote) {
            EnumFacing front = getFrontFacing();
            TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(front));
            IEnergyStorage capability = tileEntity == null ? null : tileEntity.getCapability(
                    CapabilityEnergy.ENERGY, front.getOpposite());
            if (capability != null) {
                if (maxReceive > 0 && capability.canExtract()) {
                    int maxInput = Math.min(maxReceive, (capacity - energy));
                    maxInput = capability.extractEnergy(maxInput, false);
                    if (maxInput > 0) {
                        energy += maxInput;
                        markDirty();
                    }
                } else if (maxExtract > 0 && capability.canReceive()) {
                    int maxOutput = Math.min(maxExtract, energy);
                    maxOutput = capability.receiveEnergy(maxOutput, false);
                    if (maxOutput > 0) {
                        energy -= maxOutput;
                        markDirty();
                    }
                }
            }
        }
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new TileForgeEnergyHatch(metaTileEntityId, getTier(), isOutputHatch);
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (shouldRenderOverlay()) {
            (isOutputHatch ? MBTTextures.FORGE_ENERGY_OUT : MBTTextures.FORGE_ENERGY_IN).renderSided(getFrontFacing(), renderState, translation, pipeline);
        }
    }

}
