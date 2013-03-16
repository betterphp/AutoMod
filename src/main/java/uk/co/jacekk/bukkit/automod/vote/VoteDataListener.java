package uk.co.jacekk.bukkit.automod.vote;

import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public class VoteDataListener extends BaseListener<AutoMod> {
	
	public VoteDataListener(AutoMod plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (Permission.ADMIN_VOTE.has(player) && !plugin.blockedPlayers.contains(playerName)){
			for (Entry<String, VoteData> entry : plugin.voteDataManager.getAll()){
				VoteData voteData = entry.getValue();
				
				if (!voteData.voted.contains(playerName)){
					--voteData.totalNeeded;
					plugin.processVoteData(entry.getKey(), voteData);
				}
			}
		}
	}
	
}
