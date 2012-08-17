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

package com.titankingdoms.nodinchan.titanchat.channel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.nodinchan.ncbukkit.loader.Loadable;
import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.addon.Addon;
import com.titankingdoms.nodinchan.titanchat.channel.util.Info;
import com.titankingdoms.nodinchan.titanchat.channel.util.Participant;
import com.titankingdoms.nodinchan.titanchat.channel.util.handler.*;
import com.titankingdoms.nodinchan.titanchat.command.CommandBase;
import com.titankingdoms.nodinchan.titanchat.event.chat.MessageConsoleEvent;
import com.titankingdoms.nodinchan.titanchat.event.chat.MessageReceiveEvent;
import com.titankingdoms.nodinchan.titanchat.event.chat.MessageSendEvent;
import com.titankingdoms.nodinchan.titanchat.event.util.Message;
import com.titankingdoms.nodinchan.titanchat.processing.ChatPacket;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

/**
 * Channel - Channel base
 * 
 * @author NodinChan
 *
 */
public abstract class Channel extends Loadable implements Comparable<Channel>, Listener {
	
	protected final TitanChat plugin;
	
	protected static final Debugger db = new Debugger(2);
	
	private Option option;
	
	private final Info info;
	
	private final List<String> admins;
	private final List<String> blacklist;
	private final List<String> followers;
	private final List<String> whitelist;
	
	private final Map<String, Participant> participants;
	
	private final Handler handler;
	
	private String password;
	
	private File configFile;
	private FileConfiguration config;
	
	public Channel() {
		this("", Option.TYPE);
	}
	
	public Channel(String name, Option option) {
		super(name);
		this.plugin = TitanChat.getInstance();
		this.option = option;
		this.info = new Info(this);
		this.admins = new ArrayList<String>();
		this.blacklist = new ArrayList<String>();
		this.followers = new ArrayList<String>();
		this.whitelist = new ArrayList<String>();
		this.participants = new HashMap<String, Participant>();
		this.handler = new Handler();
	}
	
	/**
	 * Checks if the player has access to the channel
	 * 
	 * @param player The player to check
	 * 
	 * @return True if the player has access
	 */
	public abstract boolean access(Player player);
	
	/**
	 * Changes the setting of the channel
	 * 
	 * @param sender The command sender
	 * 
	 * @param setting The setting to change
	 * 
	 * @param args The arguments
	 * 
	 * @return True if the channel supports changing such setting
	 */
	public final boolean changeSetting(CommandSender sender, String setting, String[] args) {
		return handler.changeSetting(sender, setting, args);
	}
	
	public final int compareTo(Channel channel) {
		if (getOption().equals(Option.TYPE))
			return getType().compareTo(channel.getType());
		else
			return getName().compareTo(channel.getName());
	}
	
	/**
	 * Creates an instance of a new channel of this type
	 * 
	 * @param sender The command sender
	 * 
	 * @param name The channel name
	 * 
	 * @param option The channel option
	 * 
	 * @return The created channel
	 */
	public abstract Channel create(CommandSender sender, String name, Option option);
	
	/**
	 * Sends a message about denial of access
	 * 
	 * @param player The player to send to
	 * 
	 * @param message The message to be sent
	 */
	public void deny(Player player, String message) {
		if (message != null && !message.equals(""))
			plugin.send(MessageLevel.WARNING, player, message);
		else
			plugin.send(MessageLevel.WARNING, player, "You do not have access");
	}
	
	/**
	 * Gets the admins of the chnanel
	 * 
	 * @return The admins
	 */
	public final List<String> getAdmins() {
		return admins;
	}
	
	/**
	 * Gets the blacklist of the channel
	 * 
	 * @return The blacklist
	 */
	public final List<String> getBlacklist() {
		return blacklist;
	}
	
	@Override
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	/**
	 * Gets the followers of the chnanel
	 * 
	 * @return The followers
	 */
	public final List<String> getFollowers() {
		return followers;
	}
	
	/**
	 * Gets info related to the channel
	 * 
	 * @return The info
	 */
	public Info getInfo() {
		return info;
	}
	
	/**
	 * Gets the option set for the channel
	 * 
	 * @return The channel option
	 */
	public final Option getOption() {
		return option;
	}
	
	/**
	 * Gets the participants of the channel
	 * 
	 * @return The participants
	 */
	public final List<Participant> getParticipants() {
		return new ArrayList<Participant>(participants.values());
	}
	
	/**
	 * Gets the password of the channel
	 * 
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Gets the type of the channel
	 * 
	 * @return The channel type
	 */
	public abstract String getType();
	
	/**
	 * Gets the whitelist of the channel
	 * 
	 * @return The whitelist
	 */
	public final List<String> getWhitelist() {
		return whitelist;
	}
	
	/**
	 * Handles the command
	 * 
	 * @param sender The command sender
	 * 
	 * @param command The command to handle
	 * 
	 * @param args The arguments
	 * 
	 * @return True if the channel has its own way of handling the command
	 */
	public final boolean handleCommand(CommandSender sender, String command, String[] args) {
		return handler.handleCommand(sender, command, args);
	}
	
	/**
	 * Checks if the player is an admin of the channel
	 * 
	 * @param name The player name
	 * 
	 * @return True if the player is an admin
	 */
	public boolean isAdmin(String name) {
		return admins.contains(name);
	}
	
	/**
	 * Checks if the player is blacklisted on the channel
	 * 
	 * @param name The player name
	 * 
	 * @return True if the player is blacklisted
	 */
	public boolean isBlacklisted(String name) {
		return blacklist.contains(name);
	}
	
	/**
	 * Checks if the player is a follower of the channel
	 * 
	 * @param name The player name
	 * 
	 * @return True if the player is a follower
	 */
	public boolean isFollower(String name) {
		return followers.contains(name);
	}
	
	/**
	 * Checks if the player is participating on the channel
	 * 
	 * @param name The player name
	 * 
	 * @return True if the player is participating
	 */
	public boolean isParticipating(String name) {
		return participants.containsKey(name.toLowerCase());
	}
	
	/**
	 * Checks if the player is whitelisted on the channel
	 * 
	 * @param name The player name
	 * 
	 * @return True if the player is whitelisted
	 */
	public boolean isWhitelisted(String name) {
		return whitelist.contains(name);
	}
	
	/**
	 * Join the channel
	 * 
	 * @param name The player name
	 */
	public void join(String name) {
		if (!participants.containsKey(name.toLowerCase())) {
			if (plugin.getManager().getChannelManager().getParticipant(name) != null) {
				Participant participant = plugin.getManager().getChannelManager().getParticipant(name);
				participants.put(name.toLowerCase(), participant);
				participant.join(this);
			}
		}
	}
	
	/**
	 * Join the channel
	 * 
	 * @param player The player
	 */
	public void join(Player player) {
		join(player.getName());
	}
	
	/**
	 * Leave the chnanel
	 * 
	 * @param name The player name
	 */
	public void leave(String name) {
		if (participants.containsKey(name.toLowerCase()))
			participants.remove(name.toLowerCase()).leave(this);
	}
	
	/**
	 * Leave the channel
	 * 
	 * @param player The player
	 */
	public void leave(Player player) {
		leave(player.getName());
	}
	
	/**
	 * Loads an instance of the channel of this type
	 * 
	 * @param name The channel name
	 * 
	 * @param option The channel option
	 * 
	 * @return The loaded channel
	 */
	public abstract Channel load(String name, Option option);
	
	@Override
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getChannelDir(), getName() + ".yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("channel.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	/**
	 * Registers the addon
	 * 
	 * @param addon The addon to register
	 */
	public final void register(Addon addon) {
		plugin.getManager().getAddonManager().register(addon);
	}
	
	/**
	 * Registers the command
	 * 
	 * @param command The command to register
	 */
	public final void register(CommandBase command) {
		plugin.getManager().getCommandManager().register(command);
	}
	
	/**
	 * Registers the listener
	 * 
	 * @param listener The listener to register
	 */
	public final void register(Listener listener) {
		plugin.register(listener);
	}
	
	/**
	 * Registers the command handlers
	 * 
	 * @param handlers The command handlers to register
	 */
	public final void registerCommandHandlers(CommandHandler... handlers) {
		handler.registerCommandHandlers(handlers);
	}
	
	/**
	 * Registers the setting handlers
	 * 
	 * @param handlers The setting handlers to register
	 */
	public final void registerSettingHandlers(SettingHandler... handlers) {
		handler.registerSettingHandlers(handlers);
	}
	
	/**
	 * Saves the admins, blacklist, whitelist and followers
	 */
	public void save() {
		getConfig().set("admins", admins);
		getConfig().set("blacklist", blacklist);
		getConfig().set("whitelist", whitelist);
		getConfig().set("followers", followers);
		saveConfig();
	}
	
	@Override
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) { plugin.log(Level.SEVERE, "Could not save config to " + configFile); }
	}
	
	/**
	 * Saves the participants
	 */
	public final void saveParticipants() {
		List<String> participants = new ArrayList<String>();
		
		for (Participant participant : getParticipants())
			participants.add(participant.getName());
		
		getConfig().set("participants", participants);
		saveConfig();
	}
	
	/**
	 * Sends the message to all participants
	 * 
	 * @param message The message to send
	 */
	public void send(String message) {
		String[] lines = plugin.getFormatHandler().split(message);
		
		for (Participant participant : getParticipants())
			if (participant.getPlayer() != null)
				participant.getPlayer().sendMessage(lines);
		
		for (String follower : getFollowers())
			if (plugin.getPlayer(follower) != null && !isParticipating(follower))
				plugin.getPlayer(follower).sendMessage(lines);
	}
	
	/**
	 * Sends the array of messages to all participants
	 * 
	 * @param messages The messages to send
	 */
	public void send(String... messages) {
		for (String message : messages)
			send(message);
	}
	
	/**
	 * Sends the message
	 * 
	 * @param sender The sender of the message
	 * 
	 * @param message The message to send
	 * 
	 * @return The console line
	 */
	public String sendMessage(Player sender, String message) {
		return sendMessage(sender, new ArrayList<Player>(), message);
	}
	
	/**
	 * Sends the message
	 * 
	 * @param sender The sender of the message
	 * 
	 * @param recipants The recipants of the message
	 * 
	 * @param message The message to send
	 * 
	 * @return The console line
	 */
	protected final String sendMessage(Player sender, List<Player> recipants, String message) {
		return sendMessage(sender, recipants.toArray(new Player[0]), message);
	}
	
	/**
	 * Sends the message
	 * 
	 * @param sender The sender of the message
	 * 
	 * @param recipants The recipants of the message
	 * 
	 * @param message The message to send
	 * 
	 * @return The console line
	 */
	protected final String sendMessage(Player sender, Player[] recipants, String message) {
		String format = plugin.getFormatHandler().format(sender, getName());
		
		MessageSendEvent sendEvent = new MessageSendEvent(sender, this, recipants, new Message(format, message));
		plugin.getServer().getPluginManager().callEvent(sendEvent);
		
		if (sendEvent.isCancelled())
			return "";
		
		MessageReceiveEvent receiveEvent = new MessageReceiveEvent(sender, sendEvent.getRecipants(), new Message(sendEvent.getFormat(), sendEvent.getMessage()));
		plugin.getServer().getPluginManager().callEvent(receiveEvent);
		
		for (Player recipant : receiveEvent.getRecipants()) {
			String chatFormat = receiveEvent.getFormat(recipant);
			String chatMessage = receiveEvent.getMessage(recipant);
			String[] formatted = plugin.getFormatHandler().splitAndFormat(chatFormat, "%message", chatMessage);
			
			ChatPacket packet = new ChatPacket(recipant, formatted);
			plugin.getChatProcessor().sendPacket(packet);
		}
		
		MessageConsoleEvent consoleEvent = new MessageConsoleEvent(sender, new Message(sendEvent.getFormat(), sendEvent.getMessage()));
		plugin.getServer().getPluginManager().callEvent(consoleEvent);
		
		return consoleEvent.getFormat().replace("%message", consoleEvent.getMessage());
	}
	
	/**
	 * Sets the password of the channel
	 * 
	 * @param password The password to set to
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Option - Channel options
	 * 
	 * @author NodinChan
	 *
	 */
	public enum Option {
		CUSTOM("custom"),
		DEFAULT("default"),
		NONE("none"),
		STAFF("staff"),
		TYPE("type");
		
		private String name;
		private static Map<String, Option> NAME_MAP = new HashMap<String, Option>();
		
		private Option(String name) {
			this.name = name;
		}
		
		static {
			for (Option option : EnumSet.allOf(Option.class))
				NAME_MAP.put(option.name.toLowerCase(), option);
		}
		
		public static Option fromName(String name) {
			return NAME_MAP.get(name.toLowerCase());
		}
		
		public String getName() {
			return name;
		}
	}
	
	/**
	 * Range - Channel ranges
	 * 
	 * @author NodinChan
	 *
	 */
	public enum Range {
		CHANNEL("channel"),
		GLOBAL("global"),
		LOCAL("local"),
		WORLD("world");
		
		private String name;
		private static Map<String, Range> NAME_MAP = new HashMap<String, Range>();
		
		private Range(String name) {
			this.name = name;
		}
		
		static {
			for (Range range : EnumSet.allOf(Range.class))
				NAME_MAP.put(range.name.toLowerCase(), range);
		}
		
		public static Range fromName(String name) {
			return NAME_MAP.get(name.toLowerCase());
		}
		
		public String getName() {
			return name;
		}
	}
}