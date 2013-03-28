package uk.co.jacekk.bukkit.automod.data;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class PlayerDataManager {
	
	private HashMap<String, PlayerData> playerData;
	
	public PlayerDataManager(){
		this.playerData = new HashMap<String, PlayerData>();
	}
	
	public void unregisterPlayer(String playerName){
		this.playerData.remove(playerName.toLowerCase());
	}
	
	public PlayerData getPlayerData(String playerName){
		playerName = playerName.toLowerCase();
		
		if (!this.playerData.containsKey(playerName)){
			this.playerData.put(playerName, new PlayerData());
		}
		
		return this.playerData.get(playerName);
	}
	
	@SuppressWarnings("unchecked")
	public Set<Entry<String, PlayerData>> getAll(){
		return ((HashMap<String, PlayerData>) this.playerData.clone()).entrySet();
	}
	
}
