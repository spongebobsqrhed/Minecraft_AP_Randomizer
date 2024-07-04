package gg.archipelago.aprandomizer;

import java.util.Optional;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;

public class ResourcePackLoader {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void findSubPacks(Consumer<Pack> packAcceptor) {
		LOGGER.debug("Entering subpack search");
		var version = SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA);
		var mod = ModList.get().getModFileById(APRandomizer.MODID);
		if (mod.requiredLanguageLoaders().stream().anyMatch(ls -> ls.languageName().equals("minecraft")))
			return;

		var file = mod.getFile();

		var root = file.findResource("datapack");
		root.forEach((iPack)->{
			LOGGER.debug("Searching "+ iPack.toFile().getAbsolutePath());
		
		var supplier = new PathPackResources.PathResourcesSupplier(iPack);

		var modinfo = file.getModInfos().get(0);
		var name = "mod:" + modinfo.getModId() + " thingy";
		var info = new PackLocationInfo(name, Component.literal(file.getFileName()), PackSource.DEFAULT,
				Optional.empty());
		var meta = Pack.readPackMetadata(info, supplier, version);
		Pack pack = null;
		if (meta != null)
			pack = new Pack(info, supplier, meta, new PackSelectionConfig(false, Position.BOTTOM, false));

		if (pack == null) {
			// Vanilla only logs an error, instead of propagating, so handle null and warn
			// that something went wrong
			ModLoader.get().addWarning(
					new ModLoadingWarning(modinfo, ModLoadingStage.ERROR, "fml.modloading.brokenresources", file));
			return;
		}
		
		LOGGER.debug(Logging.CORE, "Generating PackInfo named {} for mod file {}", name, file.getFilePath());
		
			packAcceptor.accept(pack);
		
		});
		
	}
}
