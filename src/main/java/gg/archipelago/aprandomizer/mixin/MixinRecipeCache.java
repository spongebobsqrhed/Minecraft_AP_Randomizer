package gg.archipelago.aprandomizer.mixin;

import static gg.archipelago.aprandomizer.APRandomizer.LOGGER;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gg.archipelago.aprandomizer.APRandomizer;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeCache;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

@Mixin(RecipeCache.class)
public class MixinRecipeCache {

	
	@Inject(method = "get(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/CraftingInput;)Ljava/util/Optional;", at = @At("RETURN"), cancellable = true)
	private void CheckRecipe(Level p_311354_, CraftingInput p_342819_, CallbackInfoReturnable<Optional<RecipeHolder<CraftingRecipe>>> cir) {
		var result = cir.getReturnValue();
		//LOGGER.debug("The original value: "+result);
		if(APRandomizer.getRecipeManager().getRestrictedRecipes().contains(result.orElse(null))) {
			cir.setReturnValue(Optional.empty());
		}
	}
}
