package uk.co.jacekk.bukkit.automod.checks;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
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
		
		if (plugin.playerDataManager.gotDataFor(playerName)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
			
			if (playerData.unbreakableBlocksBroken > 2){
				plugin.removeBuildFor(player, Check.BLOCK_BREAK_UNBREAKABLE);
				return;
			}
			
			if (playerData.unnaturalBlocksBroken > 10){
				plugin.removeBuildFor(player, Check.BLOCK_BREAK_UNNATURAL_BLOCKS);
				return;
			}
			
			if (playerData.ownedBlocksBroken > 5){
				plugin.removeBuildFor(player, Check.BLOCK_BREAK_OWNED_BLOCKS);
				return;
			}
			
			if (plugin.nocheat != null){
				Map<String, Object> noCheatData = plugin.nocheat.getPlayerData(playerName);
				
				if ((Integer) noCheatData.get("blockbreak.direction.vl") > 200){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_DIRECTION);
					return;
				}
				
				if ((Integer) noCheatData.get("blockbreak.reach.vl") > 200){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_REACH);
					return;
				}
				
				if ((Integer) noCheatData.get("blockbreak.noswing.vl") > 300){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_NO_SWING);
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.playerDataManager.gotDataFor(playerName)){
			if (plugin.nocheat != null){
				Map<String, Object> noCheatData = plugin.nocheat.getPlayerData(playerName);
				
				if ((Integer) noCheatData.get("blockplace.direction.vl") > 200){
					plugin.removeBuildFor(player, Check.BLOCK_PLACE_DIRECTION);
					return;
				}
				
				if ((Integer) noCheatData.get("blockplace.reach.vl") > 200){
					plugin.removeBuildFor(player, Check.BLOCK_PLACE_REACH);
					return;
				}
			}
		}
	}
	
}
