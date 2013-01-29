package uk.co.jacekk.bukkit.automod;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.v8.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission WATCH_ALL			= new PluginPermission("automod.watch.all",				PermissionDefault.FALSE,	"All things that griefers love to do will be watched for players with this permission");
	public static final PluginPermission WATCH_BUILD		= new PluginPermission("automod.watch.build",			PermissionDefault.FALSE,	"The blocks that the player places are monitored");
	public static final PluginPermission WATCH_LOGBLOCK		= new PluginPermission("automod.watch.logblock",		PermissionDefault.FALSE,	"The ownership of broken blocks is monitored with data taken from LogBlock");
	public static final PluginPermission WATCH_NOCHEAT		= new PluginPermission("automod.watch.nocheat",			PermissionDefault.FALSE,	"Data from NoCheat is looked to see if the player is hacking");
	public static final PluginPermission WATCH_CHESTS		= new PluginPermission("automod.watch.chests",			PermissionDefault.FALSE,	"The chest transactions that the player makes are watched, if they steal their permissions are removed");
	
	public static final PluginPermission ADMIN_DATA			= new PluginPermission("automod.admin.data",			PermissionDefault.OP,		"Allows the player access to the player data");
	public static final PluginPermission ADMIN_VOTE			= new PluginPermission("automod.admin.vote",			PermissionDefault.OP,		"Allows the player to vote on another players build request");
	public static final PluginPermission ADMIN_LIST_ALL		= new PluginPermission("automod.admin.lists.all",		PermissionDefault.OP,		"Allows the player to modify all lists");
	public static final PluginPermission ADMIN_LIST_BLOCKED	= new PluginPermission("automod.admin.lists.blocked",	PermissionDefault.FALSE,	"Allows the player to modify the blocked player list");
	public static final PluginPermission ADMIN_LIST_TRUSTED	= new PluginPermission("automod.admin.lists.trusted",	PermissionDefault.FALSE,	"Allows the player to modify the trusted player list");
	
}
