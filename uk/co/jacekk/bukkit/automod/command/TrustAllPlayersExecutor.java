package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class TrustAllPlayersExecutor implements CommandExecutor {
	
	private AutoMod plugin;
	
	public TrustAllPlayersExecutor(AutoMod plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender.hasPermission("automod.admin.trustallplayers") == false){
			plugin.messagePlayer(sender, ChatColor.RED + "You do not have permissions to use this command.");
			return true;
		}
		
		OfflinePlayer[] playerList = plugin.getServer().getOfflinePlayers();
		
		String trustedName;
		
		for (OfflinePlayer trustedPlayer : playerList){
			trustedName = trustedPlayer.getName();
			
			plugin.blockedPlayers.remove(trustedName);
			plugin.voteTracker.removePlayer(trustedName);
			
			if (plugin.trustedPlayers.contains(trustedName) == false){
				plugin.trustedPlayers.add(trustedName);
			}
		}
		
		plugin.messagePlayer(sender, ChatColor.GREEN + (new Integer(playerList.length)).toString() + " players have been added to the trusted list.");
		
		return true;
	}
	
}
