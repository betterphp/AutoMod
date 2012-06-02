package uk.co.jacekk.bukkit.automod.vote;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public class VoteDataManager {
	
	private HashMap<String, VoteData> voteData;
	
	public VoteDataManager(){
		this.voteData = new HashMap<String, VoteData>();
	}
	
	public void registerPlayer(String playerName){
		this.voteData.put(playerName.toLowerCase(), new VoteData());
	}
	
	public void unregisterPlayer(String playerName){
		this.voteData.remove(playerName.toLowerCase());
	}
	
	public boolean gotDataFor(String playerName){
		return this.voteData.containsKey(playerName.toLowerCase());
	}
	
	public VoteData getPlayerVoteData(String playerName){
		return this.voteData.get(playerName.toLowerCase());
	}
	
	@SuppressWarnings("unchecked")
	public Set<Entry<String, VoteData>> getAll(){
		return ((HashMap<String, VoteData>) this.voteData.clone()).entrySet();
	}
	
}
