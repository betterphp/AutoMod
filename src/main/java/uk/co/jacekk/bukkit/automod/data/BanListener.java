package uk.co.jacekk.bukkit.automod.data;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;

import com.minebans.events.PlayerBanEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.baseplugin.v7.event.BaseListener;

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
	
	public void onPlayerKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (player.isBanned()){
			plugin.blockedPlayers.remove(playerName);
			plugin.trustedPlayers.remove(playerName);
			plugin.playerDataManager.unregisterPlayer(playerName);
		}
	}
	
}
