package uk.co.jacekk.bukkit.automod.command;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.automod.AutoMod;
import uk.co.jacekk.bukkit.automod.Check;
import uk.co.jacekk.bukkit.automod.Permission;
import uk.co.jacekk.bukkit.automod.data.PlayerData;
import uk.co.jacekk.bukkit.baseplugin.v8.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v8.command.CommandHandler;

public class DataExecutor extends BaseCommandExecutor<AutoMod> {
	
	public DataExecutor(AutoMod plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"data"}, description = "Prints the data that caused a player to be placed on the block list", usage = "[player_name]")
	public void data(CommandSender sender, String label, String[] args){
		if (!Permission.ADMIN_DATA.has(sender)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to use this command"));
			return;
		}
		
		if (args.length != 1){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Usage: /" + label + " <player_name>"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " wide_load"));
			return;
		}
		
		String playerName = args[0];
		
		if (!plugin.blockedPlayers.contains(playerName)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + playerName + " was not found on the block list"));
			return;
		}
		
		Check checkFailed = Check.fromId(Integer.parseInt(plugin.blockedPlayers.getData(playerName)));
		PlayerData data = plugin.playerDataManager.getPlayerData(playerName);
		
		sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + "Reason: " + checkFailed.getDescription()));
		
		if (data != null && checkFailed != Check.CUSTOM_ADDITION){
			switch (checkFailed){
				case BLOCK_BREAK_OWNED_BLOCKS:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + String.valueOf(data.ownedBlocksBroken) + " of another player's blocks were broken"));
					
					for (Entry<Material, Integer> entry : data.ownedTypesBroken.entrySet()){
						sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + entry.getKey().name() + ": " + entry.getValue()));
					}
				break;
				
				case BLOCK_BREAK_UNNATURAL_BLOCKS:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + String.valueOf(data.unnaturalBlocksBroken) + " unnatural blocks were broken"));
					
					for (Entry<Material, Integer> entry : data.unnaturalTypesBroken.entrySet()){
						sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + entry.getKey().name() + ": " + entry.getValue()));
					}
				break;
				
				case BLOCK_BREAK_UNBREAKABLE:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + String.valueOf(data.unbreakableBlocksBroken) + " unbreakable blocks were broken"));
					
					for (Entry<Material, Integer> entry : data.unnaturalTypesBroken.entrySet()){
						sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + entry.getKey().name() + ": " + entry.getValue()));
					}
				break;
				
				case BLOCK_BREAK_DIRECTION:
				case BLOCK_BREAK_REACH:
				case BLOCK_BREAK_NO_SWING:
				case BLOCK_BREAK_FAST_BREAK:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + "NoCheatPlus violation level of " + data.blockBreakVL));
				break;
				
				case BLOCK_PLACE_DIRECTION:
				case BLOCK_PLACE_REACH:
				case BLOCK_PLACE_NO_SWING:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + "NoCheatPlus violation level of " + data.blockPlaceVL));
				break;
				
				case INVENTORY_THEFT:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + "The following items were removed from a container"));
					
					for (Entry<Material, Integer> entry : data.inventoryTheftTypes.entrySet()){
						sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + entry.getKey().name() + ": " + entry.getValue()));
					}
				break;
				
				case INVENTORY_INSTANT_EAT:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + "NoCheatPlus violation level of " + data.inventoryVL));
				break;
				
				case PVP_DIRECTION:
				case PVP_CRITICAL:
				case PVP_KNOCKBACK:
				case PVP_SPEED:
				case PVP_GODMODE:
				case PVP_NO_SWING:
				case PVP_INSTANT_HEAL:
				case PVP_INSTANT_BOW:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + "NoCheatPlus violation level of " + data.pvpVL));
				break;
				
				default:
					sender.sendMessage(plugin.formatMessage(ChatColor.AQUA + "This player was manually blocked, no data is available."));
				break;
			}
		}
	}
	
}
