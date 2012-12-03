package com.titankingdoms.nodinchan.titanchat.core.channel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.standard.StandardLoader;
import com.titankingdoms.nodinchan.titanchat.loading.Loader;
import com.titankingdoms.nodinchan.titanchat.util.C;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;
import com.titankingdoms.nodinchan.titanchat.util.Messaging;

public final class ChannelManager {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(2, "ChannelManager");
	
	private final Map<String, String> aliases;
	private final Map<String, Channel> channels;
	private final Map<String, ChannelLoader> loaders;
	private final Map<Type, Set<Channel>> types;
	
	public ChannelManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getChannelDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating channel directory...");
		
		if (getCustomChannelDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating custom channel directory...");
		
		if (getLoaderDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating channel loader directory...");
		
		this.aliases = new HashMap<String, String>();
		this.channels = new TreeMap<String, Channel>();
		this.loaders = new TreeMap<String, ChannelLoader>();
		this.types = new HashMap<Type, Set<Channel>>();
	}
	
	public void createChannel(CommandSender sender, String name, ChannelLoader loader) {
		db.debug(DebugLevel.I, sender.getName() + " is creating channel " + name);
		
		Channel channel = loader.create(sender, name, Type.NONE);
		register(channel);
		
		Messaging.sendMessage(sender, C.GOLD + "You have created " + channel.getName());
		
		channel.getAdmins().add(sender.getName());
		channel.join(plugin.getParticipant(sender.getName()));
		
		channel.getConfig().options().copyDefaults(true);
		channel.saveConfig();
	}
	
	public void deleteChannel(CommandSender sender, String name) {
		db.debug(DebugLevel.I, sender.getName() + " is deleting channel " + name);
		
		Channel channel = getChannel(name);
		channels.remove(channel.getName().toLowerCase());
		
		Messaging.sendMessage(sender, C.RED + "You have deleted " + channel.getName());
		channel.broadcast(C.RED + channel.getName() + " has been deleted");
		
		for (String participantName : channel.getParticipants())
			channel.leave(plugin.getParticipant(participantName));
		
		File config = new File(getChannelDirectory(), channel.getName() + ".yml");
		
		if (config.exists())
			config.delete();
	}
	
	public boolean existingChannel(String name) {
		return channels.containsKey(name.toLowerCase());
	}
	
	public boolean existingChannel(Channel channel) {
		return existingChannel(channel.getName());
	}
	
	public boolean existingChannelAlias(String alias) {
		return aliases.containsKey(alias.toLowerCase());
	}
	
	public boolean existingLoader(String name) {
		return loaders.containsKey(name.toLowerCase());
	}
	
	public boolean existingLoader(ChannelLoader loader) {
		return existingLoader(loader.getName());
	}
	
	public Channel getChannel(String name) {
		return channels.get(name.toLowerCase());
	}
	
	public Channel getChannel(Player player) {
		return null;
	}
	
	public Channel getChannelByAlias(String alias) {
		return (existingChannelAlias(alias)) ? getChannel(aliases.get(alias.toLowerCase())) : null;
	}
	
	public File getChannelDirectory() {
		return new File(plugin.getDataFolder(), "channels");
	}
	
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels.values());
	}
	
	public Set<Channel> getChannels(Type type) {
		Set<Channel> channels = types.get(type);
		return (channels != null) ? new HashSet<Channel>(channels) : new HashSet<Channel>();
	}
	
	public File getCustomChannelDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "channels");
	}
	
	public int getLimit() {
		return plugin.getConfig().getInt("channels.limit", -1);
	}
	
	public ChannelLoader getLoader(String name) {
		return loaders.get(name.toLowerCase());
	}
	
	public File getLoaderDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "channel-loaders");
	}
	
	public List<ChannelLoader> getLoaders() {
		return new ArrayList<ChannelLoader>(loaders.values());
	}
	
	public void load() {
		register(new StandardLoader());
		
		for (ChannelLoader loader : Loader.load(ChannelLoader.class, getLoaderDirectory()))
			register(loader);
		
		if (loaders.size() > 0) {
			StringBuilder str = new StringBuilder();
			
			for (ChannelLoader loader : getLoaders()) {
				if (str.length() > 0)
					str.append(", ");
				
				str.append(loader.getName());
			}
			
			plugin.log(Level.INFO, "Channel Loaders loaded: " + str.toString());
		}
		
		for (Channel channel : Loader.load(Channel.class, getCustomChannelDirectory()))
			register(channel);
		
		if (channels.size() > 0) {
			StringBuilder str = new StringBuilder();
			
			for (Channel channel : getChannels()) {
				if (str.length() > 0)
					str.append(", ");
				
				str.append(channel.getName());
			}
			
			plugin.log(Level.INFO, "Channels loaded: " + str.toString());
		}
	}
	
	public void register(Channel... channels) {
		for (Channel channel : channels) {
			if (existingChannel(channel))
				return;
			
			this.channels.put(channel.getName().toLowerCase(), channel);
			this.aliases.put(channel.getName().toLowerCase(), channel.getName());
			
			for (String alias : channel.getInfo().getAliases())
				if (!existingChannel(alias) && !existingChannelAlias(alias))
					this.aliases.put(alias.toLowerCase(), channel.getName());
			
			if (!this.types.containsKey(channel.getType()))
				this.types.put(channel.getType(), new HashSet<Channel>());
			
			this.types.get(channel.getType()).add(channel);
		}
	}
	
	public void register(ChannelLoader... loaders) {
		for (ChannelLoader loader : loaders) {
			if (existingLoader(loader))
				return;
			
			this.loaders.put(loader.getName().toLowerCase(), loader);
		}
	}
	
	public void reload() {
		for (ChannelLoader loader : getLoaders())
			loader.reload();
		
		for (Channel channel : getChannels())
			channel.reload();
	}
}