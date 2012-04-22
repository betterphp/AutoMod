package uk.co.jacekk.bukkit.automod.data;

import java.util.Map.Entry;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class DataCleanupTask implements Runnable {
	
	private AutoMod plugin;
	
	public DataCleanupTask(AutoMod plugin){
		this.plugin = plugin;
	}
	
	public void run(){
		String playerName;
		PlayerData playerData;
		
		for (Entry<String, PlayerData> entry : plugin.playerDataManager.getAll()){
			playerName = entry.getKey();
			playerData = entry.getValue();
			
			if (playerData.lastQuitTime > playerData.lastJoinTime && System.currentTimeMillis() - playerData.lastQuitTime > 3600000){
				plugin.playerDataManager.unregisterPlayer(playerName);
			}
		}
	}
	
}
