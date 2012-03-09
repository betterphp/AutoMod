package uk.co.jacekk.bukkit.automod.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class BuildDeniedListExecutor implements CommandExecutor {
	
	private AutoMod plugin;
	
	public BuildDeniedListExecutor(AutoMod plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender instanceof Player == false){
			sender.sendMessage("Sorry the /builddeniedlist command can only be used in game.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (sender.hasPermission("automod.admin.builddeniedlist") == false){
			plugin.messagePlayer(player, ChatColor.RED + "You do not have permissions to use this command.");
			return true;
		}
		
		List<String> playerList = plugin.buildDeniedList.getPlayerNames();
		String totalNames = (new Integer(playerList.size())).toString();
		
		if (playerList.size() == 1){
			plugin.messagePlayer(player, ChatColor.BLUE + totalNames + " player is blocked:");
		}else{
			plugin.messagePlayer(player, ChatColor.BLUE + totalNames + " players are blocked:");
		}
		
		for (String line : plugin.chatFormat.listToColumns(playerList)){
			player.sendMessage(ChatColor.AQUA + line);
		}
		
		return true;
	}
	
}
