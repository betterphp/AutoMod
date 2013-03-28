package uk.co.jacekk.bukkit.automod.check.block;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.neatmonster.nocheatplus.checks.blockplace.BlockPlaceData;
import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public class PlaceReachListener extends BaseListener<AutoMod> {
	
	public PlaceReachListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			BlockPlaceData blockPlaceData = BlockPlaceData.getData(player);
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			if (blockPlaceData.reachVL > 200){
				plugin.removeBuildFor(player, Check.BLOCK_PLACE_REACH);
				playerData.blockPlaceVL = blockPlaceData.reachVL;
			}
		}
	}
	
}
