package uk.co.jacekk.bukkit.automod.checks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class BuildDeniedListener implements Listener {
	
	private AutoMod plugin;
	
	public BuildDeniedListener(AutoMod plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.blockedPlayers.contains(playerName)){
			plugin.messagePlayer(player, ChatColor.RED + "You do not have build permissions, try /build");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerIntereactEntityEvent(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.blockedPlayers.contains(playerName)){
			plugin.messagePlayer(player, ChatColor.RED + "You do not have build permissions, try /build");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.blockedPlayers.contains(playerName)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.blockedPlayers.contains(playerName)){
			event.setCancelled(true);
		}
	}
	
}
