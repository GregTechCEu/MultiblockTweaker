package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import gregtech.api.pattern.BlockWorldState;
import gregtech.api.pattern.PatternStringError;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public class MCBlockWorldState implements IBlockWorldState {

    @NotNull
    private final BlockWorldState inner;

    public MCBlockWorldState(@NotNull BlockWorldState inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public BlockWorldState getInternal() {
        return inner;
    }

    @Override
    public IPatternMatchContext getMatchContext() {
        return new MCPatternMatchContext(inner.getMatchContext());
    }

    @Override
    public boolean hasError() {
        return inner.hasError();
    }

    @Override
    public void setError(String error) {
        inner.setError(new PatternStringError(error));
    }

    @Override
    public IBlockState getBlockState() {
        return CraftTweakerMC.getBlockState((inner.getBlockState()));
    }

    @Override
    public IBlockPos getPos() {
        return CraftTweakerMC.getIBlockPos(inner.getPos());
    }

    @Override
    public IBlockState getOffsetState(IFacing face) {
        return CraftTweakerMC.getBlockState(inner.getOffsetState((EnumFacing) face.getInternal())); // no helper method for this it seems
    }

    @Override
    public IWorld getWorld() {
        return CraftTweakerMC.getIWorld(inner.getWorld());
    }

}
