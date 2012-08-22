package uk.co.jacekk.bukkit.automod.checks;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.neatmonster.nocheatplus.checks.blockbreak.BlockBreakData;
import fr.neatmonster.nocheatplus.checks.blockplace.BlockPlaceData;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.BaseListener;

public class BlockChecksListener extends BaseListener<AutoMod> {
	
	public BlockChecksListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (!plugin.trustedPlayers.contains(playerName) && !plugin.blockedPlayers.contains(playerName) && plugin.playerDataManager.gotDataFor(playerName)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
			
			if (Permission.WATCH_ALL.hasPermission(player) || Permission.WATCH_BUILD.hasPermission(player)){
				if (playerData.unbreakableBlocksBroken > 2){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_UNBREAKABLE);
					return;
				}
				
				if (playerData.unnaturalBlocksBroken > 10){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_UNNATURAL_BLOCKS);
					return;
				}
			}
			
			if (Permission.WATCH_LOGBLOCK.hasPermission(player) && playerData.ownedBlocksBroken > 5){
				plugin.removeBuildFor(player, Check.BLOCK_BREAK_OWNED_BLOCKS);
				return;
			}
			
			if (Permission.WATCH_NOCHEAT.hasPermission(player) && plugin.nocheat != null){
				BlockBreakData blockBreakData = BlockBreakData.getData(player);
				
				if (blockBreakData.directionVL > 200){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_DIRECTION);
					playerData.blockBreakVL = blockBreakData.directionVL;
					return;
				}
				
				if (blockBreakData.reachVL > 200){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_REACH);
					playerData.blockBreakVL = blockBreakData.reachVL;
					return;
				}
				
				if (blockBreakData.noSwingVL > 200){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_NO_SWING);
					playerData.blockBreakVL = blockBreakData.noSwingVL;
					return;
				}
				
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (!plugin.trustedPlayers.contains(playerName) && !plugin.blockedPlayers.contains(playerName) && plugin.playerDataManager.gotDataFor(playerName)){
			if (plugin.nocheat != null){
				PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
				BlockPlaceData blockPlaceData = BlockPlaceData.getData(player);
				
				if (blockPlaceData.directionVL > 200){
					plugin.removeBuildFor(player, Check.BLOCK_PLACE_DIRECTION);
					playerData.blockPlaceVL = blockPlaceData.directionVL;
					return;
				}
				
				if (blockPlaceData.reachVL > 200){
					plugin.removeBuildFor(player, Check.BLOCK_PLACE_REACH);
					playerData.blockPlaceVL = blockPlaceData.reachVL;
					return;
				}
				
				if (blockPlaceData.noSwingVL > 200){
					plugin.removeBuildFor(player, Check.BLOCK_PLACE_NO_SWING);
					playerData.blockPlaceVL = blockPlaceData.noSwingVL;
					return;
				}
			}
		}
	}
	
}
