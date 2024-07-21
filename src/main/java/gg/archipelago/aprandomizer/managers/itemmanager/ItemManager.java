package gg.archipelago.aprandomizer.managers.itemmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gg.archipelago.aprandomizer.APRandomizer;
import gg.archipelago.aprandomizer.APStructures;
import gg.archipelago.aprandomizer.capability.APCapabilities;
import gg.archipelago.aprandomizer.capability.data.PlayerData;
import gg.archipelago.aprandomizer.common.Utils.Utils;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.AboutFaceTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.AnvilTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.BeeTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.BlindnessTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.CreeperTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.FakeWither;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.FishFountainTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.GhastTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.GoonTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.LevitateTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.MiningFatigueTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.PhantomTrap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.SandRain;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.Trap;
import gg.archipelago.aprandomizer.managers.itemmanager.traps.WaterTrap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.util.LazyOptional;

public class ItemManager {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final long DRAGON_EGG_SHARD = 45043L;

    private final HashMap<Long, ItemStack> itemStacks = new HashMap<>() {{

    	Registry<Enchantment> Registry = APRandomizer.server.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        put(45015L, new ItemStack(Items.NETHERITE_SCRAP, 8));
        put(45016L, new ItemStack(Items.EMERALD, 8));
        put(45017L, new ItemStack(Items.EMERALD, 4));

        ItemStack channelingBook = EnchantedBookItem.createForEnchantment(
        		new EnchantmentInstance(
        				Registry.getHolderOrThrow(Enchantments.CHANNELING), 1));
        put(45018L, channelingBook);

        ItemStack silkTouchBook = EnchantedBookItem.createForEnchantment(
        		new EnchantmentInstance(
        				Registry.getHolderOrThrow(Enchantments.SILK_TOUCH), 1));
        put(45019L, silkTouchBook);

        ItemStack sharpnessBook = EnchantedBookItem.createForEnchantment(
        		new EnchantmentInstance(
        				Registry.getHolderOrThrow(Enchantments.SHARPNESS), 3));
        put(45020L, sharpnessBook);

        ItemStack piercingBook = EnchantedBookItem.createForEnchantment(
        		new EnchantmentInstance(
        				Registry.getHolderOrThrow(Enchantments.PIERCING), 4));
        put(45021L, piercingBook);

        ItemStack lootingBook = EnchantedBookItem.createForEnchantment(
        		new EnchantmentInstance(
        				Registry.getHolderOrThrow(Enchantments.LOOTING), 3));
        put(45022L, lootingBook);

        ItemStack infinityBook = EnchantedBookItem.createForEnchantment(
        		new EnchantmentInstance(
        				Registry.getHolderOrThrow(Enchantments.INFINITY), 1));
        put(45023L, infinityBook);

        put(45024L, new ItemStack(Items.DIAMOND_ORE, 4));
        put(45025L, new ItemStack(Items.IRON_ORE, 16));
        put(45029L, new ItemStack(Items.ENDER_PEARL, 3));
        put(45004L, new ItemStack(Items.LAPIS_LAZULI, 4));
        put(45030L, new ItemStack(Items.LAPIS_LAZULI, 4));
        put(45031L, new ItemStack(Items.COOKED_PORKCHOP, 16));
        put(45032L, new ItemStack(Items.GOLD_ORE, 8));
        put(45033L, new ItemStack(Items.ROTTEN_FLESH, 8));
        ItemStack theArrow = new ItemStack(Items.ARROW, 1);
        theArrow.applyComponents(DataComponentPatch.builder().set(DataComponents.CUSTOM_NAME, (Component.literal("The Arrow"))).build());
        put(45034L, theArrow);
        put(45035L, new ItemStack(Items.ARROW, 32));
        put(45036L, new ItemStack(Items.SADDLE, 1));

        String[] compassLore = new String[]{"Right click with compass in hand to","cycle to next known structure location."};

        ItemStack villageCompass = new ItemStack(Items.COMPASS, 1);
        makeCompass(villageCompass, APStructures.VILLAGE_TAG);
        addLore(villageCompass, "Structure Compass (Village)", compassLore);
        put(45037L, villageCompass);

        ItemStack outpostCompass = new ItemStack(Items.COMPASS, 1);
        makeCompass(outpostCompass, APStructures.OUTPOST_TAG);
        addLore(outpostCompass, "Structure Compass (Pillager Outpost)", compassLore);
        put(45038L, outpostCompass);

        ItemStack fortressCompass = new ItemStack(Items.COMPASS, 1);
        makeCompass(fortressCompass, APStructures.FORTRESS_TAG);
        addLore(fortressCompass, "Structure Compass (Nether Fortress)", compassLore);
        put(45039L, fortressCompass);

        ItemStack bastionCompass = new ItemStack(Items.COMPASS, 1);
        makeCompass(bastionCompass, APStructures.BASTION_REMNANT_TAG);
        addLore(bastionCompass, "Structure Compass (Bastion Remnant)", compassLore);
        put(45040L,bastionCompass);

        ItemStack endCityCompass = new ItemStack(Items.COMPASS, 1);
        makeCompass(endCityCompass, APStructures.END_CITY_TAG);
        addLore(endCityCompass, "Structure Compass (End City)", compassLore);
        put(45041L, endCityCompass);
        
        ItemStack trialChamberCompass = new ItemStack(Items.COMPASS, 1);
        makeCompass(trialChamberCompass, APStructures.TRIAL_CHAMBERS_TAG);
        addLore(trialChamberCompass, "Structure Compass (Trial Chambers)", compassLore);
        put(45047L, trialChamberCompass);
        
        put(45042L, new ItemStack(Items.SHULKER_BOX, 1)); 
        
    }};

    private final HashMap<Long,TagKey<Structure>> compasses = new HashMap<>() {{
        put(45037L, APStructures.VILLAGE_TAG);
        put(45038L, APStructures.OUTPOST_TAG);
        put(45039L, APStructures.FORTRESS_TAG);
        put(45040L, APStructures.BASTION_REMNANT_TAG);
        put(45041L, APStructures.END_CITY_TAG);
        put(45047L, APStructures.TRIAL_CHAMBERS_TAG);

    }};

    private final HashMap<Long, Integer> xpData = new HashMap<>() {{
        put(45026L, 500);
        put(45027L, 100);
        put(45028L, 50);
    }};

    long index = 45100L;
    private final HashMap<Long, Callable<Trap>> trapData = new HashMap<>() {{
        put(index++, BeeTrap::new);
        put(index++, CreeperTrap::new);
        put(index++, SandRain::new);
        put(index++, FakeWither::new);
        put(index++, GoonTrap::new);
        put(index++, FishFountainTrap::new);
        put(index++, MiningFatigueTrap::new);
        put(index++, BlindnessTrap::new);
        put(index++, PhantomTrap::new);
        put(index++, WaterTrap::new);
        put(index++, GhastTrap::new);
        put(index++, LevitateTrap::new);
        put(index++, AboutFaceTrap::new);
        put(index++, AnvilTrap::new);
    }};

    private ArrayList<Long> receivedItems = new ArrayList<>();

    private final ArrayList<TagKey<Structure>> receivedCompasses = new ArrayList<>();

    private void makeCompass(ItemStack iStack, TagKey<Structure> structureTag) {
    	iStack.applyComponents(DataComponentPatch.builder()
        		.set(DataComponents.CUSTOM_DATA,
        		CustomData.of(CompoundTag.builder()
        		.put("structure", structureTag.location().toString())
        		.build()))
        		.build());
        BlockPos structureCords = new BlockPos(0,0,0);
        Utils.addLodestoneTags(Utils.getStructureWorld(structureTag),structureCords, iStack);
    }

    private void addLore(ItemStack iStack, String name, String[] compassLore) {
        iStack.applyComponents(DataComponentPatch.builder().set(DataComponents.CUSTOM_NAME, Component.literal(name)).build());
       
        ArrayList<Component> itemLoreLines =new ArrayList<Component>();
        for (String s : compassLore) {
            itemLoreLines.add(Component.literal(s));
        }
        ItemLore lore = new ItemLore(itemLoreLines);
        iStack.applyComponents(DataComponentPatch.builder().set(DataComponents.LORE,lore).build());
    }

    public void setReceivedItems(ArrayList<Long> items) {
        this.receivedItems = items;
        for (var item : items) {
            if(compasses.containsKey(item) && !receivedCompasses.contains(compasses.get(item))) {
                receivedCompasses.add(compasses.get(item));
            }
        }
        APRandomizer.getGoalManager().updateGoal(false);
    }

    public void giveItem(Long itemID, ServerPlayer player) {
        if (APRandomizer.isJailPlayers()) {
            //dont send items to players if game has not started.
            return;
        }
        //update the player's index of received items for syncing later.
        LazyOptional<PlayerData> loPlayerData = player.getCapability(APCapabilities.PLAYER_INDEX);
        if (loPlayerData.isPresent()) {
            PlayerData playerData = loPlayerData.orElseThrow(AssertionError::new);
            playerData.setIndex(receivedItems.size());
        }

        if (itemStacks.containsKey(itemID)) {
            ItemStack itemstack = itemStacks.get(itemID).copy();
            if(compasses.containsKey(itemID)){
                TagKey<Structure> tag = TagKey.create(Registries.STRUCTURE,  ResourceLocation.parse(itemstack.get(DataComponents.CUSTOM_DATA).copyTag().getString("structure")));
                updateCompassLocation(tag, player , itemstack);
            }
            Utils.giveItemToPlayer(player, itemstack);
        } else if (xpData.containsKey(itemID)) {
            int xpValue = xpData.get(itemID);
            player.giveExperiencePoints(xpValue);
        } else if (trapData.containsKey(itemID)) {
            try {
                trapData.get(itemID).call().trigger(player);
            } catch (Exception ignored) {
            }
        }
    }


    public void giveItemToAll(long itemID) {

        receivedItems.add(itemID);
        //check if this item is a structure compass, and we are not already tracking that one.
        if(compasses.containsKey(itemID) && !receivedCompasses.contains(compasses.get(itemID))) {
            receivedCompasses.add(compasses.get(itemID));
        }
        /*
        APRandomizer.getServer().execute(() -> {
            for (ServerPlayer serverplayerentity : APRandomizer.getServer().getPlayerList().getPlayers()) {
                giveItem(itemID, serverplayerentity);
            }
        });
         */
        APRandomizer.getGoalManager().updateGoal(true);
    }

    /***
     fetches the index from the player's capability then makes sure they have all items after that index.
     * @param player ServerPlayer to catch up
     */
    public void catchUpPlayer(ServerPlayer player) {
        LazyOptional<PlayerData> loPlayerData = player.getCapability(APCapabilities.PLAYER_INDEX);
        if (loPlayerData.isPresent()) {
            PlayerData playerData = loPlayerData.orElseThrow(AssertionError::new);
            LOGGER.debug("Player data: "+playerData.getIndex());
            for (int i = playerData.getIndex(); i < receivedItems.size(); i++) {
                giveItem(receivedItems.get(i), player);
            }
        }
    }
    /***
    an attempt to fix item duping by using the catch up function instead of giving items directly
    */
    public void catchUpAll() {
      APRandomizer.getServer().execute(() -> {
            for (ServerPlayer serverplayerentity : APRandomizer.getServer().getPlayerList().getPlayers()) {
                catchUpPlayer(serverplayerentity);
            }
        });

        APRandomizer.getGoalManager().updateGoal(true);
    }
    
    public ArrayList<TagKey<Structure>> getCompasses() {
        return receivedCompasses;
    }

    public ArrayList<Long> getAllItems() {
        return receivedItems;
    }

    public static void updateCompassLocation(TagKey<Structure> structureTag, Player player, ItemStack compass) {

        //get the actual structure data from forge, and make sure its changed to the AP one if needed.

        //get our local custom structure if needed.
        ResourceKey<Level> world = Utils.getStructureWorld(structureTag);

        //only locate structure if the player is in the same world as the one for the compass
        //otherwise just point it to 0,0 in said dimension.
        BlockPos structurePos = new BlockPos(0,0,0);
        if(player.getCommandSenderWorld().dimension().equals(world)) {
            structurePos = APRandomizer.getServer().getLevel(world).findNearestMapStructure(structureTag, player.blockPosition(), 75, false);
        }

        String displayName = Utils.getAPStructureName(structureTag);

        if(structurePos == null)
            structurePos = new BlockPos(0,0,0);

       
        compass.applyComponents(DataComponentPatch.builder()
        		.set(DataComponents.CUSTOM_DATA,
        		CustomData.of(CompoundTag.builder()
        		.put("structure", structureTag.location().toString())
        		.build()))
        		.build());
        //update the nbt data with our new structure.
        Utils.addLodestoneTags(world,structurePos,compass);
        compass.applyComponents(DataComponentPatch.builder().set(DataComponents.CUSTOM_NAME, Component.literal(String.format("Structure Compass (%s)", displayName))).build());
    }
}
