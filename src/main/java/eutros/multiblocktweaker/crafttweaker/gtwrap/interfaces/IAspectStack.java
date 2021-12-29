package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCAspectStack;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;
import stanhebben.zenscript.annotations.ZenSetter;
import thaumcraft.api.aspects.Aspect;

/**
 * Aspect from Thaumcraft.
 *
 * @zenClass mods.thaumcraft.AspectStack
 */
@ZenClass("mods.thaumcraft.AspectStack")
@ZenRegister
public interface IAspectStack {

    Aspect getInternal();

    @ZenMethod
    @ZenGetter("name")
    String getName();

    @ZenMethod
    void setAspectByName(String name);

    /**
     * Get amount of it.
     * @return amount.
     */
    @ZenMethod
    @ZenGetter("amount")
    int getAmount();

    /**
     * Set amount of it.
     * @param amount amount.
     */
    @ZenMethod
    @ZenSetter("amount")
    void setAmount(int amount);

    /**
     * Create an AspectStack with amount 1.
     * @param aspect aspect.
     * @return instance.
     */
    @ZenMethod
    static IAspectStack create(String aspect) {
        return create(aspect, 1);
    }

    /**
     * Create an AspectStack with specific amount.
     * @param aspect aspect.
     * @param amount amount.
     * @return instance.
     */
    @ZenMethod
    static IAspectStack create(String aspect, int amount) {
        return new MCAspectStack(Aspect.getAspect(aspect), amount);
    }

    @ZenOperator(OperatorType.MUL)
    default IAspectStack multi(int other) {
        setAmount(getAmount() * other);
        return this;
    }
}
