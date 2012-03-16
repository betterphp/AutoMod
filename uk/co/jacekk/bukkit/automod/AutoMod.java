package uk.co.jacekk.bukkit.automod;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.jacekk.bukkit.automod.command.SetBuildExecutor;
import uk.co.jacekk.bukkit.automod.command.TrustAllPlayersExecutor;
import uk.co.jacekk.bukkit.automod.command.TrustPlayerExecutor;
import uk.co.jacekk.bukkit.automod.command.TrustedPlayerListExecutor;
import uk.co.jacekk.bukkit.automod.command.VoteExecutor;
import uk.co.jacekk.bukkit.automod.command.BuildExecutor;
import uk.co.jacekk.bukkit.automod.command.BuildDeniedListExecutor;
import uk.co.jacekk.bukkit.automod.listener.BanListener;
import uk.co.jacekk.bukkit.automod.listener.BlockViolationListener;
import uk.co.jacekk.bukkit.automod.listener.BuildDeniedListener;
import uk.co.jacekk.bukkit.automod.listener.ConnectionListener;
import uk.co.jacekk.bukkit.automod.listener.InventoryViolationListener;
import uk.co.jacekk.bukkit.automod.tracker.PlayerViolationTracker;
import uk.co.jacekk.bukkit.automod.tracker.PlayerVoteTracker;
import uk.co.jacekk.bukkit.automod.util.ChatFormatHelper;
import uk.co.jacekk.bukkit.automod.util.PlayerListStore;
import uk.co.jacekk.bukkit.automod.util.StringListStore;

import cc.co.evenprime.bukkit.nocheat.NoCheat;

import de.diddiz.LogBlock.LogBlock;

public class AutoMod extends JavaPlugin {
	
	private PluginManager pluginManager;
	
	public AutoModLogger log;
	public ChatFormatHelper chatFormat;
	
	public LogBlock logblock;
	public NoCheat nocheat;
	
	public PlayerVoteTracker voteTracker;
	public PlayerViolationTracker violationTracker;
	
	public StringListStore trustedPlayers;
	public StringListStore blockedPlayers;
	
	public void onEnable(){
		String pluginDirPath = this.getDataFolder().getAbsolutePath();
		
		(new File(pluginDirPath)).mkdirs();
		
		this.pluginManager = this.getServer().getPluginManager();
		
		this.log = new AutoModLogger("Minecraft", this);
		this.chatFormat = new ChatFormatHelper();
		
		Plugin plugin;
		
		plugin = this.getServer().getPluginManager().getPlugin("LogBlock");
		this.logblock = (plugin != null) ? (LogBlock) plugin : null;
		
		plugin = this.getServer().getPluginManager().getPlugin("NoCheat");
		this.nocheat = (plugin != null) ? (NoCheat) plugin : null;
		
		this.voteTracker = new PlayerVoteTracker(this);
		this.violationTracker = new PlayerViolationTracker(this);
		
		this.blockedPlayers = new StringListStore(new File(pluginDirPath + File.separator + "blocked-players.txt"));
		this.trustedPlayers = new StringListStore(new File(pluginDirPath + File.separator + "trusted-players.txt"));
		
		this.blockedPlayers.load();
		this.trustedPlayers.load();
		
		// TODO: Remove this with the next major version.
		File oldTrustedFile = new File(pluginDirPath + File.separator + "passedChecksPlayerList.bin");
		File oldBlockedFile = new File(pluginDirPath + File.separator + "buildDeniedPlayerList.bin");
		
		if (oldTrustedFile.exists() || oldBlockedFile.exists()){
			this.log.info("Converting list file format...");
			
			PlayerListStore playersPassedChecks = new PlayerListStore(this, "passedChecksPlayerList.bin");
			PlayerListStore buildDeniedList = new PlayerListStore(this, "buildDeniedPlayerList.bin");
			
			playersPassedChecks.loadPlayersFromFile();
			buildDeniedList.loadPlayersFromFile();
			
			for (String playerName : buildDeniedList.getPlayerNames()){
				this.blockedPlayers.add(playerName);
			}
			
			for (String playerName : playersPassedChecks.getPlayerNames()){
				this.trustedPlayers.add(playerName);
			}
			
			this.blockedPlayers.save();
			this.trustedPlayers.save();
			
			this.log.info("Done, " + (this.blockedPlayers.getSize() + this.trustedPlayers.getSize()) + " entries written.");
			
			oldTrustedFile.delete();
			oldBlockedFile.delete();
		}
		
		this.getCommand("vote").setExecutor(new VoteExecutor(this));
		this.getCommand("build").setExecutor(new BuildExecutor(this));
		this.getCommand("setbuild").setExecutor(new SetBuildExecutor(this));
		this.getCommand("builddeniedlist").setExecutor(new BuildDeniedListExecutor(this));
		this.getCommand("trustedplayerlist").setExecutor(new TrustedPlayerListExecutor(this));
		this.getCommand("trustplayer").setExecutor(new TrustPlayerExecutor(this));
		this.getCommand("trustallplayers").setExecutor(new TrustAllPlayersExecutor(this));
		
		this.pluginManager.registerEvents(new BuildDeniedListener(this), this);
		this.pluginManager.registerEvents(new ConnectionListener(this), this);
		this.pluginManager.registerEvents(new InventoryViolationListener(this), this);
		this.pluginManager.registerEvents(new BlockViolationListener(this), this);
		
		if (this.pluginManager.isPluginEnabled("MineBans")){
			this.log.info("MineBans has been found, using the ban event.");
			this.pluginManager.registerEvents(new BanListener(this), this);
		}
		
		if (this.logblock == null){
			this.log.info("LogBlock is not available, some checks will be skipped.");
		}
		
		if (this.nocheat == null){
			this.log.info("NoCheat is not available, some checks will be skipped.");
		}
		
		this.log.info("Enabled");
	}
	
	public void onDisable(){
		this.blockedPlayers.save();
		this.trustedPlayers.save();
		
		this.log.info("Disabled");
	}
	
	public void messagePlayer(CommandSender sender, String message){
		sender.sendMessage(ChatColor.BLUE + "[AutoMod] " + message);
	}
	
	public void notifyPlayer(Player player, String reason){
		String playerName = player.getName();
		
		player.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.RED + " Your build permissions have just been removed.");
		player.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.RED + " Reason: " + reason + ".");
		player.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.RED + " Use the /build command to ask for them back.");
		
		this.log.info(playerName + " just lost their build permissions.");
		this.log.info("Reason: " + reason + ".");
		
		for (Player onlinePlayer : this.getServer().getOnlinePlayers()){
			if (onlinePlayer.hasPermission("automod.vote.build")){
				onlinePlayer.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.AQUA + " " + playerName + " just lost their build permissions.");
				onlinePlayer.sendMessage(ChatColor.BLUE + "[AutoMod]" + ChatColor.AQUA + " Reason: " + reason + ".");
			}
		}
	}
	
	public void removeBuildFor(Player player, String reason){
		this.blockedPlayers.add(player.getName());
		this.notifyPlayer(player, reason);
	}
	
}
