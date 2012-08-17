package uk.co.jacekk.bukkit.automod;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.checks.BlockChecksListener;
import uk.co.jacekk.bukkit.automod.checks.BuildDeniedListener;
import uk.co.jacekk.bukkit.automod.checks.InventoryChecksListener;
import uk.co.jacekk.bukkit.automod.command.DataExecutor;
import uk.co.jacekk.bukkit.automod.command.ListExecutor;
import uk.co.jacekk.bukkit.automod.command.TrustAllPlayersExecutor;
import uk.co.jacekk.bukkit.automod.command.BuildExecutor;
import uk.co.jacekk.bukkit.automod.command.VoteExecutor;
import uk.co.jacekk.bukkit.automod.data.BanListener;
import uk.co.jacekk.bukkit.automod.data.DataCleanupTask;
import uk.co.jacekk.bukkit.automod.data.PlayerDataListener;
import uk.co.jacekk.bukkit.automod.data.PlayerDataManager;
import uk.co.jacekk.bukkit.automod.vote.VoteData;
import uk.co.jacekk.bukkit.automod.vote.VoteDataManager;
import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.storage.DataStore;
import uk.co.jacekk.bukkit.baseplugin.storage.ListStore;

import cc.co.evenprime.bukkit.nocheat.NoCheat;

import de.diddiz.LogBlock.LogBlock;

public class AutoMod extends BasePlugin {
	
	public LogBlock logblock;
	public NoCheat nocheat;
	
	public PlayerDataManager playerDataManager;
	public VoteDataManager voteDataManager;
	
	public ListStore trustedPlayers;
	public DataStore blockedPlayers;
	
	public void onEnable(){
		super.onEnable(true);
		
		if (this.pluginManager.isPluginEnabled("LogBlock")){
			this.logblock = (LogBlock) this.pluginManager.getPlugin("LogBlock");
		}else{
			this.log.warn("LogBlock is not available, some checks will be skipped.");
		}
		
		if (this.pluginManager.isPluginEnabled("NoCheat")){
			this.nocheat = (NoCheat) this.pluginManager.getPlugin("NoCheat");
		}else{
			this.log.warn("NoCheat is not available, some checks will be skipped.");
		}
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.values(), this.log);
		
		this.playerDataManager = new PlayerDataManager();
		this.voteDataManager = new VoteDataManager();
		
		this.trustedPlayers = new ListStore(new File(this.baseDirPath + File.separator + "trusted-players.txt"), false);
		this.blockedPlayers = new DataStore(new File(this.baseDirPath + File.separator + "blocked-players.txt"), false);
		
		this.trustedPlayers.load();
		this.blockedPlayers.load();
		
		if (this.pluginManager.isPluginEnabled("MineBans")){
			this.pluginManager.registerEvents(new BanListener(this), this);
		}else{
			this.log.warn("MineBans is not available, players will not be removed from the block list when banned.");
		}
		
		this.pluginManager.registerEvents(new PlayerDataListener(this), this);
		this.pluginManager.registerEvents(new BuildDeniedListener(this), this);
		this.pluginManager.registerEvents(new InventoryChecksListener(this), this);
		this.pluginManager.registerEvents(new BlockChecksListener(this), this);
		
		this.scheduler.scheduleSyncRepeatingTask(this, new DataCleanupTask(this), 36000, 36000); // 30 minutes
		
		for (Permission permission : Permission.values()){
			this.pluginManager.addPermission(new org.bukkit.permissions.Permission(permission.getNode(), permission.getDescription(), permission.getDefault()));
		}
		
		this.getCommand("build").setExecutor(new BuildExecutor(this));
		this.getCommand("vote").setExecutor(new VoteExecutor(this));
		this.getCommand("list").setExecutor(new ListExecutor(this));
		this.getCommand("data").setExecutor(new DataExecutor(this));
		this.getCommand("trustallplayers").setExecutor(new TrustAllPlayersExecutor(this));
	}
	
	public void onDisable(){
		this.blockedPlayers.save();
		this.trustedPlayers.save();
	}
	
	public void notifyPlayer(Player player, String reason){
		String playerName = player.getName();
		
		player.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.RED + " Your build permissions have just been removed");
		player.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.RED + " Reason: " + reason);
		
		this.log.info(playerName + " just lost their build permissions");
		this.log.info("Reason: " + reason);
		
		for (Player onlinePlayer : Permission.ADMIN_VOTE.getPlayersWith()){
			onlinePlayer.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.AQUA + " " + playerName + " just lost their build permissions");
			onlinePlayer.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.AQUA + " Reason: " + reason);
		}
	}
	
	public void removeBuildFor(Player player, Check checkFailed){
		this.blockedPlayers.add(player.getName(), String.valueOf(checkFailed.getId()));
		this.notifyPlayer(player, checkFailed.getDescription());
	}
	
	public void processVoteData(String playerName, VoteData voteData){
		Player player = this.server.getPlayer(playerName);
		
		if (voteData.totalNeeded == 0){
			this.voteDataManager.unregisterPlayer(playerName);
			
			if (player != null){
				player.sendMessage(this.formatMessage(ChatColor.RED + "The only player that could vote just left, your request has been reset"));
			}
		}else if (voteData.totalVotes >= voteData.totalNeeded){
			if (voteData.totalYesVotes / voteData.totalVotes >= voteData.percentageNeeded){
				this.blockedPlayers.remove(playerName);
				this.playerDataManager.resetPlayer(playerName);
				
				if (player != null){
					player.sendMessage(this.formatMessage(ChatColor.GREEN + "Your build permissions have been restored"));
				}
			}else{
				if (player != null){
					player.sendMessage(this.formatMessage(ChatColor.RED + "Your request for build permissions has been denied"));
				}
			}
			
			this.voteDataManager.unregisterPlayer(playerName);
		}
	}
	
}
