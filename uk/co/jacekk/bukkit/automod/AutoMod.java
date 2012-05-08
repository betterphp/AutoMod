package uk.co.jacekk.bukkit.automod;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import uk.co.jacekk.bukkit.automod.checks.BlockChecksListener;
import uk.co.jacekk.bukkit.automod.checks.BuildDeniedListener;
import uk.co.jacekk.bukkit.automod.checks.InventoryChecksListener;
import uk.co.jacekk.bukkit.automod.command.SetBuildExecutor;
import uk.co.jacekk.bukkit.automod.command.TrustAllPlayersExecutor;
import uk.co.jacekk.bukkit.automod.command.TrustPlayerExecutor;
import uk.co.jacekk.bukkit.automod.command.TrustedPlayerListExecutor;
import uk.co.jacekk.bukkit.automod.command.BuildExecutor;
import uk.co.jacekk.bukkit.automod.command.BuildDeniedListExecutor;
import uk.co.jacekk.bukkit.automod.data.BanListener;
import uk.co.jacekk.bukkit.automod.data.DataCleanupTask;
import uk.co.jacekk.bukkit.automod.data.PlayerDataListener;
import uk.co.jacekk.bukkit.automod.util.ChatFormatHelper;
import uk.co.jacekk.bukkit.automod.util.StringListStore;

import cc.co.evenprime.bukkit.nocheat.NoCheat;

import de.diddiz.LogBlock.LogBlock;

public class AutoMod extends JavaPlugin {
	
	public Server server;
	public PluginManager pluginManager;
	public BukkitScheduler scheduler;
	
	public AutoModLogger log;
	public ChatFormatHelper chatFormat;
	
	public LogBlock logblock;
	public NoCheat nocheat;
	
	public PlayerDataManager playerDataManager;
	
	public StringListStore trustedPlayers;
	public StringListStore blockedPlayers;
	
	public void onEnable(){
		String pluginDirPath = this.getDataFolder().getAbsolutePath();
		
		(new File(pluginDirPath)).mkdirs();
		
		this.server = this.getServer();
		this.pluginManager = this.server.getPluginManager();
		this.scheduler = this.server.getScheduler();
		
		this.log = new AutoModLogger("Minecraft", this);
		this.chatFormat = new ChatFormatHelper();
		
		if (this.pluginManager.isPluginEnabled("LogBlock")){
			this.logblock = (LogBlock) this.pluginManager.getPlugin("LogBlock");
		}
		
		if (this.pluginManager.isPluginEnabled("NoCheat")){
			this.nocheat = (NoCheat) this.pluginManager.getPlugin("NoCheat");
		}
		
		this.playerDataManager = new PlayerDataManager();
		
		this.blockedPlayers = new StringListStore(new File(pluginDirPath + File.separator + "blocked-players.txt"));
		this.trustedPlayers = new StringListStore(new File(pluginDirPath + File.separator + "trusted-players.txt"));
		
		this.blockedPlayers.load();
		this.trustedPlayers.load();
		
		this.getCommand("build").setExecutor(new BuildExecutor(this));
		this.getCommand("setbuild").setExecutor(new SetBuildExecutor(this));
		this.getCommand("builddeniedlist").setExecutor(new BuildDeniedListExecutor(this));
		this.getCommand("trustedplayerlist").setExecutor(new TrustedPlayerListExecutor(this));
		this.getCommand("trustplayer").setExecutor(new TrustPlayerExecutor(this));
		this.getCommand("trustallplayers").setExecutor(new TrustAllPlayersExecutor(this));
		
		this.pluginManager.registerEvents(new BuildDeniedListener(this), this);
		this.pluginManager.registerEvents(new InventoryChecksListener(this), this);
		this.pluginManager.registerEvents(new PlayerDataListener(this), this);
		this.pluginManager.registerEvents(new BlockChecksListener(this), this);
		
		this.scheduler.scheduleSyncRepeatingTask(this, new DataCleanupTask(this), 36000, 36000); // 30 minutes
		
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
