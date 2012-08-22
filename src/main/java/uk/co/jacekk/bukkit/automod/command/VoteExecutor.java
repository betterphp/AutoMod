package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.automod.vote.VoteData;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class VoteExecutor extends BaseCommandExecutor<AutoMod> {
	
	public VoteExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (!Permission.ADMIN_VOTE.has(sender)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command"));
			return true;
		}
		
		if (args.length != 2){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Usage: /" + label + " [player_name] [yes/no]"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " wide_load yes"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " jarifle no"));
			return true;
		}
		
		String playerName = args[0];
		String voterName = sender.getName();
		
		if (plugin.blockedPlayers.contains(playerName)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command"));
			return true;
		}
		
		if (!plugin.voteDataManager.gotDataFor(playerName)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "There is no open vote for " + playerName));
			return true;
		}
		
		VoteData voteData = plugin.voteDataManager.getPlayerVoteData(playerName);
		
		if (voteData.voted.contains(voterName)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You have already voted for " + playerName));
			return true;
		}
		
		if (args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("true")){
			++voteData.totalYesVotes;
		}else{
			++voteData.totalNoVotes;
		}
		
		++voteData.totalVotes;
		voteData.voted.add(voterName);
		
		sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + "Your vote has been cast"));
		
		plugin.processVoteData(playerName, voteData);
		
		return true;
	}
	
}
