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
import com.titankingdoms.dev.titanchat.core.channel.standard.StandardLoader;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.loading.Loader;
import com.titankingdoms.dev.titanchat.loading.Loader.ExtensionFilter;;

public final class ChannelManager {
	
	private final TitanChat plugin;
	
	private final Map<String, Channel> channels;
	private final Map<String, Channel> labels;
	private final Map<String, ChannelLoader> loaders;
	private final Map<Status, Map<String, Channel>> statuses;
	
	public ChannelManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getChannelDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating channel directory...");
		
		this.channels = new TreeMap<String, Channel>();
		this.labels = new HashMap<String, Channel>();
		this.loaders = new TreeMap<String, ChannelLoader>();
		this.statuses = new HashMap<Status, Map<String, Channel>>();
	}
	
	public Channel getChannel(String name) {
		return labels.get(name.toLowerCase());
	}
	
	public File getChannelDirectory() {
		return new File(plugin.getDataFolder(), "channels");
	}
	
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels.values());
	}
	
	public Map<String, Channel> getChannels(Status status) {
		return new HashMap<String, Channel>(statuses.get(status));
	}
	
	public ChannelLoader getLoader(String name) {
		return loaders.get(name.toLowerCase());
	}
	
	public File getLoaderDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "loaders");
	}
	
	public List<ChannelLoader> getLoaders() {
		return new ArrayList<ChannelLoader>(loaders.values());
	}
	
	public boolean hasAlias(String alias) {
		return labels.containsKey(alias.toLowerCase());
	}
	
	public boolean hasChannel(String name) {
		return channels.containsKey(name.toLowerCase());
	}
	
	public boolean hasChannel(Channel channel) {
		return hasChannel(channel.getName());
	}
	
	public boolean hasLoader(String name) {
		return loaders.containsKey(name.toLowerCase());
	}
	
	public boolean hasLoader(ChannelLoader loader) {
		return hasLoader(loader.getName());
	}
	
	public void load() {
		for (Status status : EnumSet.allOf(Status.class))
			this.statuses.put(status, new HashMap<String, Channel>());
		
		registerLoaders(new StandardLoader());
		
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
	
	public void registerChannels(Channel... channels) {
		if (channels == null)
			return;
		
		for (Channel channel : channels) {
			if (channel == null)
				continue;
			
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
		}
	}
	
	public void registerLoaders(ChannelLoader... loaders) {
		if (loaders == null)
			return;
		
		for (ChannelLoader loader : loaders) {
			if (loader == null)
				continue;
			
			if (hasLoader(loader)) {
				plugin.log(Level.WARNING, "Duplicate type loader: " + loader.getName());
				continue;
			}
			
			this.loaders.put(loader.getName().toLowerCase(), loader);
		}
	}
	
	public void reload() {
		for (Channel channel : getChannels()) {
			if (!channel.getConfigFile().exists()) {
				for (Participant participant : channel.getParticipants())
					channel.leave(participant);
				
				channels.remove(channel.getName().toLowerCase());
				continue;
			}
			
			channel.reload();
		}
	}
	
	public void unload() {
		for (Channel channel : channels.values())
			channel.save();
		
		this.channels.clear();
		this.loaders.clear();
		
		for (Map<String, Channel> status : this.statuses.values())
			status.clear();
	}
}