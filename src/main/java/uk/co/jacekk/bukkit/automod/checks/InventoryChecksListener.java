package uk.co.jacekk.bukkit.automod.checks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.automod.data.BlockLocation;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.v7.event.BaseListener;

public class InventoryChecksListener extends BaseListener<AutoMod> {
	
	private HashMap<String, HashMap<Material, Integer>> inventories;
	
	public InventoryChecksListener(AutoMod plugin){
		super(plugin);
		
		this.inventories = new HashMap<String, HashMap<Material, Integer>>();
	}
	
	private HashMap<Material, Integer> combineItemStacks(ItemStack[] items){
		HashMap<Material, Integer> combined = new HashMap<Material, Integer>();
		
		Material type;
		int amount;
		
		for (ItemStack item : items){
			if (item != null){
				type = item.getType();
				amount = item.getAmount();
				
				if (type != Material.AIR){
					combined.put(type, (combined.containsKey(type)) ? combined.get(type) + amount : amount);
				}
			}
		}
		
		return combined;
	}
	
	private HashMap<Material, Integer> getInventoryDiff(HashMap<Material, Integer> before, HashMap<Material, Integer> after){
		HashMap<Material, Integer> items = new HashMap<Material, Integer>();
		
		for (Entry<Material, Integer> item : before.entrySet()){
			Material type = item.getKey();
			int amount = item.getValue();
			
			int change = (after.containsKey(type)) ? after.get(type) - amount : -amount;
			
			items.put(type, change);
		}
		
		return items;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event){
		HumanEntity human = event.getPlayer();
		
		if (human instanceof Player == false){
			return;
		}
		
		Player player = (Player) human;
		String playerName = player.getName();
		
		if (plugin.config.getStringList(Config.IGNORE_WORLDS).contains(player.getWorld().getName())){
			return;
		}
		
		if (!plugin.playerDataManager.gotDataFor(playerName)){
			return;
		}
		
		if (!Permission.WATCH_ALL.has(player) && !Permission.WATCH_CHESTS.has(player)){
			return;
		}
		
		if (plugin.trustedPlayers.contains(playerName) || plugin.blockedPlayers.contains(playerName)){
			return;
		}
		
		PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
		
		InventoryView inventory = event.getView();
		InventoryType type = inventory.getType();
		
		if (!Arrays.asList(InventoryType.CHEST, InventoryType.FURNACE, InventoryType.DISPENSER).contains(type)){
			return;
		}
		
		if (playerData.placedBlocks.contains(new BlockLocation(player.getTargetBlock(null, 10).getLocation()))){
			return;
		}
		
		this.inventories.put(playerName, this.combineItemStacks(inventory.getTopInventory().getContents()));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent event){
		HumanEntity human = event.getPlayer();
		
		if (human instanceof Player == false){
			return;
		}
		
		Player player = (Player) human;
		String playerName = player.getName();
		
		if (this.inventories.containsKey(playerName)){
			HashMap<Material, Integer> before = this.inventories.get(playerName);
			HashMap<Material, Integer> after = this.combineItemStacks(event.getView().getTopInventory().getContents());
			
			HashMap<Material, Integer> diff = this.getInventoryDiff(before, after);
			
			this.inventories.remove(playerName);
			
			for (Entry<Material, Integer> item : diff.entrySet()){
				if (item.getValue() < 0){
					plugin.removeBuildFor(player, Check.INVENTORY_THEFT);
					plugin.playerDataManager.getPlayerData(playerName).setInventoryTheftItems(diff);
					
					return;
				}
			}
		}
	}
	
}
