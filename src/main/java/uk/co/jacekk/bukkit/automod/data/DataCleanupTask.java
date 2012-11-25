package uk.co.jacekk.bukkit.automod.data;

import java.util.Map.Entry;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.baseplugin.v5.scheduler.BaseTask;

public class DataCleanupTask extends BaseTask<AutoMod> {
	
	public DataCleanupTask(AutoMod plugin){
		super(plugin);
	}
	
	public void run(){
		String playerName;
		PlayerData playerData;
		
		for (Entry<String, PlayerData> entry : plugin.playerDataManager.getAll()){
			playerName = entry.getKey();
			
			if (!plugin.blockedPlayers.contains(playerName)){
				playerData = entry.getValue();
				
				if (playerData.lastQuitTime > playerData.lastJoinTime && System.currentTimeMillis() - playerData.lastQuitTime > 3600000){
					plugin.playerDataManager.unregisterPlayer(playerName);
				}
			}
		}
	}
	
}
