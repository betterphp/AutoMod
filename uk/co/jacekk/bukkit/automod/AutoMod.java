package uk.co.jacekk.bukkit.automod;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.checks.BlockChecksListener;
import uk.co.jacekk.bukkit.automod.checks.BuildDeniedListener;
import uk.co.jacekk.bukkit.automod.checks.InventoryChecksListener;
import uk.co.jacekk.bukkit.automod.command.ListExecutor;
import uk.co.jacekk.bukkit.automod.command.SetBuildExecutor;
import uk.co.jacekk.bukkit.automod.command.TrustAllPlayersExecutor;
import uk.co.jacekk.bukkit.automod.command.BuildExecutor;
import uk.co.jacekk.bukkit.automod.data.BanListener;
import uk.co.jacekk.bukkit.automod.data.DataCleanupTask;
import uk.co.jacekk.bukkit.automod.data.PlayerDataListener;
import uk.co.jacekk.bukkit.automod.util.StringListStore;
import uk.co.jacekk.bukkit.baseplugin.BasePlugin;

import cc.co.evenprime.bukkit.nocheat.NoCheat;

import de.diddiz.LogBlock.LogBlock;

public class AutoMod extends BasePlugin {
	
	public LogBlock logblock;
	public NoCheat nocheat;
	
	public PlayerDataManager playerDataManager;
	
	public StringListStore trustedPlayers;
	public StringListStore blockedPlayers;
	
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
		
		this.playerDataManager = new PlayerDataManager();
		
		this.blockedPlayers = new StringListStore(new File(this.baseDirPath + File.separator + "blocked-players.txt"));
		this.trustedPlayers = new StringListStore(new File(this.baseDirPath + File.separator + "trusted-players.txt"));
		
		this.blockedPlayers.load();
		this.trustedPlayers.load();
		
		if (this.pluginManager.isPluginEnabled("MineBans")){
			this.pluginManager.registerEvents(new BanListener(this), this);
		}else{
			this.log.warn("MineBans is not available, player will not be removed from the block list when banned.");
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
		this.getCommand("setbuild").setExecutor(new SetBuildExecutor(this));
		this.getCommand("list").setExecutor(new ListExecutor(this));
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
		
		for (Player onlinePlayer : this.getServer().getOnlinePlayers()){
			if (onlinePlayer.hasPermission("automod.vote.build")){
				onlinePlayer.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.AQUA + " " + playerName + " just lost their build permissions");
				onlinePlayer.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.AQUA + " Reason: " + reason);
			}
		}
	}
	
	public void removeBuildFor(Player player, Check checkFailed){
		this.blockedPlayers.add(player.getName());
		this.notifyPlayer(player, checkFailed.getDescription());
	}
	
}
