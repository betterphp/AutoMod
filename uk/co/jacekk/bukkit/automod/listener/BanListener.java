package uk.co.jacekk.bukkit.automod.listener;

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
		
		plugin.buildDeniedList.removePlayer(playerName);
		plugin.playersPassedChecks.removePlayer(playerName);
	}
	
}
