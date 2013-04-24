package uk.co.jacekk.bukkit.automod.check.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World.Environment;
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

public class BreakUnnaturalListener extends BaseListener<AutoMod> {
	
	private List<Material> nauralTypesNormal;
	private List<Material> nauralTypesNether;
	private List<Material> nauralTypesTheEnd;
	
	public BreakUnnaturalListener(AutoMod plugin){
		super(plugin);
		
		this.nauralTypesNormal = new ArrayList<Material>();
		this.nauralTypesNormal.add(Material.GRASS);
		this.nauralTypesNormal.add(Material.DIRT);
		this.nauralTypesNormal.add(Material.LOG);
		this.nauralTypesNormal.add(Material.LEAVES);
		this.nauralTypesNormal.add(Material.VINE);
		this.nauralTypesNormal.add(Material.LONG_GRASS);
		this.nauralTypesNormal.add(Material.RED_ROSE);
		this.nauralTypesNormal.add(Material.YELLOW_FLOWER);
		this.nauralTypesNormal.add(Material.SUGAR_CANE_BLOCK);
		this.nauralTypesNormal.add(Material.RED_MUSHROOM);
		this.nauralTypesNormal.add(Material.BROWN_MUSHROOM);
		this.nauralTypesNormal.add(Material.SAND);
		this.nauralTypesNormal.add(Material.CACTUS);
		this.nauralTypesNormal.add(Material.STONE);
		this.nauralTypesNormal.add(Material.GRAVEL);
		this.nauralTypesNormal.add(Material.CROPS);
		this.nauralTypesNormal.add(Material.CLAY);
		this.nauralTypesNormal.add(Material.STONE);
		this.nauralTypesNormal.add(Material.ICE);
		this.nauralTypesNormal.add(Material.SNOW);
		this.nauralTypesNormal.add(Material.COAL_ORE);
		this.nauralTypesNormal.add(Material.IRON_ORE);
		this.nauralTypesNormal.add(Material.GOLD_ORE);
		this.nauralTypesNormal.add(Material.LAPIS_ORE);
		this.nauralTypesNormal.add(Material.REDSTONE_ORE);
		this.nauralTypesNormal.add(Material.DIAMOND_ORE);
		this.nauralTypesNormal.add(Material.OBSIDIAN);
		
		if (plugin.config.getBoolean(Config.CHECK_BLOCK_BREAK_UNNATURAL_INCLUDE_FARMS)){
			this.nauralTypesNether.add(Material.CROPS);
			this.nauralTypesNether.add(Material.MELON);
			this.nauralTypesNether.add(Material.PUMPKIN);
			this.nauralTypesNether.add(Material.SUGAR_CANE_BLOCK);
			this.nauralTypesNether.add(Material.POTATO);
			this.nauralTypesNether.add(Material.CARROT);
		}
		
		this.nauralTypesNether = Arrays.asList(Material.NETHERRACK, Material.GLOWSTONE, Material.NETHER_BRICK, Material.NETHER_FENCE, Material.NETHER_BRICK_STAIRS, Material.NETHER_WARTS, Material.SOUL_SAND, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM);
		
		this.nauralTypesTheEnd = Arrays.asList(Material.ENDER_STONE, Material.OBSIDIAN, Material.DRAGON_EGG);
	}
	
	private boolean isNaturalType(Environment environment, Material type){
		switch (environment){
			case NORMAL:
				return this.nauralTypesNormal.contains(type);
			case NETHER:
				return this.nauralTypesNether.contains(type);
			case THE_END:
				return this.nauralTypesTheEnd.contains(type);
			default:
				return false;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		
		if (plugin.shouldCheck(player)){
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			Block block = event.getBlock();
			Material type = block.getType();
			
			if (!this.isNaturalType(block.getWorld().getEnvironment(), type) && !playerData.placedBlocks.contains(new BlockLocation(block.getX(), block.getY(), block.getZ()))){
				playerData.addUnnaturalBlockBreak(type);
				
				if (playerData.unnaturalBlocksBroken > plugin.config.getInt(Config.CHECK_BLOCK_BREAK_UNNATURAL_LIMIT)){
					plugin.removeBuildFor(player, Check.BLOCK_BREAK_UNNATURAL);
				}
			}
		}
	}
	
}
