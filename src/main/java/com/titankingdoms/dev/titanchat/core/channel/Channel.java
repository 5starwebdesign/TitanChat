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

package com.titankingdoms.dev.titanchat.core.channel;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.addon.ChatAddon;
import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.ChatEntity;
import com.titankingdoms.dev.titanchat.core.channel.info.Range;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.event.ChannelJoinEvent;
import com.titankingdoms.dev.titanchat.event.ChannelLeaveEvent;

/**
 * {@link Channel} - Channels for communication
 * 
 * @author NodinChan
 *
 */
public abstract class Channel extends ChatEntity {
	
	private final String type;
	
	private final Status status;
	
	private final Set<String> blacklist;
	private final Set<String> operators;
	private final Set<String> whitelist;
	
	private final Map<String, Participant> participants;
	
	public Channel(String name, String type, Status status) {
		super("Channel", name);
		this.type = type;
		this.status = status;
		this.blacklist = new HashSet<String>();
		this.operators = new HashSet<String>();
		this.whitelist = new HashSet<String>();
		this.participants = new HashMap<String, Participant>();
	}
	
	public abstract String[] getAliases();
	
	public final Set<String> getBlacklist() {
		return blacklist;
	}
	
	@Override
	public final File getConfigFile() {
		return new File (getDataFolder(), getName() + ".yml");
	}
	
	@Override
	public final File getDataFolder() {
		return plugin.getChannelManager().getChannelDirectory();
	}
	
	@Override
	public ConfigurationSection getDataSection() {
		return getConfig().getConfigurationSection("data");
	}
	
	@Override
	public InputStream getDefaultConfigStream() {
		return plugin.getResource("channel.yml");
	}
	
	public abstract String getDisplayColour();
	
	public abstract String getFormat();
	
	public final Set<Participant> getParticipants() {
		return new HashSet<Participant>(participants.values());
	}
	
	public final Set<String> getOperators() {
		return operators;
	}
	
	public abstract Range getRange();
	
	public final Status getStatus() {
		return status;
	}
	
	public final String getType() {
		return type;
	}
	
	public final Set<String> getWhitelist() {
		return whitelist;
	}
	
	@Override
	public void init() {
		super.init();
		blacklist.addAll(getConfig().getStringList("blacklist"));
		whitelist.addAll(getConfig().getStringList("whitelist"));
		operators.addAll(getConfig().getStringList("operators"));
	}
	
	public boolean isParticipating(String name) {
		return participants.containsKey(name.toLowerCase());
	}
	
	public boolean isParticipating(Participant participant) {
		return isParticipating(participant.getName());
	}
	
	public void join(Participant participant) {
		if (participant == null)
			return;
		
		if (!isParticipating(participant))
			participants.put(participant.getName().toLowerCase(), participant);
		
		if (!participant.isParticipating(this))
			participant.join(this);
		
		ChannelJoinEvent event = new ChannelJoinEvent(participant, this);
		plugin.getServer().getPluginManager().callEvent(event);
	}
	
	public void leave(Participant participant) {
		if (participant == null)
			return;
		
		if (isParticipating(participant))
			participants.remove(participant.getName().toLowerCase());
		
		if (participant.isParticipating(this))
			participant.leave(this);
		
		ChannelLeaveEvent event = new ChannelLeaveEvent(participant, this);
		plugin.getServer().getPluginManager().callEvent(event);
	}
	
	public final void register(ChatAddon... addons) {
		plugin.getAddonManager().registerAddons(addons);
	}
	
	public final void register(Channel... channels) {
		plugin.getChannelManager().registerChannels(channels);
	}
	
	public final void register(ChannelLoader... loaders) {
		plugin.getChannelManager().registerLoaders(loaders);
	}
	
	public final void register(Command... commands) {
		plugin.getCommandManager().registerCommands(commands);
	}
	
	public void reload() {
		reloadConfig();
	}
	
	@Override
	public void save() {
		super.save();
		getConfig().set("blacklist", getBlacklist());
		getConfig().set("whitelist", getWhitelist());
		getConfig().set("operators", getOperators());
		saveConfig();
	}
	
	@Override
	public void sendMessage(String... messages) {
		for (Participant participant : participants.values())
			participant.sendMessage(messages);
	}
}