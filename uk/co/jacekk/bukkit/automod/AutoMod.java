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
	
	public PlayerListStore playersPassedChecks;
	public PlayerListStore buildDeniedList;
	
	public void onEnable(){
		File pluginDir = new File(this.getDataFolder().getAbsolutePath());
		
		if (pluginDir.exists() == false){
			pluginDir.mkdirs();
		}
		
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
		
		this.playersPassedChecks = new PlayerListStore(this, "passedChecksPlayerList.bin");
		this.buildDeniedList = new PlayerListStore(this, "buildDeniedPlayerList.bin");
		
		this.playersPassedChecks.loadPlayersFromFile();
		this.buildDeniedList.loadPlayersFromFile();
		
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
		this.playersPassedChecks.writePlayersToFile();
		this.buildDeniedList.writePlayersToFile();
		
		this.log.info("Disabled");
		
		this.logblock = null;
		this.nocheat = null;
		
		this.pluginManager = null;
		
		this.log = null;
		this.chatFormat = null;
		
		this.voteTracker = null;
		this.violationTracker = null;
		
		this.playersPassedChecks = null;
		this.buildDeniedList = null;
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
		this.buildDeniedList.addPlayer(player);
		this.notifyPlayer(player, reason);
	}
	
}
