package uk.co.jacekk.bukkit.automod;

public enum Check {
	
	BLOCK_BREAK_OWNED_BLOCKS(0,		"Breaking blocks placed by another player"),
	BLOCK_BREAK_UNNATURAL_BLOCKS(1,	"Breaking blocks that are not found naturally"),
	BLOCK_BREAK_UNBREAKABLE(2,		"Breaking an unbreakable block"),
	BLOCK_BREAK_DIRECTION(3,		"Breaking a block out of sight"),
	BLOCK_BREAK_REACH(4,			"Breaking a block out of the normal reach"),
	BLOCK_BREAK_NO_SWING(5,			"Breaking a block without arm swing"),
	
	BLOCK_PLACE_DIRECTION(6,		"Placing a block out of sight"),
	BLOCK_PLACE_REACH(7,			"Placing a block out of the normal reach"),
	
	INVENTORY_THEFT(8,				"Taking items from a container");
	
	private int id;
	private String description;
	
	Check(int id, String description){
		this.id = id;
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean equals(Check check){
		return (check.id == this.id);
	}
	
}
