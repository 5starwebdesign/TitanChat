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

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.EndPoint;
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
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link Participant} - Represents a {@link CommandSender}
 * 
 * @author NodinChan
 *
 */
public class Participant implements EndPoint {
	
	protected TitanChat plugin;
	
	private final Debugger db = new Debugger(4, "Participant");
	
	private final String name;
	
	private Channel current;
	
	private final Map<String, Channel> channels;
	private final Map<String, Meta> meta;
	
	private final Set<String> ignorelist;
	
	private final PrivateMessage pm;
	
	public Participant(String name) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.channels = new HashMap<String, Channel>();
		this.meta = new HashMap<String, Meta>();
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
		
		notice(ChatUtils.wordWrap(format.replace("%message", message), 50));
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
			notice("&4Please join a channel to speak");
			return;
		}
		
		if (!channel.getStatus().equals(Status.CONVERSATION)) {
			if (!hasPermission("TitanChat.speak." + channel.getName())) {
				if (!channel.getOperators().contains(getName())) {
					notice("&4You do not have permission");
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
		
		ChannelChatEvent event = new ChannelChatEvent(this, channel, format, message);
		event.getRecipients().addAll(recipients);
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
			notice("&6Nobody else heard you...");
		
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
			notice("&4Please join a channel to speak");
			return;
		}
		
		if (!channel.getStatus().equals(Status.CONVERSATION)) {
			if (!hasPermission("TitanChat.speak." + channel.getName())) {
				if (!channel.getOperators().contains(getName())) {
					notice("&4You do not have permission");
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
		
		ChannelEmoteEvent event = new ChannelEmoteEvent(this, channel, format, emote);
		event.getRecipients().addAll(recipients);
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
			notice("&6Nobody else saw you...");
		
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
	 * Gets a list of the names of all joined {@link Channel}s
	 * 
	 * @return A list of the names of all joined {@link Channel}s
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
	
	/**
	 * Gets the configuration of the {@link Participant}
	 * 
	 * @return The configuration
	 */
	public final ConfigurationSection getConfiguration() {
		return plugin.getParticipantManager().getConfig().getConfigurationSection(name);
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
	
	/**
	 * Gets the display name of the {@link Participant}
	 * 
	 * @return The display name
	 */
	public String getDisplayName() {
		return getMeta("display-name", getName()).stringValue();
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
	 * Gets all the {@link Meta}
	 * 
	 * @return All cached {@link Meta}
	 */
	public Map<String, Meta> getMeta() {
		if (meta.isEmpty())
			reloadMeta();
		
		return new HashMap<String, Meta>(meta);
	}
	
	/**
	 * Gets the specified {@link Meta} from cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @param def The default value
	 * 
	 * @return The {@link Meta} if found, otherwise the default value
	 */
	public Meta getMeta(String key, Meta def) {
		if (key == null)
			return (def != null) ? def : new Meta("", new Object());
		
		return (meta.containsKey(key)) ? this.meta.get(key) : def;
	}
	
	/**
	 * Gets the specified {@link Meta} from cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @param def The default value
	 * 
	 * @return The {@link Meta} if found, otherwise the default value
	 */
	public final Meta getMeta(String key, Object def) {
		if (key == null)
			return new Meta("", (def != null) ? def : new Object());
		
		return getMeta(key, new Meta(key, def));
	}
	
	public final String getName() {
		return name;
	}
	
	/**
	 * Gets the {@link PrivateMessage} {@link Channel}
	 * 
	 * @return The {@link PrivateMessage} {@link Channel}
	 */
	public PrivateMessage getPM() {
		return pm;
	}
	
	public final String getPointType() {
		return "Participant";
	}
	
	/**
	 * Gets the prefix of the {@link Participant}
	 * 
	 * @return The prefix
	 */
	public String getPrefix() {
		return getMeta("prefix", "").stringValue();
	}
	
	/**
	 * Gets the suffix of the {@link Participant}
	 * 
	 * @return The suffix
	 */
	public String getSuffix() {
		return getMeta("suffix", "").stringValue();
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
	
	public final void init() {
		reloadMeta();
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
	
	public void messageIn(EndPoint sender, String format, String message) {
		
	}
	
	public void messageOut(EndPoint recipient, String format, String message) {
		
	}
	
	public void notice(String... messages) {
		if (isOnline())
			asCommandSender().sendMessage(Format.colourise(messages));
	}
	
	/**
	 * Reloads the {@link Meta}
	 */
	public final void reloadMeta() {
		ConfigurationSection meta = getConfiguration().getConfigurationSection("meta");
		
		if (meta == null)
			return;
		
		for (String key : getMeta().keySet())
			setMeta(key, null);
		
		for (String key : meta.getKeys(false))
			setMeta(key, meta.get(key));
	}
	
	public void save() {
		saveMeta();
		plugin.getParticipantManager().getConfig().set(getName() + ".channels.current", getCurrent());
		plugin.getParticipantManager().getConfig().set(getName() + ".channels.all", getChannelList());
		plugin.getParticipantManager().getConfig().set(getName() + ".ignore", new ArrayList<String>(ignorelist));
		plugin.getParticipantManager().saveConfig();
	}
	
	public void saveMeta() {
		ConfigurationSection meta = getConfiguration().getConfigurationSection("meta");
		
		if (meta == null)
			return;
		
		for (String key : meta.getKeys(false))
			meta.set(key, null);
		
		for (String key : getMeta().keySet())
			meta.set(key, getMeta().get(key));
	}
	
	/**
	 * Sets the display name of the {@link Participant}
	 * 
	 * @param name The new display name
	 */
	public void setDisplayName(String name) {
		setMeta("display-name", name);
	}
	
	/**
	 * Sets the {@link Meta} in the cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @param value The new value
	 */
	public void setMeta(Meta meta) {
		if (meta == null || meta.key() == null || meta.key().isEmpty())
			return;
		
		if (meta.value() == null)
			this.meta.remove(meta.key());
		else
			this.meta.put(meta.key(), meta);
	}
	
	/**
	 * Sets the {@link Meta} in the cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @param meta The new value
	 */
	public final void setMeta(String key, Object meta) {
		if (key == null || key.isEmpty())
			return;
		
		setMeta(new Meta(key, meta));
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
	
	public static final class Meta {
		
		private final String key;
		private final Object value;
		
		public Meta(String key, Object value) {
			this.key = key;
			this.value = value;
		}
		
		public boolean booleanValue() {
			return Boolean.valueOf(stringValue());
		}
		
		public double doubleValue() {
			return NumberUtils.toDouble(stringValue(), 0.0D);
		}
		
		public float floatValue() {
			return NumberUtils.toFloat(stringValue(), 0.0F);
		}
		
		public int intValue() {
			return NumberUtils.toInt(stringValue(), 0);
		}
		
		public String key() {
			return key;
		}
		
		public long longValue() {
			return NumberUtils.toLong(stringValue(), 0L);
		}
		
		public String stringValue() {
			return (value != null) ? value.toString() : "";
		}
		
		public Object value() {
			return value;
		}
	}
}