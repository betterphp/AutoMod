package uk.co.jacekk.bukkit.automod.check.block;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import fr.neatmonster.nocheatplus.checks.blockbreak.BlockBreakData;
import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public class BreakReachListener extends BaseListener<AutoMod> {
	
	public BreakReachListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			BlockBreakData blockBreakData = BlockBreakData.getData(player);
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			if (blockBreakData.reachVL > plugin.config.getInt(Config.CHECK_BLOCK_BREAK_REACH_LIMIT)){
				plugin.removeBuildFor(player, Check.BLOCK_BREAK_REACH);
				playerData.blockBreakVL = blockBreakData.reachVL;
			}
		}
	}
	
}
