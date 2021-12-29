package eutros.multiblocktweaker.gregtech.tile.holders;

import eutros.multiblocktweaker.gregtech.tile.part.AbstractTileModHatch;
import gregtech.api.metatileentity.MetaTileEntityHolder;

public class AbstractHatchHolder<T extends AbstractTileModHatch<?>> extends MetaTileEntityHolder {

    public void setAbstractTileModHatch(AbstractTileModHatch<?> modHatch) {
        this.setRawMetaTileEntity(modHatch);
    }

    @Override
    public T getMetaTileEntity() {
        return (T)super.getMetaTileEntity();
    }
    
}
