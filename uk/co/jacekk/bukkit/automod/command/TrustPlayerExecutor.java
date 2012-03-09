package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class TrustPlayerExecutor implements CommandExecutor {
	
	private AutoMod plugin;
	
	public TrustPlayerExecutor(AutoMod plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender instanceof Player == false){
			sender.sendMessage("Sorry the /trustallplayers command can only be used in game.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (sender.hasPermission("automod.admin.trustplayer") == false){
			plugin.messagePlayer(player, ChatColor.RED + "You do not have permissions to use this command.");
			return true;
		}
		
		if (args.length != 1){
			plugin.messagePlayer(player, ChatColor.AQUA + "Usage: /trustplayer [player_name]");
			plugin.messagePlayer(player, ChatColor.AQUA + "Example: /trustplayer wide_load");
			return true;
		}
		
		String trustedName = args[0];
		
		if (plugin.getServer().getOfflinePlayer(trustedName) == null){
			plugin.messagePlayer(player, ChatColor.RED + trustedName + " has never connected to this server.");
			return true;
		}
		
		plugin.buildDeniedList.removePlayer(trustedName);
		plugin.voteTracker.removePlayer(trustedName);
		
		if (plugin.playersPassedChecks.contains(trustedName) == false){
			plugin.playersPassedChecks.addPlayer(trustedName);
		}
		
		plugin.messagePlayer(player, ChatColor.GREEN + trustedName + " has been added to the trusted list.");
		
		return true;
	}
	
}
