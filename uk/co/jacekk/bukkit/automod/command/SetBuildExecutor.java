package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class SetBuildExecutor implements CommandExecutor {
	
	private AutoMod plugin;
	
	public SetBuildExecutor(AutoMod plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender.hasPermission("automod.admin.setbuild") == false){
			plugin.messagePlayer(sender, ChatColor.RED + "You do not have permissions to use this command.");
			return true;
		}
		
		if (args.length != 2){
			plugin.messagePlayer(sender, ChatColor.AQUA + "Usage: /setbuild [player_name] [yes / no]");
			plugin.messagePlayer(sender, ChatColor.AQUA + "Example: /setbuild wide_load yes");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("yes") == false && args[1].equalsIgnoreCase("no") == false){
			plugin.messagePlayer(sender, ChatColor.RED + "Argument #2 must be either yes or no.");
			return true;
		}
		
		if (plugin.getServer().getPlayer(args[0]) == null && plugin.getServer().getOfflinePlayer(args[0]) == null){
			plugin.messagePlayer(sender, ChatColor.RED + "That player has never connected to the server.");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("yes")){
			if (plugin.blockedPlayers.contains(args[0])){
				plugin.blockedPlayers.remove(args[0]);
				
				if (plugin.playerDataManager.gotDataFor(args[0])){
					plugin.playerDataManager.getPlayerData(args[0]).resetAll();
				}
			}
		}else{
			if (plugin.blockedPlayers.contains(args[0]) == false){
				plugin.blockedPlayers.add(args[0]);
				plugin.trustedPlayers.remove(args[0]);
			}
		}
		
		plugin.messagePlayer(sender, ChatColor.GREEN + "Permissions set for " + args[0]);
		
		return true;
	}
	
}
