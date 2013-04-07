package uk.co.jacekk.bukkit.automod.check.block;

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
		
		this.nauralTypesNormal = Arrays.asList(
			Material.GRASS,
			Material.DIRT,
			Material.LOG,
			Material.LEAVES,
			Material.VINE,
			Material.LONG_GRASS,
			Material.RED_ROSE,
			Material.YELLOW_FLOWER,
			Material.SUGAR_CANE_BLOCK,
			Material.RED_MUSHROOM,
			Material.BROWN_MUSHROOM,
			Material.SAND,
			Material.CACTUS,
			Material.STONE,
			Material.GRAVEL,
			Material.CROPS,
			Material.CLAY,
			Material.STONE,
			Material.ICE,
			Material.SNOW,
			Material.COAL_ORE,
			Material.IRON_ORE,
			Material.GOLD_ORE,
			Material.LAPIS_ORE,
			Material.REDSTONE_ORE,
			Material.DIAMOND_ORE,
			Material.OBSIDIAN
		);
		
		this.nauralTypesNether = Arrays.asList(
			Material.NETHERRACK,
			Material.GLOWSTONE,
			Material.NETHER_BRICK,
			Material.NETHER_FENCE,
			Material.NETHER_BRICK_STAIRS,
			Material.NETHER_WARTS,
			Material.SOUL_SAND,
			Material.BROWN_MUSHROOM,
			Material.RED_MUSHROOM
		);
		
		this.nauralTypesTheEnd = Arrays.asList(
			Material.ENDER_STONE,
			Material.OBSIDIAN,
			Material.DRAGON_EGG
		);
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
