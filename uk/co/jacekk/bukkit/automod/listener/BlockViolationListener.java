package uk.co.jacekk.bukkit.automod.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;
import de.diddiz.LogBlock.QueryParams.Order;

public class BlockViolationListener implements Listener {
	
	private AutoMod plugin;
	
	private ArrayList<Material> naturalBlocksNormal;
	private ArrayList<Material> naturalBlocksNether;
	
	public BlockViolationListener(AutoMod plugin){
		this.plugin = plugin;
		
		this.naturalBlocksNormal = new ArrayList<Material>();
		this.naturalBlocksNether = new ArrayList<Material>();
		
		this.naturalBlocksNormal.add(Material.GRASS);
		this.naturalBlocksNormal.add(Material.DIRT);
		this.naturalBlocksNormal.add(Material.LOG);
		this.naturalBlocksNormal.add(Material.LEAVES);
		this.naturalBlocksNormal.add(Material.LONG_GRASS);
		this.naturalBlocksNormal.add(Material.RED_ROSE);
		this.naturalBlocksNormal.add(Material.YELLOW_FLOWER);
		this.naturalBlocksNormal.add(Material.SUGAR_CANE_BLOCK);
		this.naturalBlocksNormal.add(Material.RED_MUSHROOM);
		this.naturalBlocksNormal.add(Material.BROWN_MUSHROOM);
		this.naturalBlocksNormal.add(Material.SAND);
		this.naturalBlocksNormal.add(Material.CACTUS);
		this.naturalBlocksNormal.add(Material.STONE);
		this.naturalBlocksNormal.add(Material.GRAVEL);
		this.naturalBlocksNormal.add(Material.CROPS);
		this.naturalBlocksNormal.add(Material.CLAY);
		this.naturalBlocksNormal.add(Material.STONE);
		this.naturalBlocksNormal.add(Material.ICE);
		this.naturalBlocksNormal.add(Material.COAL_ORE);
		this.naturalBlocksNormal.add(Material.IRON_ORE);
		this.naturalBlocksNormal.add(Material.GOLD_ORE);
		this.naturalBlocksNormal.add(Material.LAPIS_ORE);
		this.naturalBlocksNormal.add(Material.REDSTONE_ORE);
		this.naturalBlocksNormal.add(Material.DIAMOND_ORE);
		this.naturalBlocksNormal.add(Material.OBSIDIAN);
		
		this.naturalBlocksNether.add(Material.NETHERRACK);
		this.naturalBlocksNether.add(Material.NETHER_BRICK);
		this.naturalBlocksNether.add(Material.NETHER_FENCE);
		this.naturalBlocksNether.add(Material.NETHER_BRICK_STAIRS);
		this.naturalBlocksNether.add(Material.NETHER_WARTS);
		this.naturalBlocksNether.add(Material.SOUL_SAND);
		this.naturalBlocksNether.add(Material.BROWN_MUSHROOM);
		this.naturalBlocksNether.add(Material.RED_MUSHROOM);
	}
	
	private boolean isNaturalBlock(Block block, boolean strict){
		if (block.getWorld().getEnvironment() == Environment.NETHER){
			return this.naturalBlocksNether.contains(block.getType());
		}else{
			if (strict && block.getType() == Material.LOG){
				return false;
			}
			
			return this.naturalBlocksNormal.contains(block.getType());
		}
	}
	
	private boolean isNaturalBlock(Block block){
		return this.isNaturalBlock(block, false);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.playersPassedChecks.contains(player) == false){
			Block block = event.getBlock();
			int blockId = block.getTypeId();
			
			if (plugin.buildDeniedList.contains(player) == false && (player.hasPermission("automod.watch.all") || player.hasPermission("automod.watch.build"))){
				plugin.violationTracker.addBlockEvent(player);
				
				if (this.isNaturalBlock(block) == false){
					plugin.violationTracker.addBlockBreakViolation(player);
				}
				
				if (plugin.violationTracker.getBlockEvents(player) >= 40){
					plugin.playersPassedChecks.addPlayer(player);
					return;
				}else if (plugin.violationTracker.getBlockBreakViolations(player) > 8){
					plugin.removeBuildFor(player, "Breaking something that looks like a building");
					return;
				}
			}
			
			if (plugin.logblock != null && (player.hasPermission("automod.watch.all") || player.hasPermission("automod.watch.logblock"))){
				if (this.isNaturalBlock(block) == false){
					try{
						QueryParams params = new QueryParams(plugin.logblock);
						
						params.loc = event.getBlock().getLocation();
						params.world = params.loc.getWorld();
						params.bct = BlockChangeType.CREATED;
						params.order = Order.DESC;
						params.limit = 1;
						
						params.needType = true;
						params.needPlayer = true;
						
						List<BlockChange> changes = plugin.logblock.getBlockChanges(params);
						
						if (changes.size() > 0){
							BlockChange change = changes.get(0);
							
							if (change.type == blockId && change.playerName != playerName){
								plugin.violationTracker.addLogBlockViolation(playerName);
							}
						}
					}catch (Exception e){
						plugin.log.warn("LogBlock lookup failed.");
						e.printStackTrace();
					}
					
					if (plugin.violationTracker.getLogBlockViolation(playerName) > 4){
						plugin.removeBuildFor(player, "Breaking blocks not placed by you");
						return;
					}
				}
			}
			
			if (plugin.nocheat != null && (player.hasPermission("automod.watch.all") || player.hasPermission("automod.watch.nocheat"))){
				Map<String, Object> playerData = plugin.nocheat.getPlayerData(playerName);
				
				if ((Integer) playerData.get("blockbreak.direction.vl") > 200){
					plugin.removeBuildFor(player, "Breaking a block out of sight");
					return;
				}
				
				if ((Integer) playerData.get("blockbreak.reach.vl") > 200){
					plugin.removeBuildFor(player, "Breaking a block out of the normal reach");
					return;
				}
				
				if ((Integer) playerData.get("blockbreak.noswing.vl") > 300){
					plugin.removeBuildFor(player, "No-swing hacking");
					return;
				}
			}
			
			if (player.hasPermission("automod.watch.all") || player.hasPermission("automod.watch.tools")){
				List<Material> unlikelyTools = Arrays.asList(
					Material.DIAMOND_AXE,
					Material.DIAMOND_HOE,
					Material.DIAMOND_PICKAXE,
					Material.DIAMOND_SPADE
				);
				
				if (unlikelyTools.contains(player.getItemInHand().getType())){
					plugin.removeBuildFor(player, "Using a tool that you didn't make.");
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onblockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		
		if (player.hasPermission("automod.watch.all") || player.hasPermission("automod.watch.build")){
			plugin.violationTracker.addBlockEvent(player);
		}
	}
	
}
