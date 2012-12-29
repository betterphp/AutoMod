package uk.co.jacekk.bukkit.automod.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;
import de.diddiz.LogBlock.QueryParams.Order;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.baseplugin.v7.event.BaseListener;

public class PlayerDataListener extends BaseListener<AutoMod> {
	
	private HashMap<Environment, ArrayList<Material>> naturalBlocks;
	private ArrayList<Material> unbreakableBlocks;
	private ArrayList<Material> containerBlocks;
	
	public PlayerDataListener(AutoMod plugin){
		super(plugin);
		
		ArrayList<Material> naturalBlocksNormal = new ArrayList<Material>();
		ArrayList<Material> naturalBlocksNether = new ArrayList<Material>();
		ArrayList<Material> naturalBlocksTheEnd = new ArrayList<Material>();
		
		naturalBlocksNormal.add(Material.GRASS);
		naturalBlocksNormal.add(Material.DIRT);
		naturalBlocksNormal.add(Material.LOG);
		naturalBlocksNormal.add(Material.LEAVES);
		naturalBlocksNormal.add(Material.VINE);
		naturalBlocksNormal.add(Material.LONG_GRASS);
		naturalBlocksNormal.add(Material.RED_ROSE);
		naturalBlocksNormal.add(Material.YELLOW_FLOWER);
		naturalBlocksNormal.add(Material.SUGAR_CANE_BLOCK);
		naturalBlocksNormal.add(Material.RED_MUSHROOM);
		naturalBlocksNormal.add(Material.BROWN_MUSHROOM);
		naturalBlocksNormal.add(Material.SAND);
		naturalBlocksNormal.add(Material.CACTUS);
		naturalBlocksNormal.add(Material.STONE);
		naturalBlocksNormal.add(Material.GRAVEL);
		naturalBlocksNormal.add(Material.CROPS);
		naturalBlocksNormal.add(Material.CLAY);
		naturalBlocksNormal.add(Material.STONE);
		naturalBlocksNormal.add(Material.ICE);
		naturalBlocksNormal.add(Material.SNOW);
		naturalBlocksNormal.add(Material.COAL_ORE);
		naturalBlocksNormal.add(Material.IRON_ORE);
		naturalBlocksNormal.add(Material.GOLD_ORE);
		naturalBlocksNormal.add(Material.LAPIS_ORE);
		naturalBlocksNormal.add(Material.REDSTONE_ORE);
		naturalBlocksNormal.add(Material.DIAMOND_ORE);
		naturalBlocksNormal.add(Material.OBSIDIAN);
		
		naturalBlocksNether.add(Material.NETHERRACK);
		naturalBlocksNether.add(Material.GLOWSTONE);
		naturalBlocksNether.add(Material.NETHER_BRICK);
		naturalBlocksNether.add(Material.NETHER_FENCE);
		naturalBlocksNether.add(Material.NETHER_BRICK_STAIRS);
		naturalBlocksNether.add(Material.NETHER_WARTS);
		naturalBlocksNether.add(Material.SOUL_SAND);
		naturalBlocksNether.add(Material.BROWN_MUSHROOM);
		naturalBlocksNether.add(Material.RED_MUSHROOM);
		
		naturalBlocksTheEnd.add(Material.ENDER_STONE);
		naturalBlocksTheEnd.add(Material.OBSIDIAN);
		naturalBlocksTheEnd.add(Material.DRAGON_EGG);
		
		this.naturalBlocks = new HashMap<Environment, ArrayList<Material>>();
		
		this.naturalBlocks.put(Environment.NORMAL, naturalBlocksNormal);
		this.naturalBlocks.put(Environment.NETHER, naturalBlocksNether);
		this.naturalBlocks.put(Environment.THE_END, naturalBlocksTheEnd);
		
		this.unbreakableBlocks = new ArrayList<Material>();
		
		this.unbreakableBlocks.add(Material.WATER);
		this.unbreakableBlocks.add(Material.STATIONARY_WATER);
		this.unbreakableBlocks.add(Material.LAVA);
		this.unbreakableBlocks.add(Material.STATIONARY_LAVA);
		
		this.containerBlocks = new ArrayList<Material>();
		
		this.containerBlocks.add(Material.CHEST);
		this.containerBlocks.add(Material.FURNACE);
		this.containerBlocks.add(Material.DISPENSER);
	}
	
	private boolean isNaturalBlock(Block block){
		Environment env = block.getWorld().getEnvironment();
		
		if (!this.naturalBlocks.containsKey(env)){
			return true;
		}
		
		return this.naturalBlocks.get(env).contains(block.getType());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		String playerName = event.getPlayer().getName();
		
		if (plugin.trustedPlayers.contains(playerName)){
			return;
		}
		
		if (!plugin.playerDataManager.gotDataFor(playerName)){
			plugin.playerDataManager.registerPlayer(playerName);
		}
		
		PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
		
		playerData.lastJoinTime = System.currentTimeMillis();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event){
		String playerName = event.getPlayer().getName();
		
		if (plugin.playerDataManager.gotDataFor(playerName)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
			
			playerData.lastQuitTime = System.currentTimeMillis();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.playerDataManager.gotDataFor(playerName) && !plugin.config.getStringList(Config.IGNORE_WORLDS).contains(player.getWorld().getName())){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
			
			Block block = event.getBlock();
			Material type = block.getType();
			
			if (!playerData.placedBlocks.contains(new BlockLocation(block.getX(), block.getY(), block.getZ()))){
				if (this.unbreakableBlocks.contains(type)){
					playerData.addUnbreakableBlockBreak(type);
				}else if (this.isNaturalBlock(block)){
					playerData.addNaturalBlockBreak(type);
				}else{
					if (type == Material.MELON_BLOCK || type == Material.PUMPKIN){
						if (block.getRelative(BlockFace.DOWN).getType() != Material.DIRT){
							playerData.addUnnaturalBlockBreak(type);
						}
					}else{
						playerData.addUnnaturalBlockBreak(type);
					}
					
					if (Permission.WATCH_LOGBLOCK.has(player) && plugin.logblock != null){
						try{
							QueryParams params = new QueryParams(plugin.logblock);
							
							params.loc = block.getLocation();
							params.world = params.loc.getWorld();
							params.bct = BlockChangeType.CREATED;
							params.order = Order.DESC;
							params.limit = 1;
							
							params.needType = true;
							params.needPlayer = true;
							
							List<BlockChange> changes = plugin.logblock.getBlockChanges(params);
							
							if (changes.size() > 0){
								BlockChange change = changes.get(0);
								
								if (change.type == block.getTypeId() && !change.playerName.equalsIgnoreCase(playerName)){
									playerData.addOwnedBlockBreak(type);
								}
							}
						}catch (Exception e){
							plugin.log.warn("LogBlock lookup failed.");
							e.printStackTrace();
						}
					}
				}
				
				++playerData.totalBlocksBroken;
				++playerData.totalBlockEvents;
				
				if (playerData.totalBlockEvents >= 40){
					plugin.trustedPlayers.add(playerName);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (plugin.playerDataManager.gotDataFor(playerName) && !plugin.config.getStringList(Config.IGNORE_WORLDS).contains(player.getWorld().getName())){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
			Block block = event.getBlock();
			
			++playerData.totalBlocksPlaced;
			++playerData.totalBlockEvents;
			
			playerData.placedBlocks.add(new BlockLocation(block.getX(), block.getY(), block.getZ()));
			
			if (playerData.totalBlockEvents >= 40){
				plugin.trustedPlayers.add(playerName);
			}
		}
	}
	
}
