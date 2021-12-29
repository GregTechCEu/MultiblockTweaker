package eutros.multiblocktweaker.gregtech.tile.holders;

import eutros.multiblocktweaker.gregtech.tile.part.TileBotaniaManaHatch;
import net.minecraft.item.EnumDyeColor;
import vazkii.botania.api.mana.IManaPool;

public class BotaniaManaHatchHolder extends AbstractHatchHolder<TileBotaniaManaHatch> implements IManaPool {

    @Override
    public boolean isOutputtingPower() {
        return false;
    }

    @Override
    public EnumDyeColor getColor() {
        return getMetaTileEntity().getColor();
    }

    @Override
    public void setColor(EnumDyeColor color) {
        getMetaTileEntity().setColor(color);
    }

    @Override
    public boolean isFull() {
        return getMetaTileEntity().getEnergyStored() == getMetaTileEntity().getMaxEnergyStored();
    }

    @Override
    public void recieveMana(int mana) {
        getMetaTileEntity().receiveEnergy(mana, false);
    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return getMetaTileEntity().maxReceive > 0;
    }

    @Override
    public int getCurrentMana() {
        return getMetaTileEntity().getEnergyStored();
    }
}
