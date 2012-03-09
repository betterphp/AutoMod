package uk.co.jacekk.bukkit.automod;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;


public class AutoModLogger {
	
	private AutoMod plugin;
	private Logger logger;
	
	public AutoModLogger(String logger, AutoMod plugin){
		this.plugin = plugin;
		this.logger = Logger.getLogger(logger);
	}
	
	private String buildString(String msg){
		PluginDescriptionFile pdFile = plugin.getDescription();
		
		return pdFile.getName() + " " + pdFile.getVersion() + ": " + msg;
	}
	
	public void info(String msg){
		this.logger.info(this.buildString(msg));
	}
	
	public void warn(String msg){
		this.logger.warning(this.buildString(msg));
	}
	
	public void severe(String msg){
		this.logger.severe(this.buildString(msg));
	}
	
}
