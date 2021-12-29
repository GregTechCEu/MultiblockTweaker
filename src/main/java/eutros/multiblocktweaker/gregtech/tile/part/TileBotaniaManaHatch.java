package eutros.multiblocktweaker.gregtech.tile.part;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import eutros.multiblocktweaker.gregtech.client.MBTTextures;
import eutros.multiblocktweaker.gregtech.tile.holders.BotaniaManaHatchHolder;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;

@SuppressWarnings("InstantiationOfUtilityClass")
public class TileBotaniaManaHatch extends AbstractEnergyStorageHatch {
    public static final MultiblockAbility<IEnergyStorage> INPUT_BOTANIA_MANA = new MultiblockAbility<>("import_botania_mana");
    public static final MultiblockAbility<IEnergyStorage> OUTPUT_BOTANIA_MANA = new MultiblockAbility<>("export_botania_mana");
    private EnumDyeColor color;

    public TileBotaniaManaHatch(ResourceLocation metaTileEntityId, int tier, boolean isOutputHatch) {
        super(metaTileEntityId, tier, isOutputHatch, INPUT_BOTANIA_MANA, OUTPUT_BOTANIA_MANA, BotaniaManaHatchHolder.class, "mana");
        color = EnumDyeColor.GREEN;
    }

    public EnumDyeColor getColor() {
        return color;
    }

    public void setColor(EnumDyeColor color) {
        if (color != this.color && !getWorld().isRemote) {
            writeCustomData(1920, buffer->buffer.writeVarInt(color.getDyeDamage()));
        }
        this.color = color;
    }

    public int getCapacityByTier() {
        return (int) (GTValues.V[getTier()] * 16);
    }

    public int getMaxTransferByTier() {
        return (int) (GTValues.V[getTier()] * 4);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data.setInteger("BotaniaColor", color.getDyeDamage());
        return super.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        color = EnumDyeColor.byDyeDamage(data.getInteger("BotaniaColor"));
    }

    @Override
    public void update() {
        super.update();
        World world = getWorld();
        if (world != null && !world.isRemote) {
            EnumFacing front = getFrontFacing();
            TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(front));
            if (tileEntity instanceof IManaReceiver) {
                IManaReceiver capability = (IManaReceiver) tileEntity;
                int lastCap = capability.getCurrentMana();
                if (maxReceive > 0 && capability instanceof IManaPool) {
                    int maxInput = Math.min(Math.min(maxReceive, (capacity - energy)), lastCap);
                    if (maxInput > 0) {
                        capability.recieveMana(-maxInput);
                        energy += maxInput;
                        markDirty();
                    }
                } else if (maxExtract > 0 && capability.canRecieveManaFromBursts() && !capability.isFull()) {
                    int maxOutput = Math.min(maxExtract, energy);
                    capability.recieveMana(maxOutput);
                    if (lastCap != capability.getCurrentMana()) {
                        energy += lastCap - capability.getCurrentMana();
                        markDirty();
                    }
                }
            }
        }
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeVarInt(color.getDyeDamage());
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        color = EnumDyeColor.byDyeDamage(buf.readVarInt());
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        if (dataId == 1920) {
            color = EnumDyeColor.byDyeDamage(buf.readVarInt());
        } else {
            super.receiveCustomData(dataId, buf);
        }
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new TileBotaniaManaHatch(metaTileEntityId, getTier(), isOutputHatch);
    }

    @Override
    public ICubeRenderer getUnFormedBaseTexture() {
        return MBTTextures.BOTANIA_MANA_BASE[getTier() - 1];
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (shouldRenderOverlay()) {
            (isOutputHatch ? MBTTextures.BOTANIA_MANA_OUT : MBTTextures.BOTANIA_MANA_IN).renderSided(getFrontFacing(), renderState, translation, pipeline);
        }
    }

}
