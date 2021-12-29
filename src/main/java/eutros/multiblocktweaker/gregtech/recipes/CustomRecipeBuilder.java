package eutros.multiblocktweaker.gregtech.recipes;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IAspectStack;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.ValidationResult;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.AspectList;

import java.util.HashMap;
import java.util.Map;

public class CustomRecipeBuilder extends RecipeBuilder<CustomRecipeBuilder> {

    Map<CustomRecipeProperty, Object> properties;
    Integer FEt;
    Integer MANAt;
    AspectList aspectInputs;
    AspectList aspectOutputs;

    private CustomRecipeBuilder(CustomRecipeBuilder builder, Map<CustomRecipeProperty, Object> recipeProperties) {
        super(builder);
        this.properties = recipeProperties;
        FEt = builder.FEt;
        MANAt = builder.MANAt;
        aspectInputs = builder.aspectInputs;
        aspectInputs = builder.aspectOutputs;
    }

    public CustomRecipeBuilder(CustomRecipeProperty[] recipeProperties) {
        if (recipeProperties.length > 0) {
            properties = new HashMap<>();
            for (CustomRecipeProperty recipeProperty : recipeProperties) {
                properties.put(recipeProperty, null);
            }
        }
    }

    public void FEt(int FEt) {
        this.FEt = FEt;
    }

    public void MANAt(int MANAt) {
        this.MANAt = MANAt;
    }

    public void aspectInputs(IAspectStack... inputs) {
        aspectInputs = new AspectList();
        for (IAspectStack input : inputs) {
            aspectInputs.merge(input.getInternal(), input.getAmount());
        }
    }

    public void aspectOutputs(IAspectStack... outputs) {
        aspectOutputs = new AspectList();
        for (IAspectStack output : outputs) {
            aspectOutputs.merge(output.getInternal(), output.getAmount());
        }
    }

    @Override
    @NotNull
    public CustomRecipeBuilder copy() {
        return new CustomRecipeBuilder(this, properties);
    }

    @Override
    @NotNull
    public ValidationResult<Recipe> build() {
        Recipe recipe = new CustomRecipe(this.inputs,
                this.outputs,
                this.chancedOutputs,
                this.fluidInputs,
                this.fluidOutputs,
                aspectInputs == null ? new AspectList() : aspectInputs,
                aspectOutputs == null ? new AspectList() : aspectOutputs,
                FEt == null ? 0 : FEt,
                MANAt == null ? 0 : MANAt,
                this.duration,
                this.EUt,
                this.hidden
        );
        if (properties != null) {
            for (Map.Entry<CustomRecipeProperty, Object> entry : properties.entrySet()) {
                if (!recipe.setProperty(entry.getKey(), entry.getValue())) {
                    return ValidationResult.newResult(EnumValidationResult.INVALID, recipe); 
                }
            }
        }
        return ValidationResult.newResult(finalizeAndValidate(), recipe);
    }

    @Override
    public boolean applyProperty(String key, Object value) {
        if (properties == null) {
            return false;
        }
        CustomRecipeProperty property = properties.keySet().stream().filter(p->p.getKey().equals(key)).findFirst().orElse(null);
        if (property == null) {
            return false;
        }
        properties.put(property, value);
        return true;
    }

}
