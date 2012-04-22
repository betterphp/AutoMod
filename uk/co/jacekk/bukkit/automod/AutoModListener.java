package uk.co.jacekk.bukkit.automod;

import org.bukkit.event.Listener;

public abstract class AutoModListener implements Listener {
	
	protected AutoMod plugin;
	
	public AutoModListener(AutoMod plugin){
		this.plugin = plugin;
	}
	
}
