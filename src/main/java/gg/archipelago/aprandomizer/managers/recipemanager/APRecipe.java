package gg.archipelago.aprandomizer.managers.recipemanager;

import java.util.Set;

import net.minecraft.world.item.crafting.RecipeHolder;

public interface APRecipe {

    Set<RecipeHolder<?>> getGrantedRecipes();

}
