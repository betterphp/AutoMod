package uk.co.jacekk.bukkit.automod.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;

public class PlayerData {
	
	// The number of blocks a player has broken.
	public int naturalBlocksBroken;
	public int unnaturalBlocksBroken;
	public int ownedBlocksBroken;
	public int unbreakableBlocksBroken;
	
	// Specific block types that the player has broken.
	public HashMap<Material, Integer> unnaturalTypesBroken;
	public HashMap<Material, Integer> ownedTypesBroken;
	public HashMap<Material, Integer> unbreakableTypesBroken;
	
	// The items a player removed from a container.
	public HashMap<Material, Integer> inventoryTheftTypes;
	
	// The NoCheat violation level for the last block break offence
	public double blockBreakVL;
	
	// The NoCheat violation level for the last block place offence
	public double blockPlaceVL;
	
	// Some useful totals, avoid doing lots of adding.
	public int totalBlocksBroken;
	public int totalBlocksPlaced;
	public int totalBlockEvents;
	
	// The time the player last joined or left.
	public long lastJoinTime;
	public long lastQuitTime;
	
	// A list of coordinates where the player has placed blocks
	public ArrayList<BlockLocation> placedBlocks;
	
	public PlayerData(){
		this.resetAll();
	}
	
	public void resetAll(){
		this.naturalBlocksBroken = 0;
		this.unnaturalBlocksBroken = 0;
		this.ownedBlocksBroken = 0;
		this.unnaturalBlocksBroken = 0;
		
		this.unnaturalTypesBroken = new HashMap<Material, Integer>();
		this.ownedTypesBroken = new HashMap<Material, Integer>();
		this.unbreakableTypesBroken = new HashMap<Material, Integer>();
		
		this.inventoryTheftTypes = new HashMap<Material, Integer>();
		
		this.blockBreakVL = 0.0d;
		this.blockPlaceVL = 0.0d;
		
		this.totalBlocksBroken = 0;
		this.totalBlocksPlaced = 0;
		this.totalBlockEvents = 0;
		
		this.lastJoinTime = 0L;
		this.lastQuitTime = 0L;
		
		this.placedBlocks = new ArrayList<BlockLocation>();
	}
	
	public void addNaturalBlockBreak(Material type){
		++this.naturalBlocksBroken;
	}
	
	public void addUnnaturalBlockBreak(Material type){
		++this.unnaturalBlocksBroken;
		this.unnaturalTypesBroken.put(type, this.unnaturalTypesBroken.containsKey(type) ? this.unnaturalTypesBroken.get(type) + 1 : 1);
	}
	
	public void addOwnedBlockBreak(Material type){
		++this.ownedBlocksBroken;
		this.ownedTypesBroken.put(type, this.ownedTypesBroken.containsKey(type) ? this.ownedTypesBroken.get(type) + 1 : 1);
	}
	
	public void addUnbreakableBlockBreak(Material type){
		++this.unbreakableBlocksBroken;
		this.unbreakableTypesBroken.put(type, this.unbreakableTypesBroken.containsKey(type) ? this.unbreakableTypesBroken.get(type) + 1 : 1);
	}
	
	public void setInventoryTheftItems(HashMap<Material, Integer> diff){
		this.inventoryTheftTypes.clear();
		
		for (Entry<Material, Integer> item : diff.entrySet()){
			Material type = item.getKey();
			int change = item.getValue();
			
			if (change < 0){
				this.inventoryTheftTypes.put(type, Math.abs(change));
			}
		}
	}
	
}
