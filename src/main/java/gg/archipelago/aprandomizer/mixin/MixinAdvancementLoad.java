package gg.archipelago.aprandomizer.mixin;

import static gg.archipelago.aprandomizer.APRandomizer.LOGGER;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;

import gg.archipelago.aprandomizer.managers.advancementmanager.AdvancementManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;

@Mixin(ServerAdvancementManager.class)
public abstract class MixinAdvancementLoad {

	@Shadow
	public Map<ResourceLocation, AdvancementHolder> advancements;
	
	@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"))
	private void loadAdvancements(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager,
			ProfilerFiller pProfiler, CallbackInfo ci) {
			LOGGER.info("Loading Advancements");
		try {
			LOGGER.debug("Advancement Load Found.");
			Builder<ResourceLocation, AdvancementHolder> newAdvancements = ImmutableMap.builder();
			Style hardStyle = Style.EMPTY.withBold(true).withColor(TextColor.parseColor("#FFA500").getOrThrow());
			LOGGER.debug("Style Created");
			Component hardText = Component.literal(" (Hard)").withStyle(hardStyle);
			LOGGER.debug("Component Created");
			// var test = advancements.;
			LOGGER.debug("HashMap Created");
			var advIterator = advancements.values().iterator();
			LOGGER.debug("Iterating.");
			while (advIterator.hasNext()) {
				// LOGGER.debug("Next");
				var holder = advIterator.next();
				// LOGGER.debug("Advancement: " + advancement);
				if (AdvancementManager.hardAdvancements.contains(holder.id())) {
					LOGGER.debug("Found hard advancement!");
					// Creating local variables for easy use
					var advancement = holder.value();
					var display = advancement.display().get();
					LOGGER.debug("Hard advancement " + advancement.display().get().getTitle().getString() + " found.");
					// Building new display
					var title = display.getTitle().copy().append(hardText);
					var newDisplay = new DisplayInfo(display.getIcon(), title, display.getDescription(),
							display.getBackground(), display.getType(), true, false, false);
					// Remaking the advancement
					var newAdvancement = Advancement.Builder.advancement().display(newDisplay)
							.parent(advancements.get(advancement.parent().get()))
							.requirements(advancement.requirements()).rewards(advancement.rewards());
					// Adding the criteria
					advancement.criteria().forEach((key, value) -> {
						newAdvancement.addCriterion(key, value);
					});
					// Adding to the new advancement list
					newAdvancements.put(holder.id(), newAdvancement.build(holder.id()));
					LOGGER.debug("Hard advancement " + newDisplay.getTitle().getString() + " modified.");
					// LOGGER.debug("Comparison for checking: " + holder.value() + "\n" +
					// newAdvancement.build(holder.id()).value());
				} else if (!holder.id().getPath().startsWith("recipes/"))
					newAdvancements.put(holder.id(), holder);
			}
			advancements = newAdvancements.buildOrThrow();
		} catch (Exception E) {
			LOGGER.error("Exception caught! " + E.getMessage() + " Other output: " + E);
			throw (E);
		}
	}
}
