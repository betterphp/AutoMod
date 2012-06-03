package uk.co.jacekk.bukkit.automod.data;

import org.bukkit.Location;

public class BlockLocation {
	
	private int x, y, z;
	
	public BlockLocation(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockLocation(Location loc){
		this(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof BlockLocation)){
			return false;
		}
		
		BlockLocation loc = (BlockLocation) obj;
		
		return (this.x == loc.x && this.y == loc.y && this.z == loc.z);
	}
	
}
