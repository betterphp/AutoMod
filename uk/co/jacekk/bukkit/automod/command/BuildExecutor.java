package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.automod.vote.VoteData;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class BuildExecutor extends BaseCommandExecutor<AutoMod> {
	
	public BuildExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender instanceof Player == false){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Sorry the /build command can only be used in game"));
			return true;
		}
		
		Player player = (Player) sender;
		String playerName = player.getName();
		
		if (!plugin.blockedPlayers.contains(playerName)){
			player.sendMessage(plugin.formatMessage(ChatColor.RED + "You already have build permission"));
			return true;
		}
		
		if (plugin.voteDataManager.gotDataFor(playerName)){
			player.sendMessage(plugin.formatMessage(ChatColor.RED + "You already have an open request"));
			return true;
		}
		
		int totalVotesNeeded = 0;
		
		for (Player voter : Permission.ADMIN_VOTE.getPlayersWith()){
			voter.sendMessage(plugin.formatMessage(ChatColor.AQUA + playerName + " is requesting to be removed from the block list"));
			voter.sendMessage(plugin.formatMessage(ChatColor.AQUA + "Should they be allowed to build again ?"));
			voter.sendMessage(plugin.formatMessage(ChatColor.AQUA + "Type /vote " + playerName + " yes or /vote " + playerName + " no"));
			
			if (Permission.ADMIN_DATA.hasPermission(voter)){
				voter.sendMessage(plugin.formatMessage(ChatColor.AQUA + "You can use /data to see theit data"));
			}
			
			++totalVotesNeeded;
		}
		
		if (totalVotesNeeded == 0){
			player.sendMessage(plugin.formatMessage(ChatColor.RED + "Sorry, at least 1 other player needs to be online."));
			return true;
		}
		
		plugin.voteDataManager.registerPlayer(playerName);
		
		VoteData voteData = plugin.voteDataManager.getPlayerVoteData(playerName);
		
		voteData.totalNeeded = totalVotesNeeded;
		
		player.sendMessage(plugin.formatMessage(ChatColor.GREEN + "Your request has been sent."));
		
		return true;
	}
	
}
