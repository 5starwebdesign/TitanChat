package com.titankingdoms.nodinchan.titanchat.channel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Type;
import com.titankingdoms.nodinchan.titanchat.channel.standard.StandardLoader;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;

public final class ChannelManager {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(2, "ChannelManager");
	
	private final List<Channel> defaults;
	private final List<Channel> staff;
	
	private final Map<String, String> aliases;
	private final Map<String, Channel> channels;
	private final Map<String, ChannelLoader> loaders;
	
	public ChannelManager() {
		this.plugin = TitanChat.getInstance();
		this.defaults = new ArrayList<Channel>();
		this.staff = new ArrayList<Channel>();
		this.aliases = new HashMap<String, String>();
		this.channels = new HashMap<String, Channel>();
		this.loaders = new HashMap<String, ChannelLoader>();
	}
	
	public void createChannel(CommandSender sender, String name, String type) {
		db.debug(DebugLevel.I, sender.getName() + " is creating channel " + name);
		
		ChannelLoader loader = getLoader(type);
		Channel channel = loader.create(sender, name, Type.NONE);
		register(channel);
		
		sortChannels();
		
		channel.getConfig().options().copyDefaults(true);
		channel.saveConfig();
	}
	
	public void deleteChannel(CommandSender sender, String name) {
		db.debug(DebugLevel.I, sender.getName() + " is deleting channel " + name);
		
		Channel channel = getChannel(name);
		channels.remove(channel.getName().toLowerCase());
		
		for (Participant participant : channel.getParticipants()) {
			channel.leave(participant);
			participant.send(MessageLevel.WARNING, channel.getName() + " has been deleted");
		}
		
		sortChannels();
		
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
		return getChannel(existingChannelAlias(alias) ? aliases.get(alias.toLowerCase()) : "");
	}
	
	public File getChannelDirectory() {
		return new File(plugin.getDataFolder(), "channels");
	}
	
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels.values());
	}
	
	public File getCustomChannelDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "channels");
	}
	
	public List<Channel> getDefaultChannels() {
		return new ArrayList<Channel>(defaults);
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
	
	public List<Channel> getStaffChannels() {
		return new ArrayList<Channel>(staff);
	}
	
	public void load() {
		register(new StandardLoader());
		
		for (ChannelLoader loader : plugin.getLoader().load(ChannelLoader.class, getLoaderDirectory()))
			register(loader);
		
		sortLoaders();
		
		for (Channel channel : plugin.getLoader().load(Channel.class, getCustomChannelDirectory()))
			register(channel);
		
		sortChannels();
	}
	
	public void register(Channel channel) {
		if (existingChannel(channel))
			return;
		
		switch (channel.getType()) {
		
		default:
			channels.put(channel.getName().toLowerCase(), channel);
			
		case DEFAULT:
			defaults.add(channel);
			break;
			
		case STAFF:
			defaults.add(channel);
			break;
		}
	}
	
	public void register(ChannelLoader loader) {
		if (existingLoader(loader))
			return;
		
		loaders.put(loader.getName().toLowerCase(), loader);
	}
	
	public void reload() {
		for (ChannelLoader loader : getLoaders())
			loader.reload();
		
		sortLoaders();
		
		for (Channel channel : getChannels())
			channel.reload();
		
		sortChannels();
	}
	
	private void sortChannels() {
		List<Channel> channels = new ArrayList<Channel>(this.channels.values());
		Collections.sort(channels);
		this.channels.clear();
		
		for (Channel channel : channels)
			this.channels.put(channel.getName().toLowerCase(), channel);
	}
	
	private void sortLoaders() {
		List<ChannelLoader> loaders = new ArrayList<ChannelLoader>(this.loaders.values());
		Collections.sort(loaders);
		this.loaders.clear();
		
		for (ChannelLoader loader : loaders)
			this.loaders.put(loader.getName().toLowerCase(), loader);
	}
}