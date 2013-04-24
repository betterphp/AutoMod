package uk.co.jacekk.bukkit.automod;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.check.BuildDeniedListener;
import uk.co.jacekk.bukkit.automod.command.BuildExecutor;
import uk.co.jacekk.bukkit.automod.command.DataExecutor;
import uk.co.jacekk.bukkit.automod.command.ListExecutor;
import uk.co.jacekk.bukkit.automod.command.VoteExecutor;
import uk.co.jacekk.bukkit.automod.data.DataCleanupTask;
import uk.co.jacekk.bukkit.automod.data.PlayerDataListener;
import uk.co.jacekk.bukkit.automod.data.PlayerDataManager;
import uk.co.jacekk.bukkit.automod.vote.VoteData;
import uk.co.jacekk.bukkit.automod.vote.VoteDataManager;
import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.storage.DataStore;
import uk.co.jacekk.bukkit.baseplugin.storage.ListStore;

public class AutoMod extends BasePlugin {
	
	public PlayerDataManager playerDataManager;
	public VoteDataManager voteDataManager;
	
	public ListStore trustedPlayers;
	public DataStore blockedPlayers;
	
	@Override
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.class, this.log);
		
		this.playerDataManager = new PlayerDataManager();
		this.voteDataManager = new VoteDataManager();
		
		this.trustedPlayers = new ListStore(new File(this.baseDirPath + File.separator + "trusted-players.txt"), false);
		this.blockedPlayers = new DataStore(new File(this.baseDirPath + File.separator + "blocked-players.txt"), false);
		
		this.trustedPlayers.load();
		this.blockedPlayers.load();
		
		this.permissionManager.registerPermissions(Permission.class);
		
		this.pluginManager.registerEvents(new PlayerDataListener(this), this);
		this.pluginManager.registerEvents(new BuildDeniedListener(this), this);
		
		check: for (Check check : Check.values()){
			if (!check.isVirtual() && this.config.getBoolean(check.getEnabledConfigKey())){
				for (String pluginName : check.getRequiredPlugins()){
					if (!this.pluginManager.isPluginEnabled(pluginName)){
						this.log.warn("The check '" + check.name() + "' could not be enabled, " + pluginName + " was not found.");
						continue check;
					}
				}
				
				try{
					this.pluginManager.registerEvents(check.getListenerClass().getConstructor(AutoMod.class).newInstance(this), this);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
		this.scheduler.scheduleSyncRepeatingTask(this, new DataCleanupTask(this), 36000, 36000); // 30 minutes
		
		this.commandManager.registerCommandExecutor(new BuildExecutor(this));
		this.commandManager.registerCommandExecutor(new VoteExecutor(this));
		this.commandManager.registerCommandExecutor(new ListExecutor(this));
		this.commandManager.registerCommandExecutor(new DataExecutor(this));
	}
	
	@Override
	public void onDisable(){
		this.blockedPlayers.save();
		this.trustedPlayers.save();
	}
	
	public boolean shouldCheck(Player player){
		String playerName = player.getName();
		
		if (Permission.CHECK_EXEMPT.has(player)){
			return false;
		}
		
		if (this.trustedPlayers.contains(playerName)){
			return false;
		}
		
		if (this.blockedPlayers.contains(playerName)){
			return false;
		}
		
		if (this.config.getStringList(Config.IGNORE_WORLDS).contains(player.getWorld().getName())){
			return false;
		}
		
		return true;
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
		String playerName = player.getName();
		
		this.blockedPlayers.add(playerName, String.valueOf(checkFailed.getId()));
		this.notifyPlayer(player, checkFailed.getDescription());
		
		for (String command : this.config.getStringList(Config.BUILD_REMOVED_COMMANDS)){
			command = command.replaceAll("%player_name%", playerName);
			command = command.replaceAll("%check_failed%", checkFailed.name());
			command = command.replaceAll("%check_failed_description%", checkFailed.getDescription());
			
			this.server.dispatchCommand(this.server.getConsoleSender(), command);
		}
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
				this.playerDataManager.unregisterPlayer(playerName);
				
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
