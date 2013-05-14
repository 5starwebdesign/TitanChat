/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.core.participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.core.ChatEntity;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.core.participant.privmsg.PrivateMessage;
import com.titankingdoms.dev.titanchat.event.ChannelChatEvent;
import com.titankingdoms.dev.titanchat.event.ChannelEmoteEvent;
import com.titankingdoms.dev.titanchat.format.Censor;
import com.titankingdoms.dev.titanchat.format.ChatUtils;
import com.titankingdoms.dev.titanchat.format.Format;
import com.titankingdoms.dev.titanchat.format.tag.Tag;
import com.titankingdoms.dev.titanchat.util.Debugger;
import com.titankingdoms.dev.titanchat.vault.Vault;

/**
 * {@link Participant} - Represents a {@link CommandSender}
 * 
 * @author NodinChan
 *
 */
public class Participant extends ChatEntity {
	
	private final Debugger db = new Debugger(4, "Participant");
	
	private Channel current;
	
	private final Map<String, Channel> channels;
	
	private final Set<String> ignorelist;
	
	private final PrivateMessage pm;
	
	public Participant(String name) {
		super("Participant", name);
		this.channels = new HashMap<String, Channel>();
		this.ignorelist = new HashSet<String>();
		this.pm = new PrivateMessage(this);
		
		FileConfiguration config = plugin.getParticipantManager().getConfig();
		ConfigurationSection section = config.getConfigurationSection(getName());
		
		if (section == null) {
			section = config.createSection(getName());
			section.set("channels.all", new ArrayList<String>());
			section.set("channels.current", "");
		}
		
		if (section.get("channels.all") != null) {
			for (String channelName : section.getStringList("channels.all")) {
				if (!plugin.getChannelManager().hasChannel(channelName))
					continue;
				
				join(plugin.getChannelManager().getChannel(channelName));
			}
		}
		
		if (channels.size() <= 1) {
			for (Channel channel : plugin.getChannelManager().getChannels(Status.DEFAULT).values())
				join(channel);
		}
		
		if (!section.getString("channels.current", "").isEmpty())
			direct(plugin.getChannelManager().getChannel(section.getString("channels.current", "")));
		
		init();
	}
	
	/**
	 * Gets the {@link CommandSender} represented by this {@link Participant}
	 * 
	 * @return The {@link CommandSender} if online
	 */
	public CommandSender asCommandSender() {
		return null;
	}
	
	/**
	 * Receives chat from the {@link Participant}
	 * 
	 * @param sender The message sender
	 * 
	 * @param format The message format
	 * 
	 * @param message The message
	 */
	public void chatIn(Participant sender, String format, String message) {
		if (!isOnline() || ignorelist.contains(sender.getName()))
			return;
		
		sendMessage(ChatUtils.wordWrap(format.replace("%message", message), 50));
	}
	
	/**
	 * Chats in the {@link Channel}
	 * 
	 * @param channel The {@link Channel} to chat in
	 * 
	 * @param message The message
	 */
	public void chatOut(Channel channel, String message) {
		if (!isOnline())
			return;
		
		if (channel == null) {
			sendMessage("&4Please join a channel to speak");
			return;
		}
		
		if (!channel.getStatus().equals(Status.CONVERSATION)) {
			if (!hasPermission("TitanChat.speak." + channel.getName())) {
				if (!channel.getOperators().contains(getName())) {
					sendMessage("&4You do not have permission");
					return;
				}
			}
		}
		
		db.debug(Level.INFO, getName() + " Chat: " + channel.getName() + ":" + message);
		
		String format = channel.getFormat();
		
		if (format == null || format.isEmpty())
			format = Format.getFormat();
		
		Set<Participant> recipients = new HashSet<Participant>();
		
		switch (channel.getRange()) {
		
		case CHANNEL:
			recipients.addAll(channel.getParticipants());
			break;
			
		case GLOBAL:
			recipients.addAll(plugin.getParticipantManager().getParticipants());
			break;
			
		case LOCAL:
			if (asCommandSender() instanceof Player) {
				double radius = plugin.getConfig().getDouble("channels.range", 15.0);
				
				for (Entity entity : ((Player) asCommandSender()).getNearbyEntities(radius, radius, radius))
					if (entity instanceof Player)
						recipients.add(plugin.getParticipantManager().getParticipant((Player) entity));
				
			} else {
				recipients.add(plugin.getParticipantManager().getConsoleParticipant());
			}
			break;
			
		case WORLD:
			if (asCommandSender() instanceof Player)
				for (Player player : ((Player) asCommandSender()).getWorld().getEntitiesByClass(Player.class))
					recipients.add(plugin.getParticipantManager().getParticipant(player));
			break;
		}
		
		if (!recipients.contains(this))
			recipients.add(this);
		
		ChannelChatEvent event = new ChannelChatEvent(this, recipients, channel, format, message);
		plugin.getServer().getPluginManager().callEvent(event);
		
		db.debug(Level.INFO, getName() + " Chat Message: " + event.getMessage());
		db.debug(Level.INFO, getName() + " Chat Format: " + event.getFormat());
		
		StringBuilder recipientList = new StringBuilder();
		
		for (Participant recipient : event.getRecipients()) {
			if (recipientList.length() > 0)
				recipientList.append(", ");
			
			recipientList.append(recipient.getName());
		}
		
		db.debug(Level.INFO, getName() + " Chat Recipients: " + recipientList.toString());
		
		List<String> phrases = plugin.getConfig().getStringList("filtering.phrases");
		String censor = plugin.getConfig().getString("filtering.censor");
		
		message = Format.colourise(Censor.filter(event.getMessage(), phrases, censor));
		
		StringBuffer parsedFormat = new StringBuffer();
		
		Pattern tagPattern = Pattern.compile("(?i)(%)([a-z0-9]+)");
		Matcher tagMatch = tagPattern.matcher(event.getFormat());
		
		while (tagMatch.find()) {
			String replacement = tagMatch.group();
			
			if (plugin.getTagManager().hasTag(tagMatch.group())) {
				Tag tag = plugin.getTagManager().getTag(tagMatch.group());
				
				String var = tag.getVariable(this, channel);
				replacement = (var != null && !var.isEmpty()) ? tag.getFormat().replace("%tag%", var) : "";
			}
			
			tagMatch.appendReplacement(parsedFormat, replacement);
		}
		
		format = Format.colourise(tagMatch.appendTail(parsedFormat).toString());
		
		for (Participant recipient : event.getRecipients())
			recipient.chatIn(this, format, message);
		
		if (event.getRecipients().size() <= 1)
			sendMessage("&6Nobody else heard you...");
		
		if (!plugin.getConfig().getBoolean("logging.chat.log", false))
			return;
		
		ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		String log = format.replace("%message", event.getMessage());
		
		if (plugin.getConfig().getBoolean("logging.chat.colour", true))
			console.sendMessage(ChatUtils.wordWrap(Format.colourise(log), 50));
		else
			console.sendMessage(ChatUtils.wordWrap(Format.decolourise(log), 50));
	}
	
	/**
	 * Chats in the current {@link Channel}
	 * 
	 * @param message The message
	 */
	public void chatOut(String message) {
		chatOut(current, message);
	}
	
	/**
	 * Directs the focus of the {@link Participant} to the specified {@link Channel}
	 * 
	 * @param channel The {@link Channel} to direct to
	 */
	public final void direct(Channel channel) {
		this.current = channel;
		
		if (channel != null && !isParticipating(channel))
			join(channel);
	}
	
	/**
	 * Emotes in the {@link Channel}
	 * 
	 * @param channel The {@link Channel} to emote in
	 * 
	 * @param emote The emote
	 */
	public final void emote(Channel channel, String emote) {
		if (!isOnline())
			return;
		
		if (channel == null) {
			sendMessage("&4Please join a channel to speak");
			return;
		}
		
		if (!channel.getStatus().equals(Status.CONVERSATION)) {
			if (!hasPermission("TitanChat.speak." + channel.getName())) {
				if (!channel.getOperators().contains(getName())) {
					sendMessage("&4You do not have permission");
					return;
				}
			}
		}
		
		db.debug(Level.INFO, getName() + " Emote: " + channel.getName() + ":" + emote);
		
		String format = plugin.getConfig().getString("emote.format", "* %prefix%display%suffix %colour%message");
		
		Set<Participant> recipients = new HashSet<Participant>();
		
		switch (channel.getRange()) {
		
		case CHANNEL:
			recipients.addAll(channel.getParticipants());
			break;
			
		case GLOBAL:
			recipients.addAll(plugin.getParticipantManager().getParticipants());
			break;
			
		case LOCAL:
			if (asCommandSender() instanceof Player) {
				double radius = plugin.getConfig().getDouble("channels.range", 15.0);
				
				for (Entity entity : ((Player) asCommandSender()).getNearbyEntities(radius, radius, radius))
					if (entity instanceof Player)
						recipients.add(plugin.getParticipantManager().getParticipant((Player) entity));
				
			} else {
				recipients.add(plugin.getParticipantManager().getConsoleParticipant());
			}
			break;
			
		case WORLD:
			if (asCommandSender() instanceof Player)
				for (Player player : ((Player) asCommandSender()).getWorld().getEntitiesByClass(Player.class))
					recipients.add(plugin.getParticipantManager().getParticipant(player));
			break;
		}
		
		if (!recipients.contains(this))
			recipients.add(this);
		
		ChannelEmoteEvent event = new ChannelEmoteEvent(this, recipients, channel, format, emote);
		plugin.getServer().getPluginManager().callEvent(event);
		
		db.debug(Level.INFO, getName() + " Emote Action: " + event.getEmote());
		db.debug(Level.INFO, getName() + " Emote Format: " + event.getFormat());
		
		StringBuilder recipientList = new StringBuilder();
		
		for (Participant recipient : event.getRecipients()) {
			if (recipientList.length() > 0)
				recipientList.append(", ");
			
			recipientList.append(recipient.getName());
		}
		
		db.debug(Level.INFO, getName() + " Emote Recipients: " + recipientList.toString());
		
		List<String> phrases = plugin.getConfig().getStringList("filtering.phrases");
		String censor = plugin.getConfig().getString("filtering.censor");
		
		emote = Format.colourise(Censor.filter(event.getEmote(), phrases, censor));
		
		StringBuffer parsedFormat = new StringBuffer();
		
		Pattern tagPattern = Pattern.compile("(?i)(%)([a-z0-9]+)");
		Matcher tagMatch = tagPattern.matcher(event.getFormat());
		
		while (tagMatch.find()) {
			String replacement = tagMatch.group();
			
			if (plugin.getTagManager().hasTag(tagMatch.group())) {
				Tag tag = plugin.getTagManager().getTag(tagMatch.group());
				
				String var = tag.getVariable(this, channel);
				replacement = (var != null && !var.isEmpty()) ? tag.getFormat().replace("%tag%", var) : "";
			}
			
			tagMatch.appendReplacement(parsedFormat, replacement);
		}
		
		format = Format.colourise(tagMatch.appendTail(parsedFormat).toString());
		
		for (Participant recipient : event.getRecipients())
			recipient.chatIn(this, format, emote);
		
		if (event.getRecipients().size() <= 1)
			sendMessage("&6Nobody else saw you...");
		
		if (!plugin.getConfig().getBoolean("logging.chat.log", false))
			return;
		
		ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		String log = format.replace("%message", event.getEmote());
		
		if (plugin.getConfig().getBoolean("logging.chat.colour", true))
			console.sendMessage(ChatUtils.wordWrap(Format.colourise(log), 50));
		else
			console.sendMessage(ChatUtils.wordWrap(Format.decolourise(log), 50));
	}
	
	/**
	 * Emotes in the current {@link Channel}
	 * 
	 * @param emote The emote
	 */
	public final void emote(String emote) {
		emote(current, emote);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Participant)
			return ((Participant) object).getName().equals(getName());
		
		return false;
	}
	
	/**
	 * Gets a list of all joined {@link Channel}s
	 * 
	 * @return A list of all joined {@link Channel}s
	 */
	public final List<String> getChannelList() {
		List<String> list = new ArrayList<String>(channels.keySet());
		Collections.sort(list);
		return list;
	}
	
	/**
	 * Gets all joined {@link Channel}s
	 * 
	 * @return All joined {@link Channel}s
	 */
	public final Set<Channel> getChannels() {
		return new HashSet<Channel>(channels.values());
	}
	
	@Override
	public FileConfiguration getConfig() {
		throw new UnsupportedOperationException("Participants do not have config files");
	}
	
	/**
	 * Gets the name of the current (@link Channel}
	 * 
	 * @return The name of the current {@link Channel}
	 */
	public final String getCurrent() {
		return (current != null) ? current.getName() : "";
	}
	
	/**
	 * Gets the current {@link Channel}
	 * 
	 * @return The current {@link Channel}
	 */
	public final Channel getCurrentChannel() {
		return current;
	}
	
	@Override
	public ConfigurationSection getDataSection() {
		FileConfiguration config = plugin.getParticipantManager().getConfig();
		return config.getConfigurationSection(getName() + ".data");
	}
	
	/**
	 * Gets the display name of the {@link Participant}
	 * 
	 * @return The display name
	 */
	public String getDisplayName() {
		return getData("display-name", getName()).asString();
	}
	
	/**
	 * Gets the ignore list of the {@link Participant}
	 * 
	 * @return The ignore list
	 */
	public Set<String> getIgnoreList() {
		return ignorelist;
	}
	
	/**
	 * Gets the {@link PrivateMessage} {@link Channel}
	 * 
	 * @return The {@link PrivateMessage} {@link Channel}
	 */
	public PrivateMessage getPM() {
		return pm;
	}
	
	/**
	 * Gets the prefix of the {@link Participant}
	 * 
	 * @return The prefix
	 */
	public String getPrefix() {
		return getData("prefix", "").asString();
	}
	
	/**
	 * Gets the suffix of the {@link Participant}
	 * 
	 * @return The suffix
	 */
	public String getSuffix() {
		return getData("suffix", "").asString();
	}
	
	/**
	 * Checks the permission of the {@link Participant}
	 * 
	 * @param node The permission node
	 * 
	 * @return True if the {@link Participant} has the permission
	 */
	public final boolean hasPermission(String node) {
		return Vault.hasPermission(asCommandSender(), node);
	}
	
	@Override
	public final void init() {
		super.init();
		ignorelist.addAll(plugin.getParticipantManager().getConfig().getStringList("ignore"));
	}
	
	/**
	 * Checks if the {@link Participant} is focused at the {@link Channel}
	 * 
	 * @param channel The {@link Channel}
	 * 
	 * @return True if focused at the {@link Channel}
	 */
	public final boolean isDirected(String channel) {
		if ((channel == null || channel.isEmpty()) && current == null)
			return true;
		
		return current.getName().equalsIgnoreCase(channel);
	}
	
	/**
	 * CHecks if the {@link Participant} is focused at the {@link Channel}
	 * 
	 * @param channel The {@link Channel}
	 * 
	 * @return True if focused at the {@link Channel}
	 */
	public final boolean isDirected(Channel channel) {
		return isDirected((channel != null) ? channel.getName() : "");
	}
	
	/**
	 * Checks if the {@link Participant} is online
	 * 
	 * @return True if the {@link Participant} is online
	 */
	public final boolean isOnline() {
		return asCommandSender() != null;
	}
	
	/**
	 * Checks if the {@link Participant} is participating in the {@link Channel}
	 * 
	 * @param channel The {@link Channel}
	 * 
	 * @return True if participating in the {@link Channel}
	 */
	public final boolean isParticipating(String channel) {
		return channels.containsKey((channel != null) ? channel.toLowerCase() : "");
	}
	
	/**
	 * Checks if the {@link Participant} is participating in the {@link Channel}
	 * 
	 * @param channel The {@link Channel}
	 * 
	 * @return True if participating in the {@link Channel}
	 */
	public final boolean isParticipating(Channel channel) {
		return (channel != null) ? isParticipating(channel.getName()) : false;
	}
	
	/**
	 * Joins the {@link Channel}
	 * 
	 * @param channel The {@link Channel} to join
	 */
	public final void join(Channel channel) {
		if (channel == null)
			return;
		
		if (!isParticipating(channel))
			this.channels.put(channel.getName().toLowerCase(), channel);
		
		if (!channel.isParticipating(this))
			channel.join(this);
		
		if (!channel.equals(current))
			direct(channel);
	}
	
	/**
	 * Leaves the {@link Channel}
	 * 
	 * @param channel The {@link Channel} to leave
	 */
	public final void leave(Channel channel) {
		if (channel == null)
			return;
		
		if (isParticipating(channel))
			this.channels.remove(channel.getName().toLowerCase());
		
		if (channel.isParticipating(this))
			channel.leave(this);
		
		if (channel.equals(current))
			direct(getChannels().iterator().hasNext() ? getChannels().iterator().next() : null);
	}
	
	@Override
	public void reloadConfig() {
		throw new UnsupportedOperationException("Participants do not have config files");
	}
	
	@Override
	public void save() {
		super.save();
		plugin.getParticipantManager().getConfig().set(getName() + ".channels.current", getCurrent());
		plugin.getParticipantManager().getConfig().set(getName() + ".channels.all", getChannelList());
		plugin.getParticipantManager().getConfig().set(getName() + ".ignore", new ArrayList<String>(ignorelist));
		plugin.getParticipantManager().saveConfig();
	}
	
	@Override
	public void saveConfig() {
		throw new UnsupportedOperationException("Participants do not have config files");
	}
	
	@Override
	public void sendMessage(String... messages) {
		if (isOnline())
			asCommandSender().sendMessage(Format.colourise(messages));
	}
	
	/**
	 * Sets the display name of the {@link Participant}
	 * 
	 * @param name The new display name
	 */
	public void setDisplayName(String name) {
		setData("display-name", name);
	}
	
	/**
	 * Gets the {@link Participant}
	 * 
	 * @return The {@link Participant} if online, otherwise this
	 */
	public Participant toParticipant() {
		if (getName().equals("CONSOLE"))
			return plugin.getParticipantManager().getConsoleParticipant();
		
		Player player = plugin.getServer().getPlayerExact(getName());
		
		if (player == null)
			return this;
		
		return plugin.getParticipantManager().getParticipant(player.getName());
	}
}