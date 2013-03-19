package com.titankingdoms.dev.titanchat.core.channel;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.core.channel.standard.StandardLoader;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
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
	
	public ChannelLoader getChannelLoader(String name) {
		return loaders.get(name.toLowerCase());
	}
	
	public List<ChannelLoader> getChannelLoaders() {
		return new ArrayList<ChannelLoader>(loaders.values());
	}
	
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels.values());
	}
	
	public Map<String, Channel> getChannels(Status status) {
		return new HashMap<String, Channel>(statuses.get(status));
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
	
	public boolean hasChannelLoader(String name) {
		return loaders.containsKey(name.toLowerCase());
	}
	
	public boolean hasChannelLoader(ChannelLoader loader) {
		return hasChannelLoader(loader.getName());
	}
	
	public void load() {
		for (Status status : EnumSet.allOf(Status.class))
			this.statuses.put(status, new HashMap<String, Channel>());
		
		registerChannelLoaders(new StandardLoader());
		
		for (File file : getChannelDirectory().listFiles(new ExtensionFilter(".yml"))) {
			String name = file.getName().substring(0, file.getName().lastIndexOf(".yml"));
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			ChannelLoader loader = getChannelLoader(config.getString("type", ""));
			
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
	}
	
	public void registerChannelLoaders(ChannelLoader... loaders) {
		if (loaders == null)
			return;
		
		for (ChannelLoader loader : loaders) {
			if (loader == null)
				continue;
			
			if (hasChannelLoader(loader)) {
				plugin.log(Level.WARNING, "Duplicate type loader: " + loader.getName());
				continue;
			}
			
			this.loaders.put(loader.getName().toLowerCase(), loader);
		}
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