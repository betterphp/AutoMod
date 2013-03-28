package uk.co.jacekk.bukkit.automod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import uk.co.jacekk.bukkit.automod.check.block.BreakDirectionListener;
import uk.co.jacekk.bukkit.automod.check.block.BreakFastBreakListener;
import uk.co.jacekk.bukkit.automod.check.block.BreakNoSwingListener;
import uk.co.jacekk.bukkit.automod.check.block.BreakOwnedListener;
import uk.co.jacekk.bukkit.automod.check.block.BreakReachListener;
import uk.co.jacekk.bukkit.automod.check.block.BreakUnbreakableListener;
import uk.co.jacekk.bukkit.automod.check.block.BreakUnnaturalListener;
import uk.co.jacekk.bukkit.automod.check.block.PlaceDirectionListener;
import uk.co.jacekk.bukkit.automod.check.block.PlaceNoSwingListener;
import uk.co.jacekk.bukkit.automod.check.block.PlaceReachListener;
import uk.co.jacekk.bukkit.automod.check.player.InventoryInstantEatListener;
import uk.co.jacekk.bukkit.automod.check.player.InventoryTheftListener;
import uk.co.jacekk.bukkit.automod.check.player.PVPCriticalListener;
import uk.co.jacekk.bukkit.automod.check.player.PVPDirectionListener;
import uk.co.jacekk.bukkit.automod.check.player.PVPGodModeListener;
import uk.co.jacekk.bukkit.automod.check.player.PVPInstantBowListener;
import uk.co.jacekk.bukkit.automod.check.player.PVPFastHealListener;
import uk.co.jacekk.bukkit.automod.check.player.PVPKnockbackListener;
import uk.co.jacekk.bukkit.automod.check.player.PVPNoSwingListener;
import uk.co.jacekk.bukkit.automod.check.player.PVPSpeedListener;
import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfigKey;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public enum Check {
	
	BLOCK_BREAK_OWNED(0,			"Breaking blocks placed by another player",			Config.CHECK_BLOCK_BREAK_OWNED_ENABLED,			BreakOwnedListener.class,			Arrays.asList("LogBlock")),
	BLOCK_BREAK_UNNATURAL(1,		"Breaking blocks that are not found naturally",		Config.CHECK_BLOCK_BREAK_UNNATURAL_ENABLED,		BreakUnnaturalListener.class,		new ArrayList<String>()),
	BLOCK_BREAK_UNBREAKABLE(2,		"Breaking an unbreakable block",					Config.CHECK_BLOCK_BREAK_UNBREAKABLE_ENABLED,	BreakUnbreakableListener.class,		new ArrayList<String>()),
	BLOCK_BREAK_DIRECTION(3,		"Breaking a block out of sight",					Config.CHECK_BLOCK_BREAK_DIRECTION_ENABLED,		BreakDirectionListener.class,		Arrays.asList("NoCheatPlus")),
	BLOCK_BREAK_REACH(4,			"Breaking a block out of the normal reach",			Config.CHECK_BLOCK_BREAK_REACH_ENABLED,			BreakReachListener.class,			Arrays.asList("NoCheatPlus")),
	BLOCK_BREAK_NO_SWING(5,			"Breaking a block without arm swing",				Config.CHECK_BLOCK_BREAK_NO_SWING_ENABLED,		BreakNoSwingListener.class,			Arrays.asList("NoCheatPlus")),
	BLOCK_BREAK_FAST_BREAK(10,		"Breaking a block faster than should be possible",	Config.CHECK_BLOCK_BREAK_FAST_BREAK_ENABLED,	BreakFastBreakListener.class,		Arrays.asList("NoCheatPlus")),
	
	BLOCK_PLACE_DIRECTION(6,		"Placing a block out of sight",						Config.CHECK_BLOCK_PLACE_DIRECTION_ENABLED,		PlaceDirectionListener.class,		Arrays.asList("NoCheatPlus")),
	BLOCK_PLACE_REACH(7,			"Placing a block out of the normal reach",			Config.CHECK_BLOCK_PLACE_REACH_ENABLED,			PlaceReachListener.class,			Arrays.asList("NoCheatPlus")),
	BLOCK_PLACE_NO_SWING(9,			"Placing a block without arm swing",				Config.CHECK_BLOCK_PLACE_NO_SWING_ENABLED,		PlaceNoSwingListener.class,			Arrays.asList("NoCheatPlus")),
	
	INVENTORY_THEFT(8,				"Taking items from a container",					Config.CHECK_INVENTORY_THEFT_ENABLED,			InventoryTheftListener.class,		new ArrayList<String>()),
	INVENTORY_INSTANT_EAT(19,		"Eating food too quickly",							Config.CHECK_INVENTORY_INSTANT_EAT_ENABLED,		InventoryInstantEatListener.class,	Arrays.asList("NoCheatPlus")),
	
	PVP_DIRECTION(11,				"Attacking a player out of the line of sight",		Config.CHECK_PVP_DIRECTION_ENABLED,				PVPDirectionListener.class,			Arrays.asList("NoCheatPlus")),
	PVP_CRITICAL(12,				"Performing critical hits when not possible",		Config.CHECK_PVP_CRITICAL_ENABLED,				PVPCriticalListener.class,			Arrays.asList("NoCheatPlus")),
	PVP_KNOCKBACK(13,				"Knocking back an enemy when not possible",			Config.CHECK_PVP_KNOCKBACK_ENABLED,				PVPKnockbackListener.class,			Arrays.asList("NoCheatPlus")),
	PVP_SPEED(14,					"Swinging a sword faster than possible",			Config.CHECK_PVP_SPEED_ENABLED,					PVPSpeedListener.class,				Arrays.asList("NoCheatPlus")),
	PVP_GODMODE(15,					"Attempting a godmode exploit",						Config.CHECK_PVP_GODMODE_ENABLED,				PVPGodModeListener.class,			Arrays.asList("NoCheatPlus")),
	PVP_NO_SWING(16,				"Attacking a player without arm swing",				Config.CHECK_PVP_NO_SWING_ENABLED,				PVPNoSwingListener.class,			Arrays.asList("NoCheatPlus")),
	PVP_FAST_HEAL(17,				"Healing too quickly",								Config.CHECK_PVP_FAST_HEAL_ENABLED,				PVPFastHealListener.class,			Arrays.asList("NoCheatPlus")),
	PVP_INSTANT_BOW(18,				"Shooting a bow too quickly",						Config.CHECK_PVP_INSTANT_BOW_ENABLED,			PVPInstantBowListener.class,		Arrays.asList("NoCheatPlus")),
	
	CUSTOM_ADDITION(100,			"Added manually",									null,											null,								null);
	
	private int id;
	private String description;
	private PluginConfigKey enabledKey;
	private Class<? extends BaseListener<AutoMod>> listenerClass;
	private List<String> requiredPlugins;
	
	private static HashMap<Integer, Check> idLookupTable;
	
	private Check(int id, String description, PluginConfigKey enabledKey, Class<? extends BaseListener<AutoMod>> listenerClass, List<String> requiredPlugins){
		this.id = id;
		this.description = description;
		this.enabledKey = enabledKey;
		this.listenerClass = listenerClass;
		this.requiredPlugins = requiredPlugins;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean isVirtual(){
		return (this.enabledKey == null || this.listenerClass == null);
	}
	
	public PluginConfigKey getEnabledConfigKey(){
		return this.enabledKey;
	}
	
	public Class<? extends BaseListener<AutoMod>> getListenerClass(){
		return this.listenerClass;
	}
	
	public List<String> getRequiredPlugins(){
		return this.requiredPlugins;
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
