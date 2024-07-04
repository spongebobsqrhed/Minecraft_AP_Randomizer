package gg.archipelago.aprandomizer.events;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gg.archipelago.aprandomizer.APRandomizer;
import gg.archipelago.aprandomizer.capability.data.WorldData;
import gg.archipelago.aprandomizer.common.Utils.Utils;
import dev.koifysh.archipelago.Print.APPrintColor;
import dev.koifysh.archipelago.events.ArchipelagoEventListener;
import dev.koifysh.archipelago.events.ReceiveItemEvent;
import dev.koifysh.archipelago.parts.NetworkItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;

public class ReceiveItem {

	private static final Logger LOGGER = LogManager.getLogger();
	static int index;
	static HashMap<String, Integer> Items = new HashMap<String, Integer>();
	static Scoreboard Board;

	@ArchipelagoEventListener
	public static void onReceiveItem(ReceiveItemEvent event) {
		NetworkItem item = event.getItem();
		String itemName = item.itemName;

		Component textItem = Component.literal(item.itemName)
				.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(APPrintColor.gold.color.getRGB())));
		Component chatMessage = Component.literal("Received ")
				.withStyle(Style.EMPTY.withColor(ChatFormatting.getByName("red"))).append(item.itemName)
				.withStyle(Style.EMPTY.withColor(ChatFormatting.getByName("gold"))).append(" from ")
				.withStyle(Style.EMPTY.withColor(ChatFormatting.getByName("red"))).append(item.playerName)
				.withStyle(Style.EMPTY.withColor(ChatFormatting.getByName("gold"))).append(" (")
				.withStyle(Style.EMPTY.withColor(ChatFormatting.getByName("red"))).append(item.locationName)
				.withStyle(Style.EMPTY.withColor(ChatFormatting.getByName("blue"))).append(")")
				.withStyle(Style.EMPTY.withColor(ChatFormatting.getByName("red")));
		Component title = Component.literal("Received")
				.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(APPrintColor.red.color.getRGB())));
		if ((APRandomizer.worldData.getInformMode() & 1) == WorldData.TITLE)
			Utils.sendTitleToAll(title, textItem, chatMessage, 10, 60, 10);
		else
			Utils.sendMessageToAll(chatMessage);
		if (!Items.containsKey(itemName))
			Items.put(itemName, 1);

		int num = Items.get(itemName);
		Items.put(itemName, num + 1);
		if (num > 1)
			itemName += " #" + num;

		Board.getOrCreatePlayerScore(ScoreHolder.forNameOnly(itemName), Board.getObjective("Received Items"))
				.set(index);

		APRandomizer.getRecipeManager().grantRecipe(item.itemID);
		APRandomizer.getItemManager().giveItemToAll(item.itemID);
		APRandomizer.getItemManager().catchUpAll();
		index++;
	}

	public static void SetScoreBoard(Scoreboard pBoard) {
		Board = pBoard;
	}

	public static void SetDisplay(boolean visible) {
		Board.setDisplayObjective(DisplaySlot.SIDEBAR, visible ? Board.getObjective("Received Items") : null);

	}

}
