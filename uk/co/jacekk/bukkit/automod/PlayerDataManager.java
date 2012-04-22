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
		this.playerData.put(playerName.toLowerCase(), new PlayerData());
	}
	
	public void unregisterPlayer(String playerName){
		this.playerData.remove(playerName.toLowerCase());
	}
	
	public boolean gotDataFor(String playerName){
		return this.playerData.containsKey(playerName.toLowerCase());
	}
	
	public PlayerData getPlayerData(String playerName){
		return this.playerData.get(playerName.toLowerCase());
	}
	
	@SuppressWarnings("unchecked")
	public Set<Entry<String, PlayerData>> getAll(){
		return ((HashMap<String, PlayerData>) this.playerData.clone()).entrySet();
	}
	
}
