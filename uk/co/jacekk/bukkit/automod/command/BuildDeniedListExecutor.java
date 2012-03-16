package uk.co.jacekk.bukkit.automod.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class BuildDeniedListExecutor implements CommandExecutor {
	
	private AutoMod plugin;
	
	public BuildDeniedListExecutor(AutoMod plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender.hasPermission("automod.admin.builddeniedlist") == false){
			plugin.messagePlayer(sender, ChatColor.RED + "You do not have permissions to use this command.");
			return true;
		}
		
		List<String> playerList = plugin.blockedPlayers.getAll();
		String totalNames = (new Integer(playerList.size())).toString();
		
		if (playerList.size() == 1){
			plugin.messagePlayer(sender, ChatColor.BLUE + totalNames + " player is blocked:");
		}else{
			plugin.messagePlayer(sender, ChatColor.BLUE + totalNames + " players are blocked:");
		}
		
		for (String line : plugin.chatFormat.listToColumns(playerList)){
			sender.sendMessage(ChatColor.AQUA + line);
		}
		
		return true;
	}
	
}
