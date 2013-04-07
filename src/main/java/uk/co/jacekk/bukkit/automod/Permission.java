package uk.co.jacekk.bukkit.automod;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission CHECK_EXEMPT		= new PluginPermission("automod.check.exempt",			PermissionDefault.OP,		"Prevents the player from being checked");
	
	public static final PluginPermission ADMIN_DATA			= new PluginPermission("automod.admin.data",			PermissionDefault.OP,		"Allows the player access to the player data");
	public static final PluginPermission ADMIN_VOTE			= new PluginPermission("automod.admin.vote",			PermissionDefault.OP,		"Allows the player to vote on another players build request");
	public static final PluginPermission ADMIN_LIST_ALL		= new PluginPermission("automod.admin.lists.all",		PermissionDefault.OP,		"Allows the player to modify all lists");
	public static final PluginPermission ADMIN_LIST_BLOCKED	= new PluginPermission("automod.admin.lists.blocked",	PermissionDefault.FALSE,	"Allows the player to modify the blocked player list");
	public static final PluginPermission ADMIN_LIST_TRUSTED	= new PluginPermission("automod.admin.lists.trusted",	PermissionDefault.FALSE,	"Allows the player to modify the trusted player list");
	
}
