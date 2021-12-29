package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IAspectStack;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IAspectTank;

public class MCAspectTank extends MCAspectStack implements IAspectTank {
    private int capacity;

    public MCAspectTank(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public IAspectStack receive(IAspectStack aspectStack) {
        if (aspectStack == null || aspectStack.getInternal() == null) return new MCAspectStack();
        if (aspectStack.getInternal() == getInternal() || getInternal() == null) {
            MCAspectStack result = new MCAspectStack();
            setAspect(aspectStack.getInternal());
            int added = Math.min(aspectStack.getAmount(), getCapacity() - getAmount());
            this.setAmount(this.getAmount() + added);
            result.setAspect(aspectStack.getInternal());
            result.setAmount(aspectStack.getAmount() - added);
            if (result.getAmount() == 0) {
                result.setAspect(null);
            }
            return result;
        }
        return aspectStack;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
