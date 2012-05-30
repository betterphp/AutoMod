package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class DataExecutor extends BaseCommandExecutor<AutoMod> {
	
	public DataExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (!Permission.ADMIN_DATA.hasPermission(sender)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command"));
			return true;
		}
		
		if (args.length != 1){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Usage: /" + label + " <player_name>"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " wide_load"));
			return true;
		}
		
		String playerName = args[0];
		
		if (!plugin.blockedPlayers.contains(playerName)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + playerName + " was not found on the block list"));
			return true;
		}
		
		
		
		return true;
	}
	
}
