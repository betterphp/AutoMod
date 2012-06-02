package uk.co.jacekk.bukkit.automod.vote;

import java.util.ArrayList;

public class VoteData {
	
	// Total number of votes received
	public int totalYesVotes;
	public int totalNoVotes;
	
	// A list of players that have voted for this player
	public ArrayList<String> voted;
	
	public VoteData(){
		this.resetAll();
	}
	
	public void resetAll(){
		this.totalYesVotes = 0;
		this.totalNoVotes = 0;
		
		this.voted = new ArrayList<String>();
	}
	
}
