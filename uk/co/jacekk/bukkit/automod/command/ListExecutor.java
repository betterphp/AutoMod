package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class ListExecutor extends BaseCommandExecutor<AutoMod> {
	
	public ListExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (args.length != 1 && args.length != 3){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Usage: /" + label + " <list_name> [action] [player_name]"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " trusted"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " blocked"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " trusted add wide_load"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " trusted removed wide_load"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " blocked add wide_load"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " blocked removed wide_load"));
			return true;
		}
		
		String listName = args[0];
		
		String option = (args.length > 1) ? args[1] : "show";
		String playerName = (args.length > 2) ? args[2] : sender.getName();
		
		if (listName.equalsIgnoreCase("blocked") || listName.equalsIgnoreCase("b")){
			if (!Permission.ADMIN_LIST_ALL.hasPermission(sender) && !Permission.ADMIN_LIST_BLOCKED.hasPermission(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command"));
				return true;
			}
			
			if (option.equalsIgnoreCase("add") || option.equalsIgnoreCase("a")){
				plugin.trustedPlayers.remove(playerName);
				plugin.blockedPlayers.add(playerName, String.valueOf(Check.CUSTOM_ADDITION.getId()));
				
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + playerName + " has been added to the block list"));
			}else if (option.equalsIgnoreCase("remove") || option.equalsIgnoreCase("r")){
				plugin.blockedPlayers.remove(playerName);
				
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + playerName + " has been removed from the block list"));
			}else{
				sender.sendMessage(plugin.formatMessage("There are " + plugin.blockedPlayers.size() + " players on the block list"));
				
				for (String name : plugin.blockedPlayers.getkeys()){
					sender.sendMessage(ChatColor.GREEN + "  - " + name);
				}
			}
		}else if (listName.equalsIgnoreCase("trusted") || listName.equalsIgnoreCase("t")){
			if (!Permission.ADMIN_LIST_ALL.hasPermission(sender) && !Permission.ADMIN_LIST_TRUSTED.hasPermission(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command"));
				return true;
			}
			
			if (option.equalsIgnoreCase("add") || option.equalsIgnoreCase("a")){
				plugin.blockedPlayers.remove(playerName);
				plugin.trustedPlayers.add(playerName);
				
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + playerName + " has been added to the trusted list"));
			}else if (option.equalsIgnoreCase("remove") || option.equalsIgnoreCase("r")){
				plugin.blockedPlayers.remove(playerName);
				
				sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + playerName + " has been removed from the trusted list"));
			}else{
				sender.sendMessage(plugin.formatMessage("There are " + plugin.trustedPlayers.size() + " players on the trusted list"));
				
				for (String name : plugin.trustedPlayers.getAll()){
					sender.sendMessage(ChatColor.GREEN + "  - " + name);
				}
			}
		}else{
			sender.sendMessage(ChatColor.RED + listName + " is a not a valid list");
		}
		
		return true;
	}
	
}
