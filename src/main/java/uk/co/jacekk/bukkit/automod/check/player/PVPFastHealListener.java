package uk.co.jacekk.bukkit.automod.check.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import fr.neatmonster.nocheatplus.checks.fight.FightData;

public class PVPFastHealListener extends BaseListener<AutoMod> {
	
	public PVPFastHealListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDamage(EntityRegainHealthEvent event){
		Entity entity = event.getEntity();
		
		if (!(entity instanceof Player)){
			return;
		}
		
		Player player = (Player) entity;
		
		if (plugin.shouldCheck(player)){
			FightData fightData = FightData.getData(player);
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			if (fightData.fastHealVL > plugin.config.getInt(Config.CHECK_PVP_FAST_HEAL_LIMIT)){
				plugin.removeBuildFor(player, Check.PVP_CRITICAL);
				playerData.pvpVL = fightData.fastHealVL;
			}
		}
	}
	
}
