package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.automod.vote.VoteData;
import uk.co.jacekk.bukkit.baseplugin.v1.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v1.command.CommandHandler;

public class BuildExecutor extends BaseCommandExecutor<AutoMod> {
	
	public BuildExecutor(AutoMod plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"build"}, description = "Requests build permissions to be restored")
	public void build(CommandSender sender, String label, String[] args){
		if (sender instanceof Player == false){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Sorry the /build command can only be used in game"));
			return;
		}
		
		Player player = (Player) sender;
		String playerName = player.getName();
		
		if (!plugin.blockedPlayers.contains(playerName)){
			player.sendMessage(plugin.formatMessage(ChatColor.RED + "You already have build permission"));
			return;
		}
		
		if (plugin.voteDataManager.gotDataFor(playerName)){
			player.sendMessage(plugin.formatMessage(ChatColor.RED + "You already have an open request"));
			return;
		}
		
		int totalVotesNeeded = 0;
		
		for (Player voter : Permission.ADMIN_VOTE.getPlayersWith()){
			if (!plugin.blockedPlayers.contains(voter.getName())){
				voter.sendMessage(plugin.formatMessage(ChatColor.AQUA + playerName + " is requesting build permissions"));
				voter.sendMessage(plugin.formatMessage(ChatColor.AQUA + "Should they be allowed to build again ?"));
				voter.sendMessage(plugin.formatMessage(ChatColor.AQUA + "Type /vote " + playerName + " yes or /vote " + playerName + " no"));
				
				if (Permission.ADMIN_DATA.has(voter)){
					voter.sendMessage(plugin.formatMessage(ChatColor.AQUA + "You can use /data to see their data"));
				}
				
				++totalVotesNeeded;
			}
		}
		
		if (totalVotesNeeded == 0){
			player.sendMessage(plugin.formatMessage(ChatColor.RED + "Sorry, at least 1 other player needs to be online."));
			return;
		}
		
		plugin.voteDataManager.registerPlayer(playerName);
		
		VoteData voteData = plugin.voteDataManager.getPlayerVoteData(playerName);
		
		voteData.totalNeeded = totalVotesNeeded;
		
		player.sendMessage(plugin.formatMessage(ChatColor.GREEN + "Your request has been sent."));
	}
	
}
