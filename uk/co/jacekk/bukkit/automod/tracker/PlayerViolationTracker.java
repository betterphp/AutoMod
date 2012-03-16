package uk.co.jacekk.bukkit.automod.tracker;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class PlayerViolationTracker {
	
	private ArrayList<String> trackedPlayers;
	
	private HashMap<String, Integer> blockEvents;
	private HashMap<String, Integer> blockBreakViolations;
	private HashMap<String, Integer> logBlockViolations;
	
	public PlayerViolationTracker(AutoMod plugin){
		this.trackedPlayers = new ArrayList<String>();
		
		this.blockEvents = new HashMap<String, Integer>();
		this.blockBreakViolations = new HashMap<String, Integer>();
		this.logBlockViolations = new HashMap<String, Integer>();
	}
	
	public void addNewPlayer(String playerName){
		if (this.trackedPlayers.contains(playerName)){
			this.trackedPlayers.add(playerName);
		}
		
		this.blockEvents.put(playerName, 0);
		this.blockBreakViolations.put(playerName, 0);
		this.logBlockViolations.put(playerName, 0);
	}
	
	public void removePlayer(String playerName){
		this.blockEvents.remove(playerName);
		this.blockBreakViolations.remove(playerName);
		this.logBlockViolations.remove(playerName);
	}
	
	public void resetPlayer(String playerName){
		this.addNewPlayer(playerName);
	}
	
	public void addBlockEvent(String playerName){
		if (this.trackedPlayers.contains(playerName)){
			this.addNewPlayer(playerName);
		}
		
		this.blockEvents.put(playerName, this.blockEvents.get(playerName) + 1);
	}
	
	public int getBlockEvents(String playerName){
		return this.blockEvents.get(playerName);
	}
	
	public void addBlockBreakViolation(String playerName){
		if (this.trackedPlayers.contains(playerName)){
			this.addNewPlayer(playerName);
		}
		
		this.blockBreakViolations.put(playerName, this.blockBreakViolations.get(playerName) + 1);
	}
	
	public int getBlockBreakViolations(String playerName){
		return this.blockBreakViolations.get(playerName);
	}
	
	public void addLogBlockViolation(String playerName){
		if (this.trackedPlayers.contains(playerName)){
			this.addNewPlayer(playerName);
		}
		
		this.blockBreakViolations.put(playerName, this.blockBreakViolations.get(playerName) + 1);
	}
	
	public int getLogBlockViolation(String playerName){
		return this.blockBreakViolations.get(playerName);
	}
	
	public int getTotalViolations(String playerName){
		int total = 0;
		
		total += this.getBlockBreakViolations(playerName);
		total += this.getBlockBreakViolations(playerName);
		
		return total;
	}
	
}
