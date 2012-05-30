package uk.co.jacekk.bukkit.automod.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class TrustedPlayerListExecutor extends BaseCommandExecutor<AutoMod> {
	
	public TrustedPlayerListExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender.hasPermission("automod.admin.trustedplayerlist") == false){
			plugin.messagePlayer(sender, ChatColor.RED + "You do not have permissions to use this command.");
			return true;
		}
		
		List<String> playerList = plugin.trustedPlayers.getAll();
		String totalNames = (new Integer(playerList.size())).toString();
		
		if (playerList.size() == 1){
			plugin.messagePlayer(sender, ChatColor.BLUE + totalNames + " player is trusted:");
		}else{
			plugin.messagePlayer(sender, ChatColor.BLUE + totalNames + " players are trusted:");
		}
		
		for (String line : playerList){
			sender.sendMessage(ChatColor.AQUA + line);
		}
		
		return true;
	}
	
}
