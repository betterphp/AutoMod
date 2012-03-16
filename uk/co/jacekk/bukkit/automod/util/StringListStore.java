package uk.co.jacekk.bukkit.automod.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StringListStore {
	
	private File storageFile;
	
	private ArrayList<String> data;
	
	public StringListStore(File storageFile){
		this.storageFile = storageFile;
		
		this.data = new ArrayList<String>();
		
		if (this.storageFile.exists() == false){
			try {
				this.storageFile.createNewFile();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void load(){
		try{
			DataInputStream input = new DataInputStream(new FileInputStream(this.storageFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			String line, entry;
			
			while ((line = reader.readLine()) != null){
				entry = line.toLowerCase();
				
				if (this.data.contains(entry) == false){
					this.data.add(entry);
				}
			}
			
			reader.close();
			input.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void save(){
		try{
			FileWriter stream = new FileWriter(this.storageFile);
			BufferedWriter out = new BufferedWriter(stream);
			
			for (String entry : this.data){
				out.write(entry);
				out.newLine();
			}
			
			out.close();
			stream.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public List<String> getAll(){
		return this.data;
	}
	
	public Integer getSize(){
		return this.data.size();
	}
	
	public void add(String entry){
		entry = entry.toLowerCase();
		
		if (this.data.contains(entry) == false){
			this.data.add(entry);
		}
	}
	
	public void remove(String entry){
		this.data.remove(entry.toLowerCase());
	}
	
	public void removeAll(){
		this.data.clear();
	}
	
	public boolean contains(String entry){
		return this.data.contains(entry.toLowerCase());
	}
	
}
