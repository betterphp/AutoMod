package uk.co.jacekk.bukkit.automod.check.block;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;
import de.diddiz.LogBlock.QueryParams.Order;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.data.BlockLocation;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public class BreakOwnedListener extends BaseListener<AutoMod> {
	
	private LogBlock logblock;
	
	public BreakOwnedListener(AutoMod plugin){
		super(plugin);
		
		this.logblock = (LogBlock) plugin.pluginManager.getPlugin("LogBlock");
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			Block block = event.getBlock();
			
			if (!playerData.placedBlocks.contains(new BlockLocation(block.getX(), block.getY(), block.getZ()))){
				try{
					QueryParams params = new QueryParams(this.logblock);
					
					params.loc = block.getLocation();
					params.world = params.loc.getWorld();
					params.bct = BlockChangeType.CREATED;
					params.order = Order.DESC;
					params.limit = 1;
					
					params.needType = true;
					params.needPlayer = true;
					
					List<BlockChange> changes = this.logblock.getBlockChanges(params);
					
					if (changes.size() > 0){
						BlockChange change = changes.get(0);
						
						if (change.type == block.getTypeId() && !change.playerName.equalsIgnoreCase(player.getName())){
							playerData.addOwnedBlockBreak(block.getType());
						}
					}
				}catch (Exception e){
					plugin.log.warn("LogBlock lookup failed.");
					e.printStackTrace();
				}
				
				if (playerData.ownedBlocksBroken > 5){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_OWNED);
				}
			}
		}
	}
	
}
