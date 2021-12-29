package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IAspectStack;
import thaumcraft.api.aspects.Aspect;

public class MCAspectStack implements IAspectStack {
    private Aspect aspect;
    private int amount;

    public MCAspectStack(Aspect aspect, int amount) {
        this.aspect = aspect;
        this.amount = amount;
    }

    public MCAspectStack() {
        this.aspect = null;
        this.amount = 0;
    }

    public void setAspect(Aspect aspect) {
        this.aspect = aspect;
    }

    @Override
    public Aspect getInternal() {
        return aspect;
    }

    @Override
    public String getName() {
        return aspect == null ? null : aspect.getName();
    }

    @Override
    public void setAspectByName(String name) {
        setAspect(Aspect.getAspect(name));
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
