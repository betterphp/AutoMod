package uk.co.jacekk.bukkit.automod;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import uk.co.jacekk.bukkit.automod.data.PlayerData;

public class PlayerDataManager {
	
	private HashMap<String, PlayerData> playerData;
	
	public PlayerDataManager(){
		this.playerData = new HashMap<String, PlayerData>();
	}
	
	public void registerPlayer(String playerName){
		if (this.playerData.containsKey(playerName) == false){
			this.playerData.put(playerName, new PlayerData());
		}
	}
	
	public void unregisterPlayer(String playerName){
		if (this.playerData.containsKey(playerName)){
			this.playerData.remove(playerName);
		}
	}
	
	public boolean gotDataFor(String playerName){
		return this.playerData.containsKey(playerName);
	}
	
	public PlayerData getPlayerData(String playerName){
		return this.playerData.get(playerName);
	}
	
	@SuppressWarnings("unchecked")
	public Set<Entry<String, PlayerData>> getAll(){
		return ((HashMap<String, PlayerData>) this.playerData.clone()).entrySet();
	}
	
}
