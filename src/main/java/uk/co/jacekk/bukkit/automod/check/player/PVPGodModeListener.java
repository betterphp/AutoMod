package uk.co.jacekk.bukkit.automod.check.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.neatmonster.nocheatplus.checks.fight.FightData;
import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Config;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public class PVPGodModeListener extends BaseListener<AutoMod> {
	
	public PVPGodModeListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageByEntityEvent event){
		Entity damager = event.getDamager();
		
		if (!(damager instanceof Player)){
			return;
		}
		
		Player player = (Player) damager;
		
		if (plugin.shouldCheck(player)){
			FightData fightData = FightData.getData(player);
			PlayerData playerData = plugin.playerDataManager.getPlayerData(player.getName());
			
			if (fightData.godModeVL > plugin.config.getInt(Config.CHECK_PVP_GODMODE_LIMIT)){
				plugin.removeBuildFor(player, Check.PVP_GODMODE);
				playerData.pvpVL = fightData.godModeVL;
			}
		}
	}
	
}
