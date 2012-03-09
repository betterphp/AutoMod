package uk.co.jacekk.bukkit.automod.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.automod.AutoMod;

public class PlayerListStore {
	
	private AutoMod plugin;
	
	private File storageFile;
	
	private ArrayList<String> playerNameList;
	
	public PlayerListStore(AutoMod plugin, String fileName){
		this.plugin = plugin;
		
		this.storageFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + fileName);
		
		this.playerNameList = new ArrayList<String>();
		
		if (this.storageFile.exists() == false){
			try {
				this.storageFile.createNewFile();
				
				ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.storageFile));
				
				stream.writeObject(this.playerNameList);
				stream.flush();
				stream.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadPlayersFromFile(){
		try {
			this.playerNameList = (ArrayList<String>) new ObjectInputStream(new FileInputStream(this.storageFile)).readObject();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void writePlayersToFile(){
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.storageFile));
			
			stream.writeObject(this.playerNameList);
			stream.flush();
			stream.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Player> getPlayersOnline(){
		ArrayList<Player> players = new ArrayList<Player>();
		Player player;
		
		for (String playerName : this.playerNameList){
			if ((player = plugin.getServer().getPlayer(playerName)) != null){
				players.add(player);
			}
		}
		
		return players;
	}
	
	public List<String> getPlayerNames(){
		return this.playerNameList;
	}
	
	public void addPlayer(String playerName){
		if (this.playerNameList.contains(playerName) == false){
			this.playerNameList.add(playerName);
		}
	}
	
	public void addPlayer(Player player){
		this.addPlayer(player.getName());
	}
	
	public void removePlayer(String playerName){
		this.playerNameList.remove(playerName);
	}
	
	public void removePlayer(Player player){
		this.removePlayer(player.getName());
	}
	
	public void removeAll(){
		this.playerNameList.clear();
	}
	
	public boolean contains(String playerName){
		return this.playerNameList.contains(playerName);
	}
	
	public boolean contains(Player player){
		return this.contains(player.getName());
	}
	
}
