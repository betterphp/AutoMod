package uk.co.jacekk.bukkit.automod.check.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import fr.neatmonster.nocheatplus.checks.inventory.InventoryData;

public class PVPInstantBowListener extends BaseListener<AutoMod> {
	
	public PVPInstantBowListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageByEntityEvent event){
		Entity damager = event.getDamager();
		
		if (!(damager instanceof Projectile)){
			return;
		}
		
		LivingEntity shooter = ((Projectile) damager).getShooter();
		
		if (!(shooter instanceof Player)){
			return;
		}
		
		Player player = (Player) shooter;
		
		if (plugin.shouldCheck(player)){
			InventoryData inventoryData = InventoryData.getData(player);
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			if (inventoryData.instantBowVL > plugin.config.getInt(Config.CHECK_PVP_INSTANT_BOW_LIMIT)){
				plugin.removeBuildFor(player, Check.PVP_CRITICAL);
				playerData.pvpVL = inventoryData.instantBowVL;
			}
		}
	}
	
}
