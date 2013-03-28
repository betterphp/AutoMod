package uk.co.jacekk.bukkit.automod.data;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerKickEvent;

import com.minebans.minebans.events.PlayerBanEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public class PlayerDataListener extends BaseListener<AutoMod> {
	
	public PlayerDataListener(AutoMod plugin){
		super(plugin);
	}
	
	private void checkTrust(Player player, PlayerData playerData){
		if (playerData.totalBlocksBroken + playerData.totalBlocksPlaced > 40){
			plugin.trustedPlayers.add(player.getName());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			++playerData.totalBlocksPlaced;
			
			this.checkTrust(player, playerData);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			Block block = event.getBlock();
			
			playerData.placedBlocks.add(new BlockLocation(block.getX(), block.getY(), block.getZ()));
			++playerData.totalBlocksPlaced;
			
			this.checkTrust(player, playerData);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBan(PlayerBanEvent event){
		String playerName = event.getPlayerName();
		
		plugin.blockedPlayers.remove(playerName);
		plugin.trustedPlayers.remove(playerName);
		plugin.playerDataManager.unregisterPlayer(playerName);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
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
