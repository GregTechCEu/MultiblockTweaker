package eutros.multiblocktweaker.gregtech.recipes;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IAspectStack;
import gregtech.api.recipes.FluidKey;
import gregtech.api.recipes.KeySharedStack;
import gregtech.api.recipes.MatchingMode;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.ItemStackKey;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecipeMapMultiblock extends RecipeMap<CustomRecipeBuilder> {
    private final Object2ObjectOpenHashMap<FluidKey, Set<Recipe>> recipeFluidMap;
    private final Object2ObjectOpenHashMap<ItemStackKey, Set<Recipe>> recipeItemMap;

    public RecipeMapMultiblock(String unlocalizedName, int minInputs, int maxInputs, int minOutputs, int maxOutputs, int minFluidInputs, int maxFluidInputs, int minFluidOutputs, int maxFluidOutputs, CustomRecipeBuilder defaultRecipe, boolean isHidden) {
        super(unlocalizedName, minInputs, maxInputs, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, isHidden);
        recipeFluidMap = ObfuscationReflectionHelper.getPrivateValue(RecipeMap.class, this, "recipeFluidMap");
        recipeItemMap = ObfuscationReflectionHelper.getPrivateValue(RecipeMap.class, this, "recipeItemMap");
    }

    public Recipe findRecipe(long voltage,
                              int fe,
                              int mana,
                              List<ItemStack> inputs,
                              List<FluidStack> fluidInputs,
                              List<IAspectStack> aspects,
                              MatchingMode matchingMode) {
        HashSet<Recipe> iteratedRecipes = new HashSet<>();
        HashSet<ItemStackKey> searchedItems = new HashSet<>();
        HashSet<FluidKey> searchedFluids = new HashSet<>();
        HashMap<Integer, LinkedList<Recipe>> priorityRecipeMap = new HashMap<>();
        HashMap<Recipe, Integer> promotedTimes = new HashMap<>();

        if (matchingMode != MatchingMode.IGNORE_ITEMS) {
            for (ItemStack stack : inputs) {
                if (!stack.isEmpty()) {
                    ItemStackKey itemStackKey = KeySharedStack.getRegisteredStack(stack);
                    if (!searchedItems.contains(itemStackKey) && recipeItemMap.containsKey(itemStackKey)) {
                        searchedItems.add(itemStackKey);
                        for (Recipe tmpRecipe : recipeItemMap.get(itemStackKey)) {
                            CustomRecipe customRecipe = (CustomRecipe) tmpRecipe;
                            if (voltage < customRecipe.getEUt() || fe < customRecipe.FEt || mana < customRecipe.MANAt) {
                                continue;
                            }
                            calculateRecipePriority(tmpRecipe, promotedTimes, priorityRecipeMap);
                        }
                    }
                }
            }
        }

        if (matchingMode != MatchingMode.IGNORE_FLUIDS) {
            for (FluidStack fluidStack : fluidInputs) {
                if (fluidStack != null) {
                    FluidKey fluidKey = new FluidKey(fluidStack);
                    if (!searchedFluids.contains(fluidKey) && recipeFluidMap.containsKey(fluidKey)) {
                        searchedFluids.add(fluidKey);
                        for (Recipe tmpRecipe : recipeFluidMap.get(fluidKey)) {
                            CustomRecipe customRecipe = (CustomRecipe) tmpRecipe;
                            if (voltage < customRecipe.getEUt() || fe < customRecipe.FEt || mana < customRecipe.MANAt) {
                                continue;
                            }
                            calculateRecipePriority(tmpRecipe, promotedTimes, priorityRecipeMap);
                        }
                    }
                }
            }
        }
        return prioritizedRecipe(priorityRecipeMap, iteratedRecipes, inputs, fluidInputs, aspects, matchingMode);
    }

    private Recipe prioritizedRecipe(Map<Integer, LinkedList<Recipe>> priorityRecipeMap, HashSet<Recipe> iteratedRecipes, List<ItemStack> inputs, List<FluidStack> fluidInputs, List<IAspectStack> aspectList, MatchingMode matchingMode) {
        for (int i = priorityRecipeMap.size(); i >= 0; i--) {
            if (priorityRecipeMap.containsKey(i)) {
                for (Recipe tmpRecipe : priorityRecipeMap.get(i)) {
                    if (iteratedRecipes.add(tmpRecipe)) {
                        CustomRecipe customRecipe = (CustomRecipe) tmpRecipe;
                        if (customRecipe.matches(false, inputs, fluidInputs, aspectList, matchingMode)) {
                            return customRecipe;
                        }
                    }
                }
            }
        }

        return null;
    }

    private void calculateRecipePriority(Recipe recipe, HashMap<Recipe, Integer> promotedTimes, Map<Integer, LinkedList<Recipe>> priorityRecipeMap) {
        Integer p = promotedTimes.get(recipe);
        if (p == null) {
            p = 0;
        }
        promotedTimes.put(recipe, p + 1);
        priorityRecipeMap.computeIfAbsent(p, k -> new LinkedList<>()).add(recipe);
    }

}
