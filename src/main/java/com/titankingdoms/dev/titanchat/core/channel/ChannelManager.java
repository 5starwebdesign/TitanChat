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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.core.channel.standard.ServerChannel;
import com.titankingdoms.dev.titanchat.core.channel.standard.StandardLoader;
import com.titankingdoms.dev.titanchat.core.channel.temporary.TemporaryLoader;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.util.Debugger;
import com.titankingdoms.dev.titanchat.util.Permissions;
import com.titankingdoms.dev.titanchat.util.loading.Loader;
import com.titankingdoms.dev.titanchat.util.loading.Loader.ExtensionFilter;

/**
 * {@link ChannelManager} - Manages {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public final class ChannelManager {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(3, "ChannelManager");
	
	private final Map<String, Channel> channels;
	private final Map<String, Channel> labels;
	private final Map<String, ChannelLoader> loaders;
	private final Map<Status, Map<String, Channel>> statuses;
	
	public ChannelManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getChannelDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating channel directory...");
		
		if (getLoaderDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating loader directory...");
		
		this.channels = new TreeMap<String, Channel>();
		this.labels = new HashMap<String, Channel>();
		this.loaders = new TreeMap<String, ChannelLoader>();
		this.statuses = new HashMap<Status, Map<String, Channel>>();
	}
	
	/**
	 * Gets the specified {@link Channel}
	 * 
	 * @param name The name of the {@link Channel}
	 * 
	 * @return The specified {@link Channel} if found, otherwise null
	 */
	public Channel getChannel(String name) {
		return labels.get(name.toLowerCase());
	}
	
	/**
	 * Gets the directory that holds the configs of {@link Channel}s
	 * 
	 * @return The directory of the configs of {@link Channel}s
	 */
	public File getChannelDirectory() {
		return new File(plugin.getDataFolder(), "channels");
	}
	
	/**
	 * Gets all {@link Channel}s
	 * 
	 * @return All registered {@link Channel}s
	 */
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels.values());
	}
	
	/**
	 * Gets the {@link Channel}s with the specified {@link Status}
	 * 
	 * @param status The {@link Status} of the channels
	 * 
	 * @return The {@link Channel}s with the specified {@link Status}
	 */
	public Map<String, Channel> getChannels(Status status) {
		return new HashMap<String, Channel>(statuses.get(status));
	}
	
	/**
	 * Gets the limit to the number of {@link Channel}s
	 * 
	 * @return The limit of {@link Channel}s allowed to be registered
	 */
	public int getLimit() {
		if (!plugin.getConfig().getBoolean("channels.enable", true))
			return 1;
		
		return plugin.getConfig().getInt("channels.limit", -1);
	}
	
	/**
	 * Gets the specified {@link ChannelLoader}
	 * 
	 * @param name The name of the {@link ChannelLoader}
	 * 
	 * @return The specified {@link ChannelLoader} if found, otherwise null
	 */
	public ChannelLoader getLoader(String name) {
		return loaders.get(name.toLowerCase());
	}
	
	/**
	 * Gets the directory that holds the {@link ChannelLoader}s
	 * 
	 * @return The directory of the {@link ChannelLoader}s
	 */
	public File getLoaderDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "loaders");
	}
	
	/**
	 * Gets all {@link ChannelLoader}s
	 * 
	 * @return All registered {@link ChannelLoader}s
	 */
	public List<ChannelLoader> getLoaders() {
		return new ArrayList<ChannelLoader>(loaders.values());
	}
	
	/**
	 * Checks if the alias has been registered for a {@link Channel}
	 * 
	 * @param alias The alias
	 * 
	 * @return True if found
	 */
	public boolean hasAlias(String alias) {
		return labels.containsKey((alias != null) ? alias.toLowerCase() : "");
	}
	
	/**
	 * Checks if the {@link Channel} has been registered
	 * 
	 * @param name The name of the {@link Channel}
	 * 
	 * @return True if found
	 */
	public boolean hasChannel(String name) {
		return channels.containsKey((name != null) ? name.toLowerCase() : "");
	}
	
	/**
	 * Checks if the {@link Channel} has been registered
	 * 
	 * @param channel The {@link Channel}
	 * 
	 * @return True if found
	 */
	public boolean hasChannel(Channel channel) {
		return hasChannel((channel != null) ? channel.getName() : "");
	}
	
	/**
	 * Checks if the {@link ChannelLoader} has been registered
	 * 
	 * @param name The name of the {@link ChannelLoader}
	 * 
	 * @return True if found
	 */
	public boolean hasLoader(String name) {
		return loaders.containsKey((name != null) ? name.toLowerCase() : "");
	}
	
	/**
	 * Checks if the {@link ChannelLoader} has been registered
	 * 
	 * @param loader The {@link ChannelLoader}
	 * 
	 * @return True if found
	 */
	public boolean hasLoader(ChannelLoader loader) {
		return hasLoader((loader != null) ? loader.getName() : "");
	}
	
	/**
	 * Loads the manager
	 */
	public void load() {
		for (Status status : EnumSet.allOf(Status.class))
			this.statuses.put(status, new HashMap<String, Channel>());
		
		if (!plugin.getConfig().getBoolean("channels.enable", true)) {
			registerChannels(new ServerChannel());
			return;
		}
		
		registerLoaders(new StandardLoader(), new TemporaryLoader());
		
		registerLoaders(Loader.load(ChannelLoader.class, getLoaderDirectory()).toArray(new ChannelLoader[0]));
		
		if (!loaders.isEmpty())
			plugin.log(Level.INFO, "ChannelLoaders loaded: " + StringUtils.join(loaders.keySet(), ", "));
		
		for (File file : getChannelDirectory().listFiles(new ExtensionFilter(".yml"))) {
			String name = file.getName().substring(0, file.getName().lastIndexOf(".yml"));
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			ChannelLoader loader = getLoader(config.getString("type", ""));
			
			if (loader == null)
				continue;
			
			Status status = Status.fromName(config.getString("status", ""));
			
			if (status == null)
				continue;
			
			Channel channel = loader.load(name, status, config);
			
			if (channel == null)
				continue;
			
			channel.init();
			registerChannels(channel);
		}
		
		if (!channels.isEmpty())
			plugin.log(Level.INFO, "Channels loaded: " + StringUtils.join(channels.keySet(), ", "));
	}
	
	/**
	 * Registers the {@link Channel}s
	 * 
	 * @param channels The {@link Channel}s to register
	 */
	public void registerChannels(Channel... channels) {
		if (channels == null)
			return;
		
		for (Channel channel : channels) {
			if (channel == null || channel.getName().isEmpty() || channel.getStatus().equals(Status.TEMPORARY))
				continue;
			
			if (getLimit() >= 0 && this.channels.size() >= getLimit())
				return;
			
			if (hasChannel(channel)) {
				plugin.log(Level.WARNING, "Duplicate channel: " + channel.getName());
				continue;
			}
			
			this.channels.put(channel.getName().toLowerCase(), channel);
			this.labels.put(channel.getName().toLowerCase(), channel);
			
			for (String alias : channel.getAliases())
				if (!hasAlias(alias))
					this.labels.put(alias.toLowerCase(), channel);
			
			this.statuses.get(channel.getStatus()).put(channel.getName().toLowerCase(), channel);
			
			db.debug(Level.INFO, "Registered channel: " + channel.getName());
			
			Permissions.load(channel);
			db.debug(Level.INFO, "Registered permissions for " + channel.getName());
		}
	}
	
	/**
	 * Registers the {@link ChannelLoader}s
	 * 
	 * @param loaders The {@link ChannelLoader}s to register
	 */
	public void registerLoaders(ChannelLoader... loaders) {
		if (loaders == null)
			return;
		
		for (ChannelLoader loader : loaders) {
			if (loader == null || loader.getName().isEmpty())
				continue;
			
			if (hasLoader(loader)) {
				plugin.log(Level.WARNING, "Duplicate type loader: " + loader.getName());
				continue;
			}
			
			this.loaders.put(loader.getName().toLowerCase(), loader);
			db.debug(Level.INFO, "Registered loader: " + loader.getName());
		}
	}
	
	/**
	 * Reloads the manager
	 */
	public void reload() {
		for (ChannelLoader loader : getLoaders())
			unregisterLoader(loader);
		
		if (!plugin.getConfig().getBoolean("channels.enable", true)) {
			for (Channel channel : getChannels())
				unregisterChannel(channel);
			
			registerChannels(new ServerChannel());
			return;
		}
		
		registerLoaders(new StandardLoader(), new TemporaryLoader());
		
		registerLoaders(Loader.load(ChannelLoader.class, getLoaderDirectory()).toArray(new ChannelLoader[0]));
		
		if (!loaders.isEmpty())
			plugin.log(Level.INFO, "ChannelLoaders loaded: " + StringUtils.join(loaders.keySet(), ", "));
		
		for (Channel channel : getChannels()) {
			if (!channel.getConfigFile().exists()) {
				for (Participant participant : channel.getParticipants())
					channel.leave(participant);
				
				unregisterChannel(channel);
				continue;
			}
			
			channel.reload();
		}
		
		for (File file : getChannelDirectory().listFiles(new ExtensionFilter(".yml"))) {
			String name = file.getName().substring(0, file.getName().lastIndexOf(".yml"));
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			if (hasChannel(name))
				continue;
			
			ChannelLoader loader = getLoader(config.getString("type", ""));
			
			if (loader == null)
				continue;
			
			Status status = Status.fromName(config.getString("status", ""));
			
			if (status == null)
				continue;
			
			Channel channel = loader.load(name, status, config);
			
			if (channel == null)
				continue;
			
			channel.init();
			registerChannels(channel);
		}
		
		if (!channels.isEmpty())
			plugin.log(Level.INFO, "Channels loaded: " + StringUtils.join(channels.keySet(), ", "));
	}
	
	/**
	 * Unloads the manager
	 */
	public void unload() {
		for (Channel channel : getChannels())
			unregisterChannel(channel);
		
		for (ChannelLoader loader : getLoaders())
			unregisterLoader(loader);
		
		for (Map<String, Channel> status : this.statuses.values())
			status.clear();
	}
	
	/**
	 * Unregisters the {@link Channel}
	 * 
	 * @param channel The {@link Channel} to unregister
	 */
	public void unregisterChannel(Channel channel) {
		if (channel == null || !hasChannel(channel))
			return;
		
		channel.save();
		
		this.channels.remove(channel.getName().toLowerCase());
		this.labels.remove(channel.getName().toLowerCase());
		
		for (String alias : channel.getAliases())
			if (hasAlias(alias) && !hasChannel(alias))
				this.labels.remove(alias.toLowerCase());
		
		this.statuses.get(channel.getStatus()).remove(channel.getName().toLowerCase());
		
		db.debug(Level.INFO, "Unregistered channel: " + channel.getName());
		
		Permissions.unload(channel);
		db.debug(Level.INFO, "Unregistered permissions for " + channel.getName());
	}
	
	/**
	 * Unregisters the {@link ChannelLoader}
	 * 
	 * @param loader The {@link ChannelLoader} to unregister
	 */
	public void unregisterLoader(ChannelLoader loader) {
		if (loader == null || !hasLoader(loader))
			return;
		
		this.loaders.remove(loader.getName().toLowerCase());
		db.debug(Level.INFO, "Unregistered loader: " + loader.getName());
	}
}