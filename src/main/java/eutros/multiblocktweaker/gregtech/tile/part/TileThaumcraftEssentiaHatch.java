package eutros.multiblocktweaker.gregtech.tile.part;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCAspectTank;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IAspectTank;
import eutros.multiblocktweaker.gregtech.client.MBTTextures;
import eutros.multiblocktweaker.gregtech.tile.holders.ThaumcraftEssentiaHatchHolder;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaTransport;

@SuppressWarnings("InstantiationOfUtilityClass")
public class TileThaumcraftEssentiaHatch extends AbstractTileModHatch<TileThaumcraftEssentiaHatch> implements IAspectSource, IEssentiaTransport {
    public static final MultiblockAbility<TileThaumcraftEssentiaHatch> INPUT_THAUMCRAFT_ESSENTIA = new MultiblockAbility<>("import_thaumcraft_aspect");
    public static final MultiblockAbility<TileThaumcraftEssentiaHatch> OUTPUT_THAUMCRAFT_ESSENTIA = new MultiblockAbility<>("export_thaumcraft_aspect");

    protected final MCAspectTank container;
    protected Aspect aspectFilter;

    public TileThaumcraftEssentiaHatch(ResourceLocation metaTileEntityId, int tier, boolean isOutputHatch){
        super(metaTileEntityId, tier, isOutputHatch, INPUT_THAUMCRAFT_ESSENTIA, OUTPUT_THAUMCRAFT_ESSENTIA, ThaumcraftEssentiaHatchHolder.class, "aspect");
        this.container = new MCAspectTank(250 * tier);
    }

    public int getCapacityByTier() {
        return getTier() * 250;
    }

    public int getMaxTransferByTier() {
        return getTier() * 5;
    }


    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new TileThaumcraftEssentiaHatch(metaTileEntityId, getTier(), isOutputHatch);
    }

    @Override
    public ICubeRenderer getUnFormedBaseTexture() {
        return MBTTextures.THAUMCRAFT_ESSENTIA_BASE[getTier() - 1];
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (shouldRenderOverlay()) {
            (isOutputHatch ? MBTTextures.THAUMCRAFT_ESSENTIA_OUT : MBTTextures.BOTANIA_MANA_IN).renderSided(getFrontFacing(), renderState, translation, pipeline);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (this.container.getInternal() != null) {
            nbttagcompound.setString("Aspect", this.container.getInternal().getTag());
        }

        if (this.aspectFilter != null) {
            nbttagcompound.setString("AspectFilter", this.aspectFilter.getTag());
        }

        nbttagcompound.setInteger("Amount", (short)this.getAmount());
        data.setTag("mbt_hatch_aspect", nbttagcompound);
        return super.writeToNBT(data);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        NBTTagCompound compound = data.getCompoundTag("mbt_hatch_aspect");
        this.container.setAmount(compound.getInteger("Amount"));
        this.container.setAspect(Aspect.getAspect(compound.getString("Aspect")));
        this.aspectFilter = Aspect.getAspect(compound.getString("AspectFilter"));
    }

    public IAspectTank getContainer() {
        return container;
    }

    @Override
    public void update() {
        super.update();
        if (getOffsetTimer() % 5 == 0) {
            TileEntity te = getWorld().getTileEntity(getPos().offset(getFrontFacing()));
            te = te instanceof IEssentiaTransport && ((IEssentiaTransport)te).isConnectable(getFrontFacing().getOpposite()) ? te : null;
            if (te != null) {
                IEssentiaTransport transport = (IEssentiaTransport)te;
                EnumFacing facing = getFrontFacing();
                if (!isOutputHatch) {
                    if (transport.canOutputTo(facing)) {
                        Aspect available = null;
                        if (this.aspectFilter != null) {
                            available = this.aspectFilter;
                        } else if (this.container.getInternal() != null && this.getAmount() > 0) {
                            available = this.container.getInternal();
                        } else if (transport.getEssentiaAmount(facing) > 0 &&
                                transport.getSuctionAmount(facing) < this.getSuctionAmount(facing.getOpposite()) &&
                                this.getSuctionAmount(facing.getOpposite()) >= transport.getMinimumSuction()) {
                            available = transport.getEssentiaType(facing);
                        }
                        if (available != null && transport.getSuctionAmount(facing) < this.getSuctionAmount(facing.getOpposite())) {
                            this.addToContainer(available, transport.takeEssentia(available, container.getCapacity() / 50, EnumFacing.DOWN));
                        }
                    }
                }
            }
        }
    }

    public int getAmount() {
        return this.container.getAmount();
    }

    @Override
    public boolean isBlocked() {
        return false;
    }

    @Override
    public AspectList getAspects() {
        AspectList all = new AspectList();
        if (this.container.getInternal() != null && this.getAmount() > 0) {
            all.add(this.container.getInternal(), this.getAmount());
        }
        return all;
    }

    @Override
    public void setAspects(AspectList aspects) {
        if (aspects != null && aspects.size() > 0) {
            this.container.setAmount(aspects.getAmount(aspects.getAspectsSortedByAmount()[0]));
            this.container.setAspect(aspects.getAspectsSortedByAmount()[0]);
        }
    }

    @Override
    public boolean doesContainerAccept(Aspect tag) {
        return this.aspectFilter == null || tag.equals(this.aspectFilter);
    }

    @Override
    public int addToContainer(Aspect aspect, int amount) {
        if (amount != 0) {
            if (this.getAmount() < container.getCapacity() && aspect == this.container.getInternal() || this.getAmount() == 0) {
                int added = Math.min(amount, container.getCapacity() - this.getAmount());
                this.container.setAmount(this.getAmount() + added);
                this.container.setAspect(aspect);
                amount -= added;
            }
            markDirty();
        }
        return amount;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        if (this.getAmount() >= amount && aspect == this.container.getInternal()) {
            this.container.setAmount(getAmount() - amount);
            if (this.getAmount() <= 0) {
                this.container.setAspect(null);
                this.container.setAmount(0);
            }
            markDirty();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean takeFromContainer(AspectList aspects) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return this.getAmount() >= amount && aspect == this.container.getInternal();
    }

    @Override
    public boolean doesContainerContain(AspectList aspects) {
        for (Aspect aspect : aspects.getAspects()) {
            if (this.getAmount() > 0 && aspect == this.container.getInternal()) {
                return true;
            }
        }
        return false;
    }

    public int containerContains(Aspect aspect) {
        return aspect == this.container.getInternal() ? this.getAmount() : 0;
    }

    public boolean isConnectable(EnumFacing face) {
        return face == getFrontFacing();
    }

    public boolean canInputFrom(EnumFacing face) {
        return !isOutputHatch && isConnectable(face);
    }

    public boolean canOutputTo(EnumFacing face) {
        return isOutputHatch && isConnectable(face);
    }

    public void setSuction(Aspect aspect, int amount) {
    }

    public int getMinimumSuction() {
        return this.aspectFilter != null ? 64 : 32;
    }

    public Aspect getSuctionType(EnumFacing face) {
        return this.aspectFilter != null ? this.aspectFilter : this.container.getInternal();
    }

    public int getSuctionAmount(EnumFacing face) {
        if (this.getAmount() < container.getCapacity()) {
            return this.aspectFilter != null ? 64 : 32;
        } else {
            return 0;
        }
    }

    public Aspect getEssentiaType(EnumFacing face) {
        return this.container.getInternal();
    }

    public int getEssentiaAmount(EnumFacing face) {
        return this.getAmount();
    }

    public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
        return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
    }

    public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
        return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
    }
}
