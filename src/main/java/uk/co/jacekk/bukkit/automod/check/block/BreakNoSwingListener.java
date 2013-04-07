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
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;

public class BreakNoSwingListener extends BaseListener<AutoMod> {
	
	public BreakNoSwingListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			BlockBreakData blockBreakData = BlockBreakData.getData(player);
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			if (blockBreakData.noSwingVL > plugin.config.getInt(Config.CHECK_BLOCK_BREAK_NO_SWING_LIMIT)){
				plugin.removeBuildFor(player, Check.BLOCK_BREAK_NO_SWING);
				playerData.blockBreakVL = blockBreakData.noSwingVL;
			}
		}
	}
	
}
