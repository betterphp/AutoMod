package uk.co.jacekk.bukkit.automod.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;
import de.diddiz.LogBlock.QueryParams.Order;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class InventoryViolationListener implements Listener {
	
	private AutoMod plugin;
	
	private HashMap<Player, ArrayList<ItemStack>> inventories;
	
	public InventoryViolationListener(AutoMod plugin){
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event){
		HumanEntity human = event.getPlayer();
		
		if (human instanceof Player == false){
			return;
		}
		
		Player player = (Player) human;
		
		if (plugin.playersPassedChecks.contains(player) || plugin.buildDeniedList.contains(player)){
			return;
		}
		
		if (player.hasPermission("automod.watch.all") == false && player.hasPermission("automod.watch.chests") == false){
			return;
		}
		
		InventoryView inventory = event.getView();
		InventoryType type = inventory.getType();
		
		if (Arrays.asList(InventoryType.CHEST, InventoryType.FURNACE, InventoryType.DISPENSER).contains(type)){
			try{
				QueryParams params = new QueryParams(plugin.logblock);
				
				params.loc = player.getTargetBlock(null, 10).getLocation();
				params.world = params.loc.getWorld();
				params.types = Arrays.asList(Material.CHEST.getId(), Material.FURNACE.getId(), Material.DISPENSER.getId());
				params.bct = BlockChangeType.CREATED;
				params.order = Order.DESC;
				params.limit = 1;
				
				params.needType = true;
				params.needPlayer = true;
				
				List<BlockChange> changes = plugin.logblock.getBlockChanges(params);
				
				if (changes.size() > 0){
					BlockChange change = changes.get(0);
					
					if (change.playerName.equalsIgnoreCase(player.getName())){
						System.out.println("yup");
						return;
					}
				}
			}catch (Exception e){
				plugin.log.warn("LogBlock lookup failed.");
				e.printStackTrace();
			}
			
			this.inventories.put(player, this.combineItemStacks(inventory.getTopInventory().getContents()));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent event){
		HumanEntity human = event.getPlayer();
		
		if (human instanceof Player == false){
			return;
		}
		
		Player player = (Player) human;
		
		if (this.inventories.containsKey(player)){
			ArrayList<ItemStack> before = this.inventories.get(player);
			ArrayList<ItemStack> after = this.combineItemStacks(event.getView().getTopInventory().getContents());
			
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
