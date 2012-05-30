package uk.co.jacekk.bukkit.automod.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class ListExecutor extends BaseCommandExecutor<AutoMod> {
	
	public ListExecutor(AutoMod plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		
		return true;
	}
	
}
