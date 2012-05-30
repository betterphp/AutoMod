package uk.co.jacekk.bukkit.automod;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public enum Permission {
	
	WATCH_ALL(			"automod.watch.all",			PermissionDefault.FALSE,	"All things that griefers love to do will be watched for players with this permission"),
	WATCH_BUILD(		"automod.watch.build",			PermissionDefault.FALSE,	"The blocks that the player places are monitored"),
	WATCH_LOGBLOCK(		"automod.watch.logblock",		PermissionDefault.FALSE,	"The ownership of broken blocks is monitored with data taken from LogBlock"),
	WATCH_NOCHEAT(		"automod.watch.nocheat",		PermissionDefault.FALSE,	"Data from NoCheat is looked to see if the player is hacking"),
	WATCH_CHESTS(		"automod.watch.chests",			PermissionDefault.FALSE,	"The chest transactions that the player makes are watched, if they steal their permissions are removed"),
	
	ADMIN_SETBUILD(		"automod.admin.setbuild",		PermissionDefault.OP,		"Allows the player to set the build permissions for other players"),
	ADMIN_LIST_ALL(		"automod.admin.lists.all",		PermissionDefault.OP,		"Allows the player to modify all lists"),
	ADMIN_LIST_BLOCKED(	"automod.admin.lists.blocked",	PermissionDefault.FALSE,	"Allows the player to modift the build denied list"),
	ADMIN_LIST_TRUSTED(	"automod.admin.lists.trusted",	PermissionDefault.FALSE,	"Allows the player to modify the trusted player list");
	
	protected String node;
	protected PermissionDefault defaultValue;
	protected String description;
	
	private Permission(String node, PermissionDefault defaultValue, String description){
		this.node = node;
		this.defaultValue = defaultValue;
		this.description = description;
	}
	
	public List<Player> getPlayersWith(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		for (Player player : Bukkit.getServer().getOnlinePlayers()){
			if (this.hasPermission(player)){
				players.add(player);
			}
		}
		
		return players;
	}
	
	public Boolean hasPermission(CommandSender sender){
		return sender.hasPermission(this.node);
	}
	
	public String getNode(){
		return this.node;
	}
	
	public PermissionDefault getDefault(){
		return this.defaultValue;
	}
	
	public String getDescription(){
		return this.description;
	}
	
}