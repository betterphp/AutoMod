package uk.co.jacekk.bukkit.automod.command;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.baseplugin.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.command.CommandHandler;

public class ListExecutor extends BaseCommandExecutor<AutoMod> {
	
	public ListExecutor(AutoMod plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"list"}, description = "Manage the block and trusted player lists", usage = "[list_name] [action] [player_name]")
	public void list(CommandSender sender, String label, String[] args){
		if (args.length == 0 || args.length > 3){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Usage: /" + label + " <list_name> [action] [player_name]"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " trusted"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " blocked"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " trusted clear"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " blocked clear"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " trusted add wide_load"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " trusted removed wide_load"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " blocked add wide_load"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " blocked removed wide_load"));
			return;
		}
		
		String listName = args[0];
		
		String option = (args.length > 1) ? args[1] : "show";
		String playerName = (args.length > 2) ? args[2] : sender.getName();
		
		if (listName.equalsIgnoreCase("blocked") || listName.equalsIgnoreCase("b")){
			if (!Permission.ADMIN_LIST_ALL.has(sender) && !Permission.ADMIN_LIST_BLOCKED.has(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command"));
				return;
			}
			
			if (option.equalsIgnoreCase("add") || option.equalsIgnoreCase("a")){
				plugin.trustedPlayers.remove(playerName);
				plugin.blockedPlayers.add(playerName, String.valueOf(Check.CUSTOM_ADDITION.getId()));
				
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + playerName + " has been added to the block list"));
			}else if (option.equalsIgnoreCase("remove") || option.equalsIgnoreCase("r")){
				plugin.blockedPlayers.remove(playerName);
				plugin.playerDataManager.unregisterPlayer(playerName);
				
				if (plugin.voteDataManager.gotDataFor(playerName)){
					plugin.voteDataManager.unregisterPlayer(playerName);
					
					for (Player player : Permission.ADMIN_VOTE.getPlayersWith()){
						player.sendMessage(plugin.formatMessage(ChatColor.AQUA + "The vote for " + playerName + " has been ended"));
					}
				}
				
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + playerName + " has been removed from the block list"));
			}else if (option.equalsIgnoreCase("clear") || option.equalsIgnoreCase("c")){
				plugin.blockedPlayers.removeAll();
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + "The blocked player list has been cleared"));
			}else{
				sender.sendMessage(plugin.formatMessage("There are " + plugin.blockedPlayers.size() + " players on the block list"));
				
				for (Entry<String, String> entry : plugin.blockedPlayers.getAll()){
					sender.sendMessage(ChatColor.AQUA + " " + entry.getKey() + " - " + Check.fromId(Integer.parseInt(entry.getValue())).getDescription());
				}
			}
		}else if (listName.equalsIgnoreCase("trusted") || listName.equalsIgnoreCase("t")){
			if (!Permission.ADMIN_LIST_ALL.has(sender) && !Permission.ADMIN_LIST_TRUSTED.has(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command"));
				return;
			}
			
			if (option.equalsIgnoreCase("add") || option.equalsIgnoreCase("a")){
				plugin.blockedPlayers.remove(playerName);
				plugin.playerDataManager.unregisterPlayer(playerName);
				plugin.trustedPlayers.add(playerName);
				
				if (plugin.voteDataManager.gotDataFor(playerName)){
					plugin.voteDataManager.unregisterPlayer(playerName);
					
					for (Player player : Permission.ADMIN_VOTE.getPlayersWith()){
						player.sendMessage(plugin.formatMessage(ChatColor.AQUA + "The vote for " + playerName + " has been ended"));
					}
				}
				
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + playerName + " has been added to the trusted list"));
			}else if (option.equalsIgnoreCase("remove") || option.equalsIgnoreCase("r")){
				plugin.trustedPlayers.remove(playerName);
				
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + playerName + " has been removed from the trusted list"));
			}else if (option.equalsIgnoreCase("clear") || option.equalsIgnoreCase("c")){
				plugin.trustedPlayers.removeAll();
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + "The trusted player list has been cleared"));
			}else{
				sender.sendMessage(plugin.formatMessage("There are " + plugin.trustedPlayers.size() + " players on the trusted list"));
				
				for (String name : plugin.trustedPlayers.getAll()){
					sender.sendMessage(ChatColor.AQUA + " " + name);
				}
			}
		}else{
			sender.sendMessage(ChatColor.RED + listName + " is a not a valid list");
		}
	}
	
	@CommandHandler(names = {"trustallplayers"}, description = "Adds all of the players that have ever been on the server to the trusted list.", usage = "/trustallplayers")
	public void trustallplayers(CommandSender sender, String label, String[] args){
		if (!Permission.ADMIN_LIST_ALL.has(sender) && !Permission.ADMIN_LIST_TRUSTED.has(sender)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command."));
			return;
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
	}
	
}
