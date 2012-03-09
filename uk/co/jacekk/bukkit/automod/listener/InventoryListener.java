package uk.co.jacekk.bukkit.automod.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.inventory.InventoryCloseEvent;
import org.getspout.spoutapi.event.inventory.InventoryOpenEvent;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;
import de.diddiz.LogBlock.QueryParams.Order;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class InventoryListener implements Listener {
	
	private AutoMod plugin;
	
	private HashMap<Player, ArrayList<ItemStack>> inventories;
	
	public InventoryListener(AutoMod plugin){
		this.plugin = plugin;
		
		this.inventories = new HashMap<Player, ArrayList<ItemStack>>();
	}
	
	private ArrayList<ItemStack> combineItemStacks(ItemStack[] items){
		ArrayList<ItemStack> combined = new ArrayList<ItemStack>();
		
		int type;
		byte data;
		boolean found;
		
		for (ItemStack item : items){
			if (item != null){
				type = item.getTypeId();
				data = item.getData().getData();
				found = false;
				
				for (ItemStack smallItem : combined){
					if (smallItem.getTypeId() == type && smallItem.getData().getData() == data){
						smallItem.setAmount(smallItem.getAmount() + item.getAmount());
						found = true;
						break;
					}
				}
				
				if (found == false){
					combined.add(new ItemStack(type, item.getAmount(), (short) 0, data));
				}
			}
		}
		
		Collections.sort(combined, new Comparator<ItemStack>(){
			
			public int compare(ItemStack a, ItemStack b){
				int aType = a.getTypeId();
				int bType = b.getTypeId();
				
				if (aType < bType){
					return -1;
				}
				
				if (aType > bType){
					return 1;
				}
				
				byte aData = a.getData().getData();
				byte bData = b.getData().getData();
				
				if (aData < bData){
					return -1;
				}
				
				if (aData > bData){
					return 1;
				}
				
				return 0;
			}
			
		});
		
		return combined;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryOpen(InventoryOpenEvent event){
		if (event.isCancelled()) return;
		
		Player player = event.getPlayer();
		
		if (plugin.playersPassedChecks.contains(player) == false && plugin.buildDeniedList.contains(player) == false){
			if (player.hasPermission("automod.watch.all") || player.hasPermission("automod.watch.chests")){
				Location location = event.getLocation();
				
				if (location != null && location.getBlock().getTypeId() == Material.CHEST.getId()){
					try{
						QueryParams params = new QueryParams(plugin.logblock);
						
						params.loc = location;
						params.world = params.loc.getWorld();
						params.types = Arrays.asList(Material.CHEST.getId());
						params.bct = BlockChangeType.CREATED;
						params.order = Order.DESC;
						params.limit = 1;
						
						params.needType = true;
						params.needPlayer = true;
						
						List<BlockChange> changes = plugin.logblock.getBlockChanges(params);
						
						if (changes.size() > 0){
							BlockChange change = changes.get(0);
							
							if (change.playerName == player.getName()){
								return;
							}
						}
					}catch (Exception e){
						plugin.log.warn("LogBlock lookup failed.");
						e.printStackTrace();
					}
					
					this.inventories.put(event.getPlayer(), this.combineItemStacks(event.getInventory().getContents()));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClose(InventoryCloseEvent event){
		if (event.isCancelled()) return;
		
		Player player = event.getPlayer();
		
		if (this.inventories.containsKey(player)){
			ArrayList<ItemStack> before = this.inventories.get(player);
			ArrayList<ItemStack> after = this.combineItemStacks(event.getInventory().getContents());
			
			if (before.size() > after.size()){
				plugin.removeBuildFor(player, "Stealing from a chest");
				return;
			}
			
			int type;
			byte data;
			
			for (ItemStack item : before){
				type = item.getTypeId();
				data = item.getData().getData();
				
				for (ItemStack compare : after){
					if (type == compare.getTypeId() && data == compare.getData().getData() && compare.getAmount() < item.getAmount()){
						plugin.removeBuildFor(player, "Stealing from a chest");
						return;
					}
				}
			}
			
			this.inventories.remove(player);
		}
	}
	
}
