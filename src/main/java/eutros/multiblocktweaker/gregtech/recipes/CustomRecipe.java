package eutros.multiblocktweaker.gregtech.recipes;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IAspectStack;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.MatchingMode;
import gregtech.api.recipes.Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.List;
import java.util.Map;

public class CustomRecipe extends Recipe {
    public final int FEt;
    public final int MANAt;
    public final AspectList aspectInputs;
    public final AspectList aspectOutputs;

    public CustomRecipe(
            List<CountableIngredient> inputs,
            List<ItemStack> outputs,
            List<ChanceEntry> chancedOutputs,
            List<FluidStack> fluidInputs,
            List<FluidStack> fluidOutputs,
            AspectList aspectInputs,
            AspectList aspectOutputs,
            int duration, 
            int EUt, 
            int FEt,
            int MANAt,
            boolean hidden) {
        super(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs,
                duration, EUt, hidden);
        this.FEt = FEt;
        this.MANAt = MANAt;
        this.aspectInputs = aspectInputs.copy();
        this.aspectOutputs = aspectOutputs.copy();
    }

    public boolean matches(boolean consumeIfSuccessful, List<ItemStack> inputs, List<FluidStack> fluidInputs, List<IAspectStack> aspectInputs, MatchingMode matchingMode) {
        Pair<Boolean, Integer[]> aspects = matchesAspect(aspectInputs);
        if (super.matches(consumeIfSuccessful, inputs, fluidInputs, matchingMode)){
            if (consumeIfSuccessful && matchingMode == MatchingMode.DEFAULT) {
                Integer[] aspectAmountInTank = aspects.getValue();
                for (int i = 0; i < aspectAmountInTank.length; i++) {
                    IAspectStack aspectStack = aspectInputs.get(i);
                    int aspectAmount = aspectAmountInTank[i];
                    if (aspectStack == null || aspectStack.getAmount() == aspectAmount)
                        continue;
                    aspectStack.setAmount(aspectAmount);
                    if (aspectStack.getAmount() == 0)
                        aspectInputs.set(i, null);
                }
            }
        }
        return true;
    }

    private Pair<Boolean, Integer[]> matchesAspect(List<IAspectStack> aspectInputs) {
        Integer[] aspectAmountInTank = new Integer[aspectInputs.size()];

        for (int i = 0; i < aspectAmountInTank.length; i++) {
            IAspectStack aspectStack = aspectInputs.get(i);
            aspectAmountInTank[i] = aspectStack == null ? 0 : aspectStack.getAmount();
        }

        for (Map.Entry<Aspect, Integer> aspect : this.aspectInputs.aspects.entrySet()) {
            int amount = aspect.getValue();
            boolean isNotConsumed = false;
            if (amount == 0) {
                amount = 1;
                isNotConsumed = true;
            }
            for (int i = 0; i < aspectInputs.size(); i++) {
                IAspectStack aspectStack = aspectInputs.get(i);
                if (aspectStack == null || !(aspectStack.getInternal() == aspect.getKey()))
                    continue;
                int aspectAmountToConsume = Math.min(aspectAmountInTank[i], amount);
                amount -= aspectAmountToConsume;
                if (!isNotConsumed) aspectAmountInTank[i] -= aspectAmountToConsume;
                if (amount == 0) break;
            }
            if (amount > 0)
                return Pair.of(false, aspectAmountInTank);
        }
        return Pair.of(true, aspectAmountInTank);
    }
}
