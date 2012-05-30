package uk.co.jacekk.bukkit.automod.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class BuildDeniedListExecutor extends BaseCommandExecutor<AutoMod> {
	
	public BuildDeniedListExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender.hasPermission("automod.admin.builddeniedlist") == false){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permissions to use this command."));
			return true;
		}
		
		List<String> playerList = plugin.blockedPlayers.getAll();
		String totalNames = (new Integer(playerList.size())).toString();
		
		if (playerList.size() == 1){
			sender.sendMessage(plugin.formatMessage(ChatColor.BLUE + totalNames + " player is blocked:"));
		}else{
			sender.sendMessage(plugin.formatMessage(ChatColor.BLUE + totalNames + " players are blocked:"));
		}
		
		for (String playerName : playerList){
			sender.sendMessage("  - " + playerName);
		}
		
		return true;
	}
	
}
