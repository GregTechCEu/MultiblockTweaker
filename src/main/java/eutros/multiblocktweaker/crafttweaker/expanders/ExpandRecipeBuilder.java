package eutros.multiblocktweaker.crafttweaker.expanders;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IAspectStack;
import eutros.multiblocktweaker.gregtech.recipes.CustomRecipeBuilder;
import gregtech.api.recipes.crafttweaker.CTRecipeBuilder;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("mods.gregtech.recipe.RecipeBuilder")
public class ExpandRecipeBuilder {

    @ZenMethod
    public static CTRecipeBuilder FEt(CTRecipeBuilder builder, int FEt) {
        if (builder.backingBuilder instanceof CustomRecipeBuilder) {
            ((CustomRecipeBuilder) builder.backingBuilder).FEt(FEt);
        }
        return builder;
    }

    @ZenMethod
    public static CTRecipeBuilder MANAt(CTRecipeBuilder builder, int MANAt) {
        if (builder.backingBuilder instanceof CustomRecipeBuilder) {
            ((CustomRecipeBuilder) builder.backingBuilder).MANAt(MANAt);
        }
        return builder;
    }

    @ZenMethod
    public static CTRecipeBuilder aspectInputs(CTRecipeBuilder builder, IAspectStack... inputs) {
        if (builder.backingBuilder instanceof CustomRecipeBuilder) {
            ((CustomRecipeBuilder) builder.backingBuilder).aspectInputs(inputs);
        }
        return builder;
    }

    @ZenMethod
    public static CTRecipeBuilder aspectOutputs(CTRecipeBuilder builder, IAspectStack... outputs) {
        if (builder.backingBuilder instanceof CustomRecipeBuilder) {
            ((CustomRecipeBuilder) builder.backingBuilder).aspectOutputs(outputs);
        }
        return builder;
    }
}
