package uk.co.jacekk.bukkit.automod.data;

public class PlayerData {
	
	// The number of blocks a player has broken.
	public int naturalBlocksBroken;
	public int unnaturalBlocksBroken;
	public int ownedBlocksBroken;
	public int unbreakableBlocksBroken;
	
	// Some useful totals, avoid doing lots of adding.
	public int totalBlocksBroken;
	public int totalBlocksPlaced;
	public int totalBlockEvents;
	
	// The time the player last joined or left.
	public long lastJoinTime;
	public long lastQuitTime;
	
	public PlayerData(){
		this.naturalBlocksBroken = 0;
		this.unnaturalBlocksBroken = 0;
		this.ownedBlocksBroken = 0;
		this.unnaturalBlocksBroken = 0;
		
		this.totalBlocksBroken = 0;
		this.totalBlocksPlaced = 0;
		this.totalBlockEvents = 0;
		
		this.lastJoinTime = 0L;
		this.lastQuitTime = 0L;
	}
	
}
