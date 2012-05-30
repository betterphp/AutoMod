package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class BuildExecutor extends BaseCommandExecutor<AutoMod> {
	
	public BuildExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender instanceof Player == false){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Sorry the /build command can only be used in game."));
			return true;
		}
		
		Player player = (Player) sender;
		String playerName = player.getName();
		
		if (plugin.blockedPlayers.contains(playerName) == false){
			player.sendMessage(plugin.formatMessage(ChatColor.RED + "You already have build permission."));
			return true;
		}
		
		int totalVotesNeeded = 0;
		
		for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()){
			if (onlinePlayer.hasPermission("automod.vote.build")){
				onlinePlayer.sendMessage(plugin.formatMessage(ChatColor.AQUA + playerName + " is requesting build permissions."));
				onlinePlayer.sendMessage(plugin.formatMessage(ChatColor.AQUA + "Should they be allowed to build again ?"));
				onlinePlayer.sendMessage(plugin.formatMessage(ChatColor.AQUA + "Type /vote " + playerName + " yes or /vote " + playerName + " no"));
				
				++totalVotesNeeded;
			}
		}
		
		if (totalVotesNeeded == 0){
			player.sendMessage(plugin.formatMessage(ChatColor.RED + "Sorry, at least 1 other player needs to be online."));
			return true;
		}
		
		player.sendMessage(plugin.formatMessage(ChatColor.GREEN + "Your request has been sent."));
		
		return true;
	}
	
}
