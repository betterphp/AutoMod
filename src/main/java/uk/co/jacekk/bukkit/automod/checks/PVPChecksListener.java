package uk.co.jacekk.bukkit.automod.checks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.neatmonster.nocheatplus.checks.fight.FightData;
import fr.neatmonster.nocheatplus.checks.inventory.InventoryData;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.v1.event.BaseListener;

public class PVPChecksListener extends BaseListener<AutoMod> {
	
	public PVPChecksListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageByEntityEvent event){
		Entity damager = event.getDamager();
		
		if (damager instanceof Player){
			Player player = (Player) damager;
			String playerName = player.getName();
			
			if (!plugin.trustedPlayers.contains(playerName) && !plugin.blockedPlayers.contains(playerName) && plugin.playerDataManager.gotDataFor(playerName)){
				if (Permission.WATCH_NOCHEAT.has(player) && plugin.nocheat != null){
					PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
					FightData fightData = FightData.getData(player);
					
					if (fightData.directionVL > 200 || fightData.angleVL > 200){
						plugin.removeBuildFor(player, Check.PVP_DIRECTION);
						playerData.pvpVL = Math.max(fightData.directionVL, fightData.angleVL);
						return;
					}
					
					if (fightData.criticalVL > 200){
						plugin.removeBuildFor(player, Check.PVP_CRITICAL);
						playerData.pvpVL = fightData.criticalVL;
						return;
					}
					
					if (fightData.knockbackVL > 200){
						plugin.removeBuildFor(player, Check.PVP_KNOCKBACK);
						playerData.pvpVL = fightData.knockbackVL;
						return;
					}
					
					if (fightData.speedVL > 200){
						plugin.removeBuildFor(player, Check.PVP_SPEED);
						playerData.pvpVL = fightData.speedVL;
						return;
					}
					
					if (fightData.godModeVL > 200){
						plugin.removeBuildFor(player, Check.PVP_GODMODE);
						playerData.pvpVL = fightData.godModeVL;
						return;
					}
					
					if (fightData.noSwingVL > 200){
						plugin.removeBuildFor(player, Check.PVP_NO_SWING);
						playerData.pvpVL = fightData.noSwingVL;
						return;
					}
					
					if (fightData.instantHealVL > 200){
						plugin.removeBuildFor(player, Check.PVP_INSTANT_HEAL);
						playerData.pvpVL = fightData.instantHealVL;
						return;
					}
				}
			}
		}else if (damager instanceof Projectile){
			LivingEntity shooter = ((Projectile) damager).getShooter();
			
			if (shooter instanceof Player){
				Player player = (Player) shooter;
				String playerName = player.getName();
				
				if (!plugin.trustedPlayers.contains(playerName) && !plugin.blockedPlayers.contains(playerName) && plugin.playerDataManager.gotDataFor(playerName)){
					if (Permission.WATCH_NOCHEAT.has(player) && plugin.nocheat != null){
						PlayerData playerData = plugin.playerDataManager.getPlayerData(playerName);
						InventoryData inventoryData = InventoryData.getData(player);
						
						if (inventoryData.instantBowVL > 200){
							plugin.removeBuildFor(player, Check.PVP_INSTANT_BOW);
							playerData.pvpVL = inventoryData.instantBowVL;
							return;
						}
						
						// TODO: Move this to the inventory listener ?
						if (inventoryData.instantEatVL > 200){
							plugin.removeBuildFor(player, Check.INVENTORY_INSTANT_EAT);
							playerData.inventoryVL = inventoryData.instantEatVL;
							return;
						}
					}
				}
			}
		}
	}
	
}
