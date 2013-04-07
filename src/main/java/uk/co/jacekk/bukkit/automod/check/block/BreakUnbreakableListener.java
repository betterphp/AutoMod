package uk.co.jacekk.bukkit.automod.check.block;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.data.BlockLocation;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;

public class BreakUnbreakableListener extends BaseListener<AutoMod> {
	
	private List<Material> unbreakableTypes;
	
	public BreakUnbreakableListener(AutoMod plugin){
		super(plugin);
		
		this.unbreakableTypes = Arrays.asList(
			Material.WATER, Material.STATIONARY_WATER,
			Material.LAVA, Material.STATIONARY_LAVA
		);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			Block block = event.getBlock();
			Material type = block.getType();
			
			if (this.unbreakableTypes.contains(type) && !playerData.placedBlocks.contains(new BlockLocation(block.getX(), block.getY(), block.getZ()))){
				playerData.addUnbreakableBlockBreak(type);
				
				if (playerData.unbreakableBlocksBroken > plugin.config.getInt(Config.CHECK_BLOCK_BREAK_UNBREAKABLE_LIMIT)){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_UNBREAKABLE);
				}
			}
		}
	}
	
}
