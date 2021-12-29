package eutros.multiblocktweaker.gregtech.tile.holders;

import eutros.multiblocktweaker.gregtech.tile.part.TileThaumcraftEssentiaHatch;
import net.minecraft.util.EnumFacing;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaTransport;

public class ThaumcraftEssentiaHatchHolder extends AbstractHatchHolder<TileThaumcraftEssentiaHatch> implements IAspectSource, IEssentiaTransport {

    @Override
    public boolean isBlocked() {
        return getMetaTileEntity().isBlocked();
    }

    @Override
    public AspectList getAspects() {
        return getMetaTileEntity().getAspects();
    }

    @Override
    public void setAspects(AspectList aspects) {
        getMetaTileEntity().setAspects(aspects);
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return getMetaTileEntity().doesContainerAccept(aspect);
    }

    @Override
    public int addToContainer(Aspect aspect, int amount) {
        return getMetaTileEntity().addToContainer(aspect, amount);
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        return getMetaTileEntity().takeFromContainer(aspect, amount);
    }

    @Override
    public boolean takeFromContainer(AspectList aspects) {
        return getMetaTileEntity().takeFromContainer(aspects);
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return getMetaTileEntity().doesContainerContainAmount(aspect, amount);
    }

    @Override
    public boolean doesContainerContain(AspectList aspects) {
        return getMetaTileEntity().doesContainerContain(aspects);
    }

    @Override
    public int containerContains(Aspect aspect) {
        return getMetaTileEntity().containerContains(aspect);
    }

    @Override
    public boolean isConnectable(EnumFacing side) {
        return getMetaTileEntity().isConnectable(side);
    }

    @Override
    public boolean canInputFrom(EnumFacing side) {
        return getMetaTileEntity().canInputFrom(side);
    }

    @Override
    public boolean canOutputTo(EnumFacing side) {
        return getMetaTileEntity().canOutputTo(side);
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {
        getMetaTileEntity().setSuction(aspect, amount);
    }

    @Override
    public Aspect getSuctionType(EnumFacing side) {
        return getMetaTileEntity().getSuctionType(side);
    }

    @Override
    public int getSuctionAmount(EnumFacing side) {
        return getMetaTileEntity().getSuctionAmount(side);
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, EnumFacing side) {
        return getMetaTileEntity().takeEssentia(aspect, amount, side);
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing side) {
        return getMetaTileEntity().addEssentia(aspect, amount, side);
    }

    @Override
    public Aspect getEssentiaType(EnumFacing side) {
        return getMetaTileEntity().getEssentiaType(side);
    }

    @Override
    public int getEssentiaAmount(EnumFacing side) {
        return getMetaTileEntity().getEssentiaAmount(side);
    }

    @Override
    public int getMinimumSuction() {
        return getMetaTileEntity().getMinimumSuction();
    }
}
