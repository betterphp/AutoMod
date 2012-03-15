package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		
		if (plugin.voteTracker.voteExistsFor(args[0])){
			for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()){
				if (onlinePlayer.hasPermission("automod.vote.build")){
					plugin.messagePlayer(onlinePlayer, ChatColor.AQUA + sender.getName() + " just ended the vote for " + args[0]);
				}
			}
			
			plugin.voteTracker.removePlayer(args[0]);
		}
		
		if (args[1].equalsIgnoreCase("yes")){
			if (plugin.buildDeniedList.contains(args[0])){
				plugin.buildDeniedList.removePlayer(args[0]);
				plugin.violationTracker.resetPlayer(args[0]);
			}
		}else{
			if (plugin.buildDeniedList.contains(args[0]) == false){
				plugin.buildDeniedList.addPlayer(args[0]);
				plugin.playersPassedChecks.removePlayer(args[0]);
			}
		}
		
		plugin.messagePlayer(sender, ChatColor.GREEN + "Permissions set for " + args[0]);
		
		return true;
	}
	
}
