package uk.co.jacekk.bukkit.automod.data;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.minebans.events.PlayerBanEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class BanListener implements Listener {
	
	private AutoMod plugin;
	
	public BanListener(AutoMod plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBan(PlayerBanEvent event){
		String playerName = event.getPlayerName();
		
		plugin.blockedPlayers.remove(playerName);
		plugin.trustedPlayers.remove(playerName);
		plugin.playerDataManager.unregisterPlayer(playerName);
	}
	
}
