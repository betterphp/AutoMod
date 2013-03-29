package uk.co.jacekk.bukkit.automod.check.block;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.neatmonster.nocheatplus.checks.blockplace.BlockPlaceData;
import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public class PlaceNoSwingListener extends BaseListener<AutoMod> {
	
	public PlaceNoSwingListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			BlockPlaceData blockPlaceData = BlockPlaceData.getData(player);
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			if (blockPlaceData.noSwingVL > plugin.config.getInt(Config.CHECK_BLOCK_PLACE_NO_SWING_LIMIT)){
				plugin.removeBuildFor(player, Check.BLOCK_PLACE_NO_SWING);
				playerData.blockPlaceVL = blockPlaceData.noSwingVL;
			}
		}
	}
	
}
