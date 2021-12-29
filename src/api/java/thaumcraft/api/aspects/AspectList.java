//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package thaumcraft.api.aspects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class AspectList implements Serializable {
    public LinkedHashMap<Aspect, Integer> aspects;

    public AspectList(ItemStack stack) {
    }

    public AspectList() {
    }

    public AspectList copy() {
        return this;
    }

    public int size() {
        return 0;
    }

    public int visSize() {
        return 0;
    }

    public Aspect[] getAspects() {
        return new Aspect[0];
    }

    public Aspect[] getAspectsSortedByName() {
        return new Aspect[0];
    }

    public Aspect[] getAspectsSortedByAmount() {
        return new Aspect[0];
    }

    public int getAmount(Aspect key) {
        return 0;
    }

    public boolean reduce(Aspect key, int amount) {
        return false;
    }

    public AspectList remove(Aspect key, int amount) {
        return this;
    }

    public AspectList remove(Aspect key) {
        return this;
    }

    public AspectList add(Aspect aspect, int amount) {
        return this;
    }

    public AspectList merge(Aspect aspect, int amount) {
        return this;
    }

    public AspectList add(AspectList in) {
        return this;
    }

    public AspectList remove(AspectList in) {
        return this;
    }

    public AspectList merge(AspectList in) {
        return this;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {

    }

    public void readFromNBT(NBTTagCompound nbttagcompound, String label) {

    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {

    }

    public void writeToNBT(NBTTagCompound nbttagcompound, String label) {
    }
}
