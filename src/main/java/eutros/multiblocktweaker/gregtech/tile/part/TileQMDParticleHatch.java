package eutros.multiblocktweaker.gregtech.tile.part;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import eutros.multiblocktweaker.gregtech.client.MBTTextures;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.client.renderer.ICubeRenderer;
import lach_01298.qmd.capabilities.CapabilityParticleStackHandler;
import lach_01298.qmd.particle.IParticleStackHandler;
import lach_01298.qmd.particle.ITileParticleStorage;
import lach_01298.qmd.particle.ParticleStack;
import lach_01298.qmd.particle.ParticleStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("InstantiationOfUtilityClass")
public class TileQMDParticleHatch extends AbstractTileModHatch<ITileParticleStorage> implements ITileParticleStorage{
    public static final MultiblockAbility<ITileParticleStorage> INPUT_QMD_PARTICLE = new MultiblockAbility<>("import_qmd_particle");
    public static final MultiblockAbility<ITileParticleStorage> OUTPUT_QMD_PARTICLE = new MultiblockAbility<>("export_qmd_particle");

    protected final ParticleStorage beam;

    public TileQMDParticleHatch(ResourceLocation metaTileEntityId, int tier, boolean isOutputHatch) {
        super(metaTileEntityId, tier, isOutputHatch, INPUT_QMD_PARTICLE, OUTPUT_QMD_PARTICLE, null, "amount");
        this.beam = new ParticleStorage(null, 9223372036854775807L, getCapacityByTier()) {
            @Override
            public boolean canReciveParticle(EnumFacing side, ParticleStack stack) {
                return !TileQMDParticleHatch.this.isOutputHatch && super.canReciveParticle(side, stack);
            }

            @Override
            public boolean canExtractParticle(EnumFacing side) {
                return TileQMDParticleHatch.this.isOutputHatch && super.canExtractParticle(side);
            }
        };
    }

    @Override
    int getCapacityByTier() {
        return Integer.MAX_VALUE / (5 - getTier());
    }

    @Override
    int getAmount() {
        return beam.getParticleStack() == null ? 0 : beam.getParticleStack().getAmount();
    }

    @Override
    public ICubeRenderer getUnFormedBaseTexture() {
        return MBTTextures.QMD_PARTICLE_BASE;
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        ICubeRenderer renderer = this.getBaseTexture();
        int color = -1;
        if (renderer == MBTTextures.QMD_PARTICLE_BASE) {
            switch (getTier()) {
                case 1:
                    color = 0xff880000;
                    break;
                case 2:
                    color = 0xff008800;
                    break;
                case 3:
                    color = 0xff000088;
                    break;
                case 4:
                    color = 0xff880088;
                    break;
            }
        }
        renderer.render(renderState, translation, ArrayUtils.add(pipeline, new ColourMultiplier(color)));
        if (shouldRenderOverlay()) {
            (isOutputHatch ? MBTTextures.QMD_PARTICLE_OUT : MBTTextures.QMD_PARTICLE_IN).renderSided(getFrontFacing(), renderState, translation, pipeline);
        }
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new TileQMDParticleHatch(metaTileEntityId, getTier(), isOutputHatch);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        T result = capability == CapabilityParticleStackHandler.PARTICLE_HANDLER_CAPABILITY ? CapabilityParticleStackHandler.PARTICLE_HANDLER_CAPABILITY.cast(beam) : null;
        return result == null ?  super.getCapability(capability, side) : result;
    }

    @Override
    public void update() {
        World world = getWorld();
        if(world != null && !world.isRemote) {
            BlockPos pos = getPos();
            EnumFacing face = getFrontFacing();
            TileEntity tile = world.getTileEntity(pos.offset(face));
            if (tile != null) {
                IParticleStackHandler otherStorage = tile.getCapability(CapabilityParticleStackHandler.PARTICLE_HANDLER_CAPABILITY, face.getOpposite());
                if (otherStorage != null) {
                    otherStorage.reciveParticle(face.getOpposite(), beam.getParticleStack());
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        NBTTagCompound nbt = new NBTTagCompound();
        beam.writeToNBT(nbt, 0);
        data.setTag("mbt_hatch_particle", nbt);
        return super.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        beam.readFromNBT(data.getCompoundTag("mbt_hatch_particle"), 0);
    }

    @Nonnull
    @Override
    public List<? extends ParticleStorage> getParticleBeams() {
        return Collections.singletonList(beam);
    }
}
