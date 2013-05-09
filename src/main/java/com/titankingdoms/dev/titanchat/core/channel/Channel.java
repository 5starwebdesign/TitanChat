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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.addon.ChatAddon;
import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.ChatEntity;
import com.titankingdoms.dev.titanchat.core.channel.info.Range;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.event.ChannelJoinEvent;
import com.titankingdoms.dev.titanchat.event.ChannelLeaveEvent;
import com.titankingdoms.dev.titanchat.format.tag.Tag;
import com.titankingdoms.dev.titanchat.topic.Topic;
import com.titankingdoms.dev.titanchat.util.Debugger;

/**
 * {@link Channel} - Channels for communication
 * 
 * @author NodinChan
 *
 */
public abstract class Channel extends ChatEntity {
	
	private final Debugger db = new Debugger(3, "Channel");
	
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
	
	/**
	 * Gets the aliases of the {@link Channel}
	 * 
	 * @return The aliases
	 */
	public abstract String[] getAliases();
	
	/**
	 * Gets the blacklist of the {@link Channel}
	 * 
	 * @return The blacklist
	 */
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
	
	/**
	 * Gets the chat display colour of the {@link Channel}
	 * 
	 * @return The chat dislay colour
	 */
	public abstract String getDisplayColour();
	
	/**
	 * Gets the format of the {@link Channel}
	 * 
	 * @return The format
	 */
	public abstract String getFormat();
	
	/**
	 * Gets the {@link Participant}s of the {@link Channel}
	 * 
	 * @return The {@link Participant}s
	 */
	public Set<Participant> getParticipants() {
		return new HashSet<Participant>(participants.values());
	}
	
	/**
	 * Gets the operators of the {@link Channel}
	 * 
	 * @return The operators
	 */
	public final Set<String> getOperators() {
		return operators;
	}
	
	/**
	 * Gets the {@link Range} of the {@link Channel}
	 * 
	 * @return The {@link Range}
	 */
	public abstract Range getRange();
	
	/**
	 * Gets the {@link Status} of the {@link Channel}
	 * 
	 * @return The {@link Status}
	 */
	public final Status getStatus() {
		return status;
	}
	
	/**
	 * Gets the type of the {@link Channel}
	 * 
	 * @return The type
	 */
	public final String getType() {
		return type;
	}
	
	/**
	 * Gets the whitelist of the {@link Channel}
	 * 
	 * @return The whitelist
	 */
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
	
	/**
	 * Checks if the specified {@link Participant} is participating in the {@link Channel}
	 * 
	 * @param name The name of the {@link Participant}
	 * 
	 * @return True if found
	 */
	public boolean isParticipating(String name) {
		return participants.containsKey(name.toLowerCase());
	}
	
	/**
	 * Checks if the speicified {@link Participant} is participating in the {@link Channel}
	 * 
	 * @param participant The {@link Participant}
	 * 
	 * @return True if found
	 */
	public boolean isParticipating(Participant participant) {
		return isParticipating(participant.getName());
	}
	
	/**
	 * Joins the {@link Channel}
	 * 
	 * @param participant The {@link Participant} to join
	 */
	public void join(Participant participant) {
		if (participant == null)
			return;
		
		if (!isParticipating(participant)) {
			sendMessage("&6" + participant.getDisplayName() + " has joined " + getName());
			participants.put(participant.getName().toLowerCase(), participant);
		}
		
		if (!participant.isParticipating(this))
			participant.join(this);
		
		ChannelJoinEvent event = new ChannelJoinEvent(participant, this);
		plugin.getServer().getPluginManager().callEvent(event);
		
		db.debug(Level.INFO, getName() + " Channel Join: " + participant.getName());
	}
	
	/**
	 * Leaves the {@link Channel}
	 * 
	 * @param participant The {@link Participant} to leave
	 */
	public void leave(Participant participant) {
		if (participant == null)
			return;
		
		if (isParticipating(participant)) {
			participants.remove(participant.getName().toLowerCase());
			sendMessage("&6" + participant.getDisplayName() + " has left " + getName());
		}
		
		if (participant.isParticipating(this))
			participant.leave(this);
		
		ChannelLeaveEvent event = new ChannelLeaveEvent(participant, this);
		plugin.getServer().getPluginManager().callEvent(event);
		
		db.debug(Level.INFO, getName() + " Channel Leave: " + participant.getName());
	}
	
	/**
	 * Registers the {@link ChatAddon}s
	 * 
	 * @param addons The {@link ChatAddon}s to register
	 */
	public final void register(ChatAddon... addons) {
		plugin.getAddonManager().registerAddons(addons);
	}
	
	/**
	 * Registers the {@link Channel}s
	 * 
	 * @param channels The {@link Channel}s to register
	 */
	public final void register(Channel... channels) {
		plugin.getChannelManager().registerChannels(channels);
	}
	
	/**
	 * Registers the {@link ChannelLoader}s
	 * 
	 * @param loaders The {@link ChannelLoader}s to register
	 */
	public final void register(ChannelLoader... loaders) {
		plugin.getChannelManager().registerLoaders(loaders);
	}
	
	/**
	 * Registers the {@link Command}s
	 * 
	 * @param commands The {@link Command}s to register
	 */
	public final void register(Command... commands) {
		plugin.getCommandManager().registerCommands(commands);
	}
	
	/**
	 * Registers the {@link Tag}s
	 * 
	 * @param tags The {@link Tag}s to register
	 */
	public final void register(Tag... tags) {
		plugin.getTagManager().registerTags(tags);
	}
	
	/**
	 * Registers the {@link Topic}s
	 * 
	 * @param topics The {@link Topic}s to register
	 */
	public final void register(Topic... topics) {
		plugin.getTopicManager().registerTopics(topics);
	}
	
	public void reload() {
		reloadConfig();
		init();
	}
	
	@Override
	public void save() {
		super.save();
		getConfig().set("type", getType());
		getConfig().set("status", getStatus().getName());
		getConfig().set("range", getRange().getName());
		getConfig().set("blacklist", new ArrayList<String>(getBlacklist()));
		getConfig().set("whitelist", new ArrayList<String>(getWhitelist()));
		getConfig().set("operators", new ArrayList<String>(getOperators()));
		saveConfig();
	}
	
	@Override
	public void sendMessage(String... messages) {
		for (Participant participant : participants.values())
			participant.sendMessage(messages);
	}
}