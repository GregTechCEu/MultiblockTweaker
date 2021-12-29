package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

/**
 * Aspect Tank, the container of the AspectStack.
 *
 * @zenClass mods.thaumcraft.AspectTank
 */
@ZenClass("mods.thaumcraft.AspectTank")
@ZenRegister
public interface IAspectTank extends IAspectStack{
    /**
     * Set amount of it.
     * @param amount amount.
     */
    @ZenMethod
    IAspectStack receive(IAspectStack aspectStack);

    @ZenMethod
    @ZenGetter("capacity")
    int getCapacity();

    @ZenMethod
    @ZenSetter("capacity")
    void setCapacity(int capacity);
}
