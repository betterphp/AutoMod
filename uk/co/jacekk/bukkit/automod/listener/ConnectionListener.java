package uk.co.jacekk.bukkit.automod.listener;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class ConnectionListener implements Listener {
	
	private AutoMod plugin;
	
	public ConnectionListener(AutoMod plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		
		if (player.hasPermission("automod.watch.all")){
			plugin.violationTracker.addNewPlayer(event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		
		if (player.hasPermission("automod.vote.build")){
			plugin.voteTracker.removeVoter(player);
			
			ArrayList<String> approvedPlayers = new ArrayList<String>();
			
			for (String badPlayer : plugin.buildDeniedList.getPlayerNames()){
				if (plugin.voteTracker.voteExistsFor(badPlayer) && plugin.voteTracker.getOutstandingVotersFor(badPlayer).size() == 0){
					if (plugin.voteTracker.getYesVotesFor(badPlayer) > 0 && plugin.voteTracker.getNoVotesFor(badPlayer) == 0){
						approvedPlayers.add(badPlayer);
						
						plugin.messagePlayer(plugin.getServer().getPlayer(badPlayer), ChatColor.GREEN + "Your request has been approved :)");
					}else{
						plugin.messagePlayer(plugin.getServer().getPlayer(badPlayer), ChatColor.RED + "Your request has been denied :(");
					}
					
					plugin.voteTracker.removePlayer(badPlayer);
				}
			}
			
			for (String badPlayer : approvedPlayers){
				plugin.buildDeniedList.removePlayer(badPlayer);
				plugin.violationTracker.resetPlayer(badPlayer);
			}
		}
		
		if (plugin.voteTracker.voteExistsFor(player)){
			plugin.voteTracker.removePlayer(player);
		}
	}
	
}
