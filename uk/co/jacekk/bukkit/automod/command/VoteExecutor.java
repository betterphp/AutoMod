package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class VoteExecutor implements CommandExecutor {
	
	private AutoMod plugin;
	
	public VoteExecutor(AutoMod plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender instanceof Player == false){
			sender.sendMessage("Sorry the /vote command can only be used in game.");
			return true;
		}
		
		Player player = (Player) sender;
		String playerName = player.getName();
		
		if (player.hasPermission("automod.vote.build") == false){
			plugin.messagePlayer(player, ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}
		
		if (args.length != 2){
			plugin.messagePlayer(player, ChatColor.AQUA + "Usage: /vote [player_name] [yes / no]");
			plugin.messagePlayer(player, ChatColor.AQUA + "Example: /vote wide_load yes");
			return true;
		}
		
		if (plugin.voteTracker.voteExistsFor(args[0]) == false){
			plugin.messagePlayer(player, ChatColor.RED + "There is no open vote for " + args[0]);
			return true;
		}
		
		if (args[1].equalsIgnoreCase("yes") == false && args[1].equalsIgnoreCase("no") == false){
			plugin.messagePlayer(player, ChatColor.RED + "Your vote must be either yes or no.");
			return true;
		}
		
		if (plugin.voteTracker.getOutstandingVotersFor(args[0]).contains(playerName) == false){
			plugin.messagePlayer(player, ChatColor.RED + "You have already voted on this player.");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("yes")){
			plugin.voteTracker.addYesVoteFor(args[0]);
		}else{
			plugin.voteTracker.addNoVoteFor(args[0]);
		}
		
		plugin.messagePlayer(player,  ChatColor.GREEN + "Your vote has been cast.");
		
		plugin.voteTracker.removeVoterFor(args[0], playerName);
		
		if (plugin.voteTracker.getOutstandingVotersFor(args[0]).size() == 0){
			if (plugin.voteTracker.getYesVotesFor(args[0]) > 0 && plugin.voteTracker.getNoVotesFor(args[0]) == 0){
				plugin.blockedPlayers.remove(args[0]);
				plugin.violationTracker.resetPlayer(args[0]);
				
				plugin.messagePlayer(plugin.getServer().getPlayer(args[0]), ChatColor.GREEN + "Your request has been approved :)");
			}else{
				plugin.messagePlayer(plugin.getServer().getPlayer(args[0]), ChatColor.RED + "Your request has been denied :(");
			}
			
			plugin.voteTracker.removePlayer(args[0]);
		}
		
		return true;
	}
	
}
