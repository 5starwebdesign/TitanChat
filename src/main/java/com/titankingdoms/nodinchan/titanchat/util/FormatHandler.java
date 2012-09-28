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

package com.titankingdoms.nodinchan.titanchat.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.util.Info;
import com.titankingdoms.nodinchan.titanchat.event.chat.MessageFormatEvent;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;

/**
 * FormatHandler - Handles formatting
 * 
 * @author NodinChan
 *
 */
public final class FormatHandler {
	
	private static TitanChat plugin;
	
	protected static final Debugger db = new Debugger(5);
	
	private final Pattern colourPattern = Pattern.compile("(&)([a-fk-or0-9])", Pattern.CASE_INSENSITIVE);
	private final Pattern formatPattern = Pattern.compile("(%)([a-z0-9]+)", Pattern.CASE_INSENSITIVE);
	
	private final AffixFinder affixFinder;
	
	public FormatHandler() {
		FormatHandler.plugin = TitanChat.getInstance();
		this.affixFinder = new AffixFinder();
	}
	
	/**
	 * Formats for broadcasting
	 * 
	 * @param sender The command sender
	 * 
	 * @return The format
	 */
	public String broadcastFormat(CommandSender sender) {
		String format = plugin.getConfig().getString("chat.server.broadcast");
		
		if (sender instanceof Player) {
			format = plugin.getConfig().getString("chat.player.broadcast");
			format = format.replace("%player", ((Player) sender).getDisplayName());
			format = format.replace("%name", sender.getName());
			format = format.replace("%prefix", affixFinder.getPrefix((Player) sender));
			format = format.replace("%suffix", affixFinder.getSuffix((Player) sender));
			format = infoParse((Player) sender, format, "message");
		}
		
		return plugin.getFormatHandler().colourise(format);
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
	public String colour(Player sender, String msg) {
		StringBuffer colourised = new StringBuffer();
		Matcher match = colourPattern.matcher(msg);
		
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
	public String colourise(String text) {
		return text.replaceAll(colourPattern.toString(), "\u00A7$2");
	}
	
	/**
	 * Decolourises the text
	 * 
	 * @param text The text to decolourise
	 * 
	 * @return The decolourised text
	 */
	public String decolourise(String message) {
		return message.replaceAll(colourPattern.toString(), "");
	}
	
	/**
	 * Formats for emoting
	 * 
	 * @param sender The command sender
	 * 
	 * @param channel The channel to send to
	 * 
	 * @return The format
	 */
	public String emoteFormat(CommandSender sender, String channel) {
		String format = plugin.getConfig().getString("chat.server.emote");
		
		if (channel != null && !channel.isEmpty())
			format = plugin.getConfig().getString("chat.channel-emote.format");
		else if (sender instanceof Player)
			format = plugin.getConfig().getString("chat.player.emote");
		
		if (sender instanceof Player) {
			format = format.replace("%player", ((Player) sender).getDisplayName());
			format = format.replace("%name", sender.getName());
			format = format.replace("%prefix", affixFinder.getPrefix((Player) sender));
			format = format.replace("%suffix", affixFinder.getSuffix((Player) sender));
			format = infoParse((Player) sender, format, "action");
		}
		
		return plugin.getFormatHandler().colourise(format);
	}
	
	/**
	 * Formats for channels
	 * 
	 * @param sender The sender
	 * 
	 * @param channel The channel to send to
	 * 
	 * @return The format
	 */
	public String format(Player sender, String channel) {
		String format = "%tag %prefix%player%suffix&f: %message";
		
		Info info = plugin.getManager().getChannelManager().getChannel(channel).getInfo();
		
		if (plugin.getConfig().getBoolean("formatting.use-custom-format"))
			format = info.getFormat();
		
		format = format.replace("%player", sender.getDisplayName());
		format = format.replace("%name", sender.getName());
		format = format.replace("%tag", info.getTag());
		format = format.replace("%prefix", affixFinder.getPrefix(sender));
		format = format.replace("%suffix", affixFinder.getSuffix(sender));
		format = format.replace("%message", info.getChatColour() + "%message");
		format = infoParse(sender, format, "message");
		
		MessageFormatEvent event = new MessageFormatEvent(sender, format);
		plugin.getServer().getPluginManager().callEvent(event);
		
		return plugin.getFormatHandler().colourise(event.getFormat());
	}
	
	/**
	 * Gets the affix finder
	 * 
	 * @return The affix finder
	 */
	public AffixFinder getAffixFinder() {
		return affixFinder;
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
	 * Formats for console to channel chat
	 * 
	 * @param channel The channel to send to
	 * 
	 * @return The format
	 */
	public String serverFormat(Channel channel) {
		String format = plugin.getConfig().getString("formatting.server");
		
		Info info = channel.getInfo();
		format = format.replace("%tag", info.getTag());
		format = format.replace("%message", info.getChatColour() + "%message");
		
		return plugin.getFormatHandler().colourise(format);
	}
	
	/**
	 * Splits the line into lines of 119 characters max each
	 * 
	 * @param line The line to split
	 * 
	 * @return The array of split lines
	 */
	public String[] split(String line) {
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
	public String[] splitAndFormat(String format, String variable, String line) {
		return split(format.replace(variable, line));
	}
	
	/**
	 * Formats for whispering
	 * 
	 * @param sender The command sender
	 * 
	 * @return The format
	 */
	public String whisperFormat(CommandSender sender) {
		String format = plugin.getConfig().getString("chat.server.whisper");
		
		if (sender instanceof Player) {
			format = plugin.getConfig().getString("chat.player.whisper");
			format = format.replace("%player", ((Player) sender).getDisplayName());
			format = format.replace("%name", ((Player) sender).getName());
			format = format.replace("%prefix", affixFinder.getPrefix((Player) sender));
			format = format.replace("%suffix", affixFinder.getSuffix((Player) sender));
			format = infoParse((Player) sender, format, "message");
		}
		
		return plugin.getFormatHandler().colourise(format);
	}
}