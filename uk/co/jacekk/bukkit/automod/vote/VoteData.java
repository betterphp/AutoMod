package uk.co.jacekk.bukkit.automod.vote;

import java.util.ArrayList;

public class VoteData {
	
	// Total number of votes received
	public int totalYesVotes;
	public int totalNoVotes;
	public int totalVotes;
	
	// Total number of votes needed
	public int totalNeeded;
	public double percentageNeeded;
	
	// A list of players that have voted for this player
	public ArrayList<String> voted;
	
	public VoteData(){
		this.resetAll();
	}
	
	public void resetAll(){
		this.totalYesVotes = 0;
		this.totalNoVotes = 0;
		this.totalVotes = 0;
		
		this.totalNeeded = 0;
		this.percentageNeeded = 1.0D;
		
		this.voted = new ArrayList<String>();
	}
	
}
