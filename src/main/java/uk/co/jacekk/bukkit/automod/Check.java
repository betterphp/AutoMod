package uk.co.jacekk.bukkit.automod;

import java.util.HashMap;

public enum Check {
	
	BLOCK_BREAK_OWNED_BLOCKS(0,		"Breaking blocks placed by another player"),
	BLOCK_BREAK_UNNATURAL_BLOCKS(1,	"Breaking blocks that are not found naturally"),
	BLOCK_BREAK_UNBREAKABLE(2,		"Breaking an unbreakable block"),
	BLOCK_BREAK_DIRECTION(3,		"Breaking a block out of sight"),
	BLOCK_BREAK_REACH(4,			"Breaking a block out of the normal reach"),
	BLOCK_BREAK_NO_SWING(5,			"Breaking a block without arm swing"),
	BLOCK_BREAK_FAST_BREAK(10,		"Breaking a block faster than should be possible"),
	
	BLOCK_PLACE_DIRECTION(6,		"Placing a block out of sight"),
	BLOCK_PLACE_REACH(7,			"Placing a block out of the normal reach"),
	BLOCK_PLACE_NO_SWING(9,			"Placing a block without arm swing"),
	
	INVENTORY_THEFT(8,				"Taking items from a container"),
	INVENTORY_INSTANT_EAT(19,		"Eating food too quickly"),
	
	PVP_DIRECTION(11,				"Attacking a player out of the line of sight"),
	PVP_CRITICAL(12,				"Performing critical hits when not possible"),
	PVP_KNOCKBACK(13,				"Knocking back an enemy when not possible"),
	PVP_SPEED(14,					"Swinging a sword faster than possible"),
	PVP_GODMODE(15,					"Attempting a godmode exploit"),
	PVP_NO_SWING(16,				"Attacking a player without arm swing"),
	PVP_INSTANT_HEAL(17,			"Healing too quickly"),
	PVP_INSTANT_BOW(18,				"Shooting a bow too quickly"),
	
	CUSTOM_ADDITION(100,			"Added manually");
	
	private int id;
	private String description;
	
	private static HashMap<Integer, Check> idLookupTable;
	
	Check(int id, String description){
		this.id = id;
		this.description = description;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean equals(Check check){
		return (check.id == this.id);
	}
	
	static{
		idLookupTable = new HashMap<Integer, Check>();
		
		for (Check check : values()){
			idLookupTable.put(check.getId(), check);
		}
	}
	
	public static Check fromId(int id){
		if (!idLookupTable.containsKey(id)){
			return Check.CUSTOM_ADDITION;
		}
		
		return idLookupTable.get(id);
	}
	
}
