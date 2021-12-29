//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package thaumcraft.api.aspects;

import net.minecraft.item.ItemStack;

public interface IEssentiaContainerItem {
    AspectList getAspects(ItemStack var1);

    void setAspects(ItemStack var1, AspectList var2);

    boolean ignoreContainedAspects();
}
