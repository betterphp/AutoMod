package uk.co.jacekk.bukkit.automod.checks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.AutoModListener;
import uk.co.jacekk.bukkit.automod.data.PlayerData;

public class BlockChecksListener extends AutoModListener {
	
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
				
			}
			
			if (playerData.unnaturalBlocksBroken > 8){
				
			}
			
			if (playerData.ownedBlocksBroken > 6){
				
			}
			
			if (plugin.nocheat != null){
				Map<String, Object> noCheatData = plugin.nocheat.getPlayerData(playerName);
				
				if ((Integer) noCheatData.get("blockbreak.direction.vl") > 200){
					plugin.removeBuildFor(player, "Breaking a block out of sight");
					return;
				}
				
				if ((Integer) noCheatData.get("blockbreak.reach.vl") > 200){
					plugin.removeBuildFor(player, "Breaking a block out of the normal reach");
					return;
				}
				
				if ((Integer) noCheatData.get("blockbreak.noswing.vl") > 300){
					plugin.removeBuildFor(player, "No-swing hacking");
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		String playerName = event.getPlayer().getName();
		
		if (plugin.playerDataManager.gotDataFor(playerName)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
			
			
		}
	}
	
}
