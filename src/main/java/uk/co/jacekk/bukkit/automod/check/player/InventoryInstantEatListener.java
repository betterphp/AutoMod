package uk.co.jacekk.bukkit.automod.check.player;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import fr.neatmonster.nocheatplus.checks.inventory.InventoryData;

public class InventoryInstantEatListener extends BaseListener<AutoMod> {
	
	public InventoryInstantEatListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDamage(FoodLevelChangeEvent event){
		HumanEntity entity = event.getEntity();
		
		if (!(entity instanceof Player)){
			return;
		}
		
		Player player = (Player) entity;
		
		if (plugin.shouldCheck(player)){
			InventoryData inventoryData = InventoryData.getData(player);
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			if (inventoryData.instantEatVL > plugin.config.getInt(Config.CHECK_INVENTORY_INSTANT_EAT_LIMIT)){
				plugin.removeBuildFor(player, Check.INVENTORY_INSTANT_EAT);
				playerData.pvpVL = inventoryData.instantEatVL;
			}
		}
	}
	
}
