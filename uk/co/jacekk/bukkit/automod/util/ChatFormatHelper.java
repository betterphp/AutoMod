package uk.co.jacekk.bukkit.automod.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class ChatFormatHelper {
	
	public int chatwidth = 318;
	
	public String charWidthIndex = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»";
	public int[] charWidths = {4, 2, 5, 6, 6, 6, 6, 3, 5, 5, 5, 6, 2, 6, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 5, 2, 5, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 3, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 2, 6, 6, 8, 9, 9, 6, 6, 6, 8, 8, 6, 8, 8, 8, 8, 8, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 9, 9, 9, 5, 9, 9, 8, 7, 7, 8, 7, 8, 8, 8, 7, 8, 8, 7, 9, 9, 6, 7, 7, 7, 7, 7, 9, 6, 7, 8, 7, 6, 6, 9, 7, 6, 7, 1};
	
	private int getCharWidth(char c){
		int key = this.charWidthIndex.indexOf(c);
		
		if (key >= 0){
			return this.charWidths[key];
		}
		
		return 6;
	}
	
	private int getStringWidth(String s){
		int length = 0;
		
		for (char c : s.replaceAll("\\u00A7.", "").toCharArray()){
			length += this.getCharWidth(c);
		}
		
		return length;
	}
	
	private String strRepeat(char c, int len){
		String str = "";
		
		for (int i = 0; i < len; ++i){
			str += c;
		}
		
		return str;
	}
	
	public String strPadCentre(String str, char pad, ChatColor padColour, ChatColor strColour){
		int padWidth = this.chatwidth - this.getStringWidth(str);
		int padCharWidth = this.getCharWidth(pad);
		
		int prepend = (padWidth / padCharWidth) / 2;
		int append = (padWidth - (prepend * padCharWidth)) / padCharWidth;
		
		return padColour + this.strRepeat(pad, prepend) + strColour + str + padColour + this.strRepeat(pad, append);
	}
	
	public String strPadLeftRight(String left, String right, char pad, ChatColor leftColour, ChatColor padColour, ChatColor rightColour){
		int padWidth = this.chatwidth - this.getStringWidth(left) - this.getStringWidth(right);
		int padCharWidth = this.getCharWidth(pad);
		
		int repeat = padWidth / padCharWidth;
		
		if (repeat == 0){
			return leftColour + left + padColour + " " + rightColour + right;
		}
		
		return leftColour + left + padColour + this.strRepeat(pad, repeat) + rightColour + right;
	}
	
	public List<String> listToColumns(List<String> pluginList){
		ArrayList<String> lines = new ArrayList<String>();
		
		if (pluginList.size() == 0){
			return lines;
		}
		
		int maxLen = 0;
		int maxLenPX = 0;
		
		for (String item : pluginList){
			if (item.length() > maxLen){
				maxLen = item.length();
				maxLenPX = this.getStringWidth(item);
			}
		}
		
		int columns = (this.chatwidth - 18) / maxLenPX;
		int columnWidth = this.chatwidth / columns;
		int padCharWidth = this.getCharWidth(' ');
		
		for (int l = 0; l < pluginList.size(); l += columns){
			String thisLine = "";
			
			for (int i = 0; i < columns; ++i){
				int index = l + i;
				
				if (index < pluginList.size()){
					String pluginName = pluginList.get(index);
					int pad = (columnWidth - this.getStringWidth(pluginName)) / padCharWidth;
					
					thisLine += pluginName + this.strRepeat(' ', pad);
				}
			}
			
			lines.add(thisLine);
		}
		
		return lines;
	}
	
	public List<String> strWrapWithPrefix(String prefix, String message, ChatColor prefixColour, ChatColor messageColour){
		ArrayList<String> lines = new ArrayList<String>();
		
		int prefixWidth = this.getStringWidth(prefix);
		int spaceWidth = this.getCharWidth(' ');
		
		int currentWidth = prefixWidth + 1;
		int currentLine = 0;
		
		lines.add(currentLine, prefixColour + prefix + " " + messageColour);
		
		for (String word : message.split(" ")){
			currentWidth += this.getStringWidth(word) + spaceWidth;
			
			if (currentWidth > this.chatwidth){
				currentWidth = prefixWidth + 1;
				++currentLine;
				
				lines.add(currentLine, prefixColour + prefix + " " + messageColour);
			}
			
			lines.set(currentLine, lines.get(currentLine) + word + " ");
		}
		
		return lines;
	}
	
}
