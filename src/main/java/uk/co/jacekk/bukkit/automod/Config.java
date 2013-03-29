package uk.co.jacekk.bukkit.automod;

import java.util.Arrays;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfigKey;

public class Config {
	
	public static final PluginConfigKey IGNORE_WORLDS			= new PluginConfigKey("ignore-worlds", Arrays.asList("world_nether", "world_the_end"));
	public static final PluginConfigKey BUILD_REMOVED_COMMANDS	= new PluginConfigKey("build-removed-commands", Arrays.asList());
	public static final PluginConfigKey TRUSTED_LIMIT			= new PluginConfigKey("trusted-limit", 40);
	
	public static final PluginConfigKey CHECK_BLOCK_BREAK_OWNED_ENABLED			= new PluginConfigKey("check.block-break.owned.enabled",		true);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_OWNED_LIMIT			= new PluginConfigKey("check.block-break.owned.limit",			5);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_UNNATURAL_ENABLED		= new PluginConfigKey("check.block-break.unnatural.enabled",	true);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_UNNATURAL_LIMIT		= new PluginConfigKey("check.block-break.unnatural.limit",		10);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_UNBREAKABLE_ENABLED	= new PluginConfigKey("check.block-break.unbreakable.enabled",	true);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_UNBREAKABLE_LIMIT		= new PluginConfigKey("check.block-break.unbreakable.limit",	2);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_DIRECTION_ENABLED		= new PluginConfigKey("check.block-break.direction.enabled",	true);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_DIRECTION_LIMIT		= new PluginConfigKey("check.block-break.direction.limit",		200);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_REACH_ENABLED			= new PluginConfigKey("check.block-break.reach.enabled",		true);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_REACH_LIMIT			= new PluginConfigKey("check.block-break.reach.limit",			200);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_NO_SWING_ENABLED		= new PluginConfigKey("check.block-break.no-swing.enabled",		true);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_NO_SWING_LIMIT		= new PluginConfigKey("check.block-break.no-swing.limit",		200);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_FAST_BREAK_ENABLED	= new PluginConfigKey("check.block-break.fast-break.enabled",	true);
	public static final PluginConfigKey CHECK_BLOCK_BREAK_FAST_BREAK_LIMIT		= new PluginConfigKey("check.block-break.fast-break.limit",		200);
	
	public static final PluginConfigKey CHECK_BLOCK_PLACE_DIRECTION_ENABLED		= new PluginConfigKey("check.block-place.direction.enabled",	true);
	public static final PluginConfigKey CHECK_BLOCK_PLACE_DIRECTION_LIMIT		= new PluginConfigKey("check.block-place.direction.limit",		200);
	public static final PluginConfigKey CHECK_BLOCK_PLACE_REACH_ENABLED			= new PluginConfigKey("check.block-place.reach.enabled",		true);
	public static final PluginConfigKey CHECK_BLOCK_PLACE_REACH_LIMIT			= new PluginConfigKey("check.block-place.reach.limit",			200);
	public static final PluginConfigKey CHECK_BLOCK_PLACE_NO_SWING_ENABLED		= new PluginConfigKey("check.block-place.no-swing.enabled",		true);
	public static final PluginConfigKey CHECK_BLOCK_PLACE_NO_SWING_LIMIT		= new PluginConfigKey("check.block-place.no-swing.limit",		200);
	
	public static final PluginConfigKey CHECK_INVENTORY_THEFT_ENABLED			= new PluginConfigKey("check.inventory.theft.enabled",			true);
	public static final PluginConfigKey CHECK_INVENTORY_INSTANT_EAT_ENABLED		= new PluginConfigKey("check.inventory.instant-eat.enabled",	true);
	public static final PluginConfigKey CHECK_INVENTORY_INSTANT_EAT_LIMIT		= new PluginConfigKey("check.inventory.instant-eat.limit",		200);
	
	public static final PluginConfigKey CHECK_PVP_DIRECTION_ENABLED				= new PluginConfigKey("check.pvp.direction.enabled",			true);
	public static final PluginConfigKey CHECK_PVP_DIRECTION_LIMIT				= new PluginConfigKey("check.pvp.direction.limit",				200);
	public static final PluginConfigKey CHECK_PVP_CRITICAL_ENABLED				= new PluginConfigKey("check.pvp.critical.enabled",				true);
	public static final PluginConfigKey CHECK_PVP_CRITICAL_LIMIT				= new PluginConfigKey("check.pvp.critical.limit",				200);
	public static final PluginConfigKey CHECK_PVP_KNOCKBACK_ENABLED				= new PluginConfigKey("check.pvp.knockback.enabled",			true);
	public static final PluginConfigKey CHECK_PVP_KNOCKBACK_LIMIT				= new PluginConfigKey("check.pvp.knockback.limit",				200);
	public static final PluginConfigKey CHECK_PVP_SPEED_ENABLED					= new PluginConfigKey("check.pvp.speed.enabled",				true);
	public static final PluginConfigKey CHECK_PVP_SPEED_LIMIT					= new PluginConfigKey("check.pvp.speed.limit",					200);
	public static final PluginConfigKey CHECK_PVP_GODMODE_ENABLED				= new PluginConfigKey("check.pvp.godmode.enabled",				true);
	public static final PluginConfigKey CHECK_PVP_GODMODE_LIMIT					= new PluginConfigKey("check.pvp.godmode.limit",				200);
	public static final PluginConfigKey CHECK_PVP_NO_SWING_ENABLED				= new PluginConfigKey("check.pvp.no-swing.enabled",				true);
	public static final PluginConfigKey CHECK_PVP_NO_SWING_LIMIT				= new PluginConfigKey("check.pvp.no-swing.limit",				200);
	public static final PluginConfigKey CHECK_PVP_FAST_HEAL_ENABLED				= new PluginConfigKey("check.pvp.fast-heal.enabled",			true);
	public static final PluginConfigKey CHECK_PVP_FAST_HEAL_LIMIT				= new PluginConfigKey("check.pvp.fast-heal.limit",				200);
	public static final PluginConfigKey CHECK_PVP_INSTANT_BOW_ENABLED			= new PluginConfigKey("check.pvp.instant-bow.enabled",			true);
	public static final PluginConfigKey CHECK_PVP_INSTANT_BOW_LIMIT				= new PluginConfigKey("check.pvp.instant-bow.limit",			200);
	
}