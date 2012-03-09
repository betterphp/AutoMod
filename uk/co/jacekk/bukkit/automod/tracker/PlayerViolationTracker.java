package uk.co.jacekk.bukkit.automod.tracker;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

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
	
	public void addNewPlayer(Player player){
		this.addNewPlayer(player.getName());
	}
	
	public void resetPlayer(String playerName){
		this.addNewPlayer(playerName);
	}
	
	public void resetPlayer(Player player){
		this.resetPlayer(player.getName());
	}
	
	public void addBlockEvent(String playerName){
		if (this.trackedPlayers.contains(playerName)){
			this.addNewPlayer(playerName);
		}
		
		this.blockEvents.put(playerName, this.blockEvents.get(playerName) + 1);
	}
	
	public void addBlockEvent(Player player){
		this.addBlockEvent(player.getName());
	}
	
	public int getBlockEvents(String playerName){
		return this.blockEvents.get(playerName);
	}
	
	public int getBlockEvents(Player player){
		return this.getBlockEvents(player.getName());
	}
	
	public void addBlockBreakViolation(String playerName){
		if (this.trackedPlayers.contains(playerName)){
			this.addNewPlayer(playerName);
		}
		
		this.blockBreakViolations.put(playerName, this.blockBreakViolations.get(playerName) + 1);
	}
	
	public void addBlockBreakViolation(Player player){
		this.addBlockBreakViolation(player.getName());
	}
	
	public int getBlockBreakViolations(String playerName){
		return this.blockBreakViolations.get(playerName);
	}
	
	public int getBlockBreakViolations(Player player){
		return this.getBlockBreakViolations(player.getName());
	}
	
	public void addLogBlockViolation(String playerName){
		if (this.trackedPlayers.contains(playerName)){
			this.addNewPlayer(playerName);
		}
		
		this.blockBreakViolations.put(playerName, this.blockBreakViolations.get(playerName) + 1);
	}
	
	public void addLogBlockViolation(Player player){
		this.addLogBlockViolation(player.getName());
	}
	
	public int getLogBlockViolation(String playerName){
		return this.blockBreakViolations.get(playerName);
	}
	
	public int getLogBlockViolation(Player player){
		return this.getLogBlockViolation(player.getName());
	}
	
	public int getTotalViolations(String playerName){
		int total = 0;
		
		total += this.getBlockBreakViolations(playerName);
		total += this.getBlockBreakViolations(playerName);
		
		return total;
	}
	
	public int getTotalViolations(Player player){
		return this.getTotalViolations(player.getName());
	}
	
}
