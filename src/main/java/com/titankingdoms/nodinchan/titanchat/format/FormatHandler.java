/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.titankingdoms.nodinchan.titanchat.format;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;

/**
 * FormatHandler - Handles formatting
 * 
 * @author NodinChan
 *
 */
public final class FormatHandler {
	
	private final TitanChat plugin;
	
	protected static final Debugger db = new Debugger(5, "FormatHandler");
	
	private final static Pattern colour = Pattern.compile("(&)([a-fk-or0-9])", Pattern.CASE_INSENSITIVE);
	private final Pattern formatPattern = Pattern.compile("(%)([a-z0-9]+)", Pattern.CASE_INSENSITIVE);
	
	public static final String DEFAULT_FORMAT = "%tag %prefix%player%suffix&f: %message";
	
	public FormatHandler() {
		this.plugin = TitanChat.getInstance();
	}
	
	/**
	 * Colours the message according to the permissions of the sender
	 * 
	 * @param sender The message sender
	 * 
	 * @param msg The message to colour
	 * 
	 * @return The formatted message
	 */
	public static String colourise(Player sender, String msg) {
		StringBuffer colourised = new StringBuffer();
		Matcher match = colour.matcher(msg);
		
		while (match.find()) {
			ChatColor colour = ChatColor.getByChar(match.group(2).toLowerCase());
			
			if (sender.hasPermission("TitanChat.format.&" + colour.getChar()))
				match.appendReplacement(colourised, colour.toString());
			else
				match.appendReplacement(colourised, "");
		}
		
		return match.appendTail(colourised).toString();
	}
	
	/**
	 * Colourises the text
	 * 
	 * @param text The text to colourise
	 * 
	 * @return The colourised text
	 */
	public static String colourise(String text) {
		return text.replaceAll(colour.toString(), "\u00A7$2");
	}
	
	/**
	 * Decolourises the text
	 * 
	 * @param text The text to decolourise
	 * 
	 * @return The decolourised text
	 */
	public static String decolourise(String message) {
		return message.replaceAll(colour.toString(), "");
	}
	
	public String getDefaultFormat() {
		return DEFAULT_FORMAT;
	}
	
	public String getFormat() {
		return colourise(plugin.getConfig().getString("formatting.format", getDefaultFormat()));
	}
	
	public String getFormat(Channel channel) {
		if (channel == null || channel.getInfo() == null)
			return getFormat();
		
		String format = channel.getInfo().getFormat();
		return (format == null || format.isEmpty()) ? getFormat() : colourise(format);
	}
	
	/**
	 * Parses the format with the info handler
	 * 
	 * @param sender The sender of the message
	 * 
	 * @param format The format used
	 * 
	 * @param exclude The variables to be excluded
	 * 
	 * @return The parsed format
	 */
	public String infoParse(Player sender, String format, String... exclude) {
		db.debug(DebugLevel.I, "FormatHandler: Parsing format: " + format);
		StringBuffer parsed = new StringBuffer();
		Matcher match = formatPattern.matcher(format);
		
		List<String> exclusion = Arrays.asList(exclude);
		
		while (match.find()) {
			String infoType = match.group(2);
			
			if (exclusion.contains(infoType))
				continue;
			
			db.debug(DebugLevel.I, "FormatHandler: Matched and found info type: " + infoType);
			String info = plugin.getInfoHandler().getInfo(sender, infoType, "");
			match.appendReplacement(parsed, info);
			db.debug(DebugLevel.I, "FormatHandler: Replaced \"%" + infoType + "\" with \"" + info + "\"");
		}
		
		match.appendTail(parsed);
		db.debug(DebugLevel.I, "FormatHandler: Parsed format: " + parsed.toString());
		return parsed.toString();
	}
	
	/**
	 * Splits the line into lines of 119 characters max each
	 * 
	 * @param line The line to split
	 * 
	 * @return The array of split lines
	 */
	public static String[] split(String line) {
		List<String> lines = new LinkedList<String>();
		
		while (line.length() >= 119) {
			int end = line.lastIndexOf(' ', 119);
			
			if (end == -1)
				end = 119;
			
			lines.add(line.substring(0, end));
			line = (ChatColor.getLastColors(lines.get(lines.size() - 1)) + line.substring(end)).trim();
		}
		
		if (line.length() > 0)
			lines.add(line);
		
		return lines.toArray(new String[0]);
	}
	
	/**
	 * Splits the formatted line into lines of 119 characters max each
	 * 
	 * @param format The format to use
	 * 
	 * @param variable The variable to replace with the line
	 * 
	 * @param line The line to split
	 * 
	 * @return The array of formatted split lines
	 */
	public static String[] splitAndFormat(String format, String variable, String line) {
		return split(format.replace(variable, line));
	}
}