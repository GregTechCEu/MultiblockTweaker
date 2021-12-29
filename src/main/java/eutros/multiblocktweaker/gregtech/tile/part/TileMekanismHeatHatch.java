package eutros.multiblocktweaker.gregtech.tile.part;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import eutros.multiblocktweaker.gregtech.client.MBTTextures;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.SimpleTextWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.client.renderer.ICubeRenderer;
import mekanism.api.IHeatTransfer;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.util.HeatUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("InstantiationOfUtilityClass")
public class TileMekanismHeatHatch extends AbstractTileModHatch<IHeatTransfer> implements IHeatTransfer{
    public static final MultiblockAbility<IHeatTransfer> MEKANISM_HEAT = new MultiblockAbility<>("mekanism_heat");

    public double temperature;
    public double heatToAbsorb = 0;
    public double lastEnvironmentLoss;

    public TileMekanismHeatHatch(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier, MEKANISM_HEAT, null, "heat");
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new TileMekanismHeatHatch(metaTileEntityId, getTier());
    }

    @Override
    public ICubeRenderer getUnFormedBaseTexture() {
        return MBTTextures.MEKANISM_HEAT_BASE[getTier() - 1];
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (shouldRenderOverlay()) {
            MBTTextures.MEKANISM_HEAT_IO.render(renderState, translation, pipeline);
        }
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return ModularUI.builder(GuiTextures.BACKGROUND, 300, 150)
                .label(5, 5, this.getMetaFullName())
                .widget(new SimpleTextWidget(5, 50, "", 0xffff0000, ()-> "" + getTemp()))
                .build(this.getHolder(), entityPlayer);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.universal.enabled"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data.setDouble("mbt_hatch_temperature", temperature);
        return super.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        temperature = data.getDouble("mbt_hatch_temperature");
        super.readFromNBT(data);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        T result = capability == Capabilities.HEAT_TRANSFER_CAPABILITY ? Capabilities.HEAT_TRANSFER_CAPABILITY.cast(this) : null;
        return result == null ? super.getCapability(capability, side) : result;
    }

    @Override
    public void update() {
        super.update();
        if (getWorld() != null && !getWorld().isRemote) {
            double[] loss = simulateHeat();
            applyTemperatureChange();
            lastEnvironmentLoss = loss[1];
        }
    }

    @Override
    public double getTemp() {
        return temperature;
    }

    @Override
    public double getInverseConductionCoefficient() {
        return 5 * getTier();
    }

    @Override
    public double getInsulationCoefficient(EnumFacing enumFacing) {
        return 1000 * getTier();
    }

    @Override
    public void transferHeatTo(double heat) {
        heatToAbsorb += heat;
    }

    @Override
    public double[] simulateHeat() {
        return HeatUtils.simulate(this);
    }

    @Override
    public double applyTemperatureChange() {
        temperature += heatToAbsorb;
        heatToAbsorb = 0;
        return temperature;
    }

    @Override
    public boolean canConnectHeat(EnumFacing enumFacing) {
        return true;
    }

    @Override
    public IHeatTransfer getAdjacent(EnumFacing side) {
        TileEntity adj = getWorld().getTileEntity(getPos().offset(side));
        if (adj != null && adj.hasCapability(Capabilities.HEAT_TRANSFER_CAPABILITY, side.getOpposite())) {
            return adj.getCapability(Capabilities.HEAT_TRANSFER_CAPABILITY, side.getOpposite());
        }
        return null;
    }

}
