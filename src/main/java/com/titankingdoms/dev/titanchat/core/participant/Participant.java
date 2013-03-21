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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.titankingdoms.dev.titanchat.event.ChannelChatEvent;
import com.titankingdoms.dev.titanchat.format.Censor;
import com.titankingdoms.dev.titanchat.format.ChatUtils;
import com.titankingdoms.dev.titanchat.format.Format;
import com.titankingdoms.dev.titanchat.format.tag.Tag;
import com.titankingdoms.dev.titanchat.vault.Vault;

public class Participant extends ChatEntity {
	
	private Channel current;
	
	private final Map<String, Channel> channels;
	
	public Participant(String name) {
		super("Participant", name);
		this.channels = new HashMap<String, Channel>();
		
		FileConfiguration config = plugin.getParticipantManager().getConfig();
		ConfigurationSection section = config.getConfigurationSection(getName());
		
		if (section.get("channels.all") != null) {
			for (String channelName : section.getStringList("channels.all")) {
				if (!plugin.getChannelManager().hasChannel(channelName))
					continue;
				
				join(plugin.getChannelManager().getChannel(channelName));
			}
		}
		
		if (!section.getString("channels.current", "").isEmpty())
			direct(plugin.getChannelManager().getChannel(section.getString("channels.current", "")));
		
		if (channels.isEmpty()) {
			for (Channel channel : plugin.getChannelManager().getChannels(Status.DEFAULT).values())
				join(channel);
		}
		
		init();
	}
	
	public CommandSender asCommandSender() {
		return null;
	}
	
	public final void chat(Channel channel, String message) {
		if (channel == null || !isOnline())
			return;
		
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
		
		String[] lines = ChatUtils.wordWrap(format.replace("%message", message), 119);
		
		for (Participant recipient : event.getRecipients())
			recipient.sendMessage(lines);
		
		if (event.getRecipients().size() <= 1)
			sendMessage("§6Nobody heard you...");
		
		if (!plugin.getConfig().getBoolean("logging.chat.log", false))
			return;
		
		ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		String log = event.getFormat().replace("%message", event.getMessage());
		
		if (plugin.getConfig().getBoolean("logging.chat.colour", true))
			console.sendMessage(ChatUtils.wordWrap(Format.colourise(log), 119));
		else
			console.sendMessage(ChatUtils.wordWrap(Format.decolourise(log), 119));
	}
	
	public final void chat(String message) {
		chat(current, message);
	}
	
	public final void direct(Channel channel) {
		this.current = channel;
		
		if (channel != null && !isParticipating(channel))
			join(channel);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Participant)
			return ((Participant) object).getName().equals(getName());
		
		return false;
	}
	
	public final Set<Channel> getChannels() {
		return new HashSet<Channel>(channels.values());
	}
	
	@Override
	public FileConfiguration getConfig() {
		throw new UnsupportedOperationException("Participants do not have config files");
	}
	
	public final Channel getCurrent() {
		return current;
	}
	
	@Override
	public ConfigurationSection getDataSection() {
		FileConfiguration config = plugin.getParticipantManager().getConfig();
		return config.getConfigurationSection(getName().toLowerCase() + ".data");
	}
	
	public String getDisplayName() {
		return getData("display-name", getName()).asString();
	}
	
	public String getPrefix() {
		return getData("prefix", "").asString();
	}
	
	public String getSuffix() {
		return getData("suffix", "").asString();
	}
	
	public final boolean hasPermission(String node) {
		return Vault.hasPermission(asCommandSender(), node);
	}
	
	@Override
	public final void init() {
		loadData();
	}
	
	public final boolean isOnline() {
		return asCommandSender() != null;
	}
	
	public final boolean isParticipating(String channel) {
		return channels.containsKey(channel.toLowerCase());
	}
	
	public final boolean isParticipating(Channel channel) {
		return (channel != null) ? isParticipating(channel.getName()) : false;
	}
	
	public final void join(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.put(channel.getName().toLowerCase(), channel);
		
		if (!channel.isParticipating(this))
			channel.join(this);
		
		if (!channel.equals(current))
			direct(channel);
	}
	
	public final void leave(Channel channel) {
		if (channel == null)
			return;
		
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
		plugin.getParticipantManager().getConfig().set(getName() + ".channels.current", current.getName());
		plugin.getParticipantManager().getConfig().set(getName() + ".channels.all", channels.keySet());
		saveData();
	}
	
	@Override
	public void saveConfig() {
		throw new UnsupportedOperationException("Participants do not have config files");
	}
	
	@Override
	public void sendMessage(String... messages) {
		if (isOnline())
			asCommandSender().sendMessage(messages);
	}
	
	public void setDisplayName(String name) {
		setData("display-name", name);
	}
	
	public Participant toParticipant() {
		Player player = plugin.getServer().getPlayer(getName());
		
		if (player == null)
			return this;
		
		return plugin.getParticipantManager().getParticipant(player.getName());
	}
}