package uk.co.jacekk.bukkit.automod.data;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.minebans.events.PlayerBanEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.baseplugin.BaseListener;

public class BanListener extends BaseListener<AutoMod> {
	
	public BanListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBan(PlayerBanEvent event){
		String playerName = event.getPlayerName();
		
		plugin.blockedPlayers.remove(playerName);
		plugin.trustedPlayers.remove(playerName);
		plugin.playerDataManager.unregisterPlayer(playerName);
	}
	
}
