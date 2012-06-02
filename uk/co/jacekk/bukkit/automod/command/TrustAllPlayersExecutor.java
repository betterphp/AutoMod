package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class TrustAllPlayersExecutor extends BaseCommandExecutor<AutoMod> {
	
	public TrustAllPlayersExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (!Permission.ADMIN_LIST_TRUSTED.hasPermission(sender)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permissions to use this command."));
			return true;
		}
		
		OfflinePlayer[] playerList = plugin.server.getOfflinePlayers();
		
		String trustedName;
		
		for (OfflinePlayer trustedPlayer : playerList){
			trustedName = trustedPlayer.getName();
			
			plugin.blockedPlayers.remove(trustedName);
			
			if (plugin.trustedPlayers.contains(trustedName) == false){
				plugin.trustedPlayers.add(trustedName);
			}
		}
		
		sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + String.valueOf(playerList.length) + " players have been added to the trusted list."));
		
		return true;
	}
	
}
