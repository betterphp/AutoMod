package uk.co.jacekk.bukkit.automod.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class PlayerVoteTracker {
	
	private AutoMod plugin;
	
	private HashMap<String, Integer> yesVotes;
	private HashMap<String, Integer> noVotes;
	
	private HashMap<String, ArrayList<String>> voters;
	
	public PlayerVoteTracker(AutoMod plugin){
		this.plugin = plugin;
		
		this.yesVotes = new HashMap<String, Integer>();
		this.noVotes = new HashMap<String, Integer>();
		
		this.voters = new HashMap<String, ArrayList<String>>();
	}
	
	public boolean voteExistsFor(String playerName){
		return this.voters.containsKey(playerName);
	}
	
	public boolean voteExistsFor(Player player){
		return this.voteExistsFor(player.getName());
	}
	
	public void removeVoter(String voterName){
		for (String player : this.voters.keySet()){
			this.removeVoterFor(player, voterName);
			
			if (this.getOutstandingVotersFor(player).size() == 0 && this.yesVotes.get(player) == 0 && this.noVotes.get(player) == 0){
				this.removePlayer(player);
			}
		}
	}
	
	public void removeVoter(Player player){
		this.removeVoter(player.getName());
	}
	
	public void addPlayer(String playerName){
		this.yesVotes.put(playerName, 0);
		this.noVotes.put(playerName, 0);
		
		this.voters.put(playerName, new ArrayList<String>());
		
		for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()){
			if (onlinePlayer.hasPermission("automod.vote.build")){
				this.voters.get(playerName).add(onlinePlayer.getName());
			}
		}
	}
	
	public void addPlayer(Player player){
		this.addPlayer(player.getName());
	}
	
	public void resetVotesFor(String playerName){
		this.addPlayer(playerName);
	}
	
	public void resetVotesFor(Player player){
		this.addPlayer(player);
	}
	
	public void removePlayer(String playerName){
		this.yesVotes.remove(playerName);
		this.noVotes.remove(playerName);
		
		this.voters.remove(playerName);
	}
	
	public void removeVoterFor(String playerName, String voterName){
		ArrayList<String> voters = this.voters.get(playerName);
		
		voters.remove(voterName);
		
		this.voters.put(playerName, voters);
	}
	
	public void removeVoterFor(Player player, Player voter){
		this.removeVoterFor(player.getName(), voter.getName());
	}
	
	public void removePlayer(Player player){
		this.removePlayer(player.getName());
	}
	
	public void addYesVoteFor(String playerName){
		this.yesVotes.put(playerName, this.yesVotes.get(playerName) + 1);
	}
	
	public void addYesVoteFor(Player player){
		this.addYesVoteFor(player.getName());
	}
	
	public void addNoVoteFor(String playerName){
		this.noVotes.put(playerName, this.noVotes.get(playerName) + 1);
	}
	
	public void addnoVoteFor(Player player){
		this.addNoVoteFor(player.getName());
	}
	
	public int getYesVotesFor(String playerName){
		return this.yesVotes.get(playerName);
	}
	
	public int getYesVotesFor(Player player){
		return this.getYesVotesFor(player.getName());
	}
	
	public int getNoVotesFor(String playerName){
		return this.noVotes.get(playerName);
	}
	
	public int getNoVotesFor(Player player){
		return this.getNoVotesFor(player.getName());
	}
	
	public List<String> getOutstandingVotersFor(String playerName){
		return this.voters.get(playerName);
	}
	
	public List<String> getOutstandingVotersFor(Player player){
		return this.getOutstandingVotersFor(player.getName());
	}
	
}
