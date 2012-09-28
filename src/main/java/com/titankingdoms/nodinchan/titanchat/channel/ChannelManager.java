/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.channel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.nodinchan.ncbukkit.loader.Loader;
import com.nodinchan.ncbukkit.util.FileExtensionFilter;
import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.Option;
import com.titankingdoms.nodinchan.titanchat.channel.standard.ServerChannel;
import com.titankingdoms.nodinchan.titanchat.channel.standard.ServerChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.standard.StandardChannel;
import com.titankingdoms.nodinchan.titanchat.channel.standard.StandardChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.util.Participant;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

/**
 * ChannelManager - Manages channels
 * 
 * @author NodinChan
 *
 */
public final class ChannelManager {
	
	private final TitanChat plugin;
	
	private static final Debugger db = new Debugger(2);
	
	private final Map<String, String> aliases;
	private final Map<String, Channel> channels;
	private final Map<String, ChannelLoader> loaders;
	private final Map<String, Participant> participants;
	private final Map<String, Boolean> silenced;
	private final Map<String, Channel> types;
	
	public ChannelManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getChannelDir().mkdirs())
			plugin.log(Level.INFO, "Creating custom channels directory...");
		
		this.aliases = new HashMap<String, String>();
		this.channels = new LinkedHashMap<String, Channel>();
		this.loaders = new HashMap<String, ChannelLoader>();
		this.participants = new HashMap<String, Participant>();
		this.silenced = new HashMap<String, Boolean>();
		this.types = new LinkedHashMap<String, Channel>();
	}
	
	/**
	 * Creates a new channel
	 * 
	 * @param sender The command sender
	 * 
	 * @param name The channel name
	 * 
	 * @param type The channel type
	 */
	public void createChannel(CommandSender sender, String name, String type) {
		db.i("ChannelManager: " + sender.getName() + " is creating " + name);
		ChannelLoader loader = getChannelLoader(type);
		Channel channel = loader.create(sender, name, Option.NONE);
		register(channel);
		
		if (sender instanceof Player) {
			channel.join((Player) sender);
			channel.getAdmins().add(sender.getName());
			plugin.send(MessageLevel.INFO, sender, "You have joined " + channel.getName());
		}
		
		channel.getConfig().options().copyDefaults(true);
		channel.saveConfig();
		
		sortChannels();
		
		plugin.getPermissions().load(channel);
		plugin.send(MessageLevel.INFO, sender, "You have created channel " + name + " of type " + loader.getName());
	}
	
	/**
	 * Deletes the chnanel
	 * 
	 * @param sender The command sender
	 * 
	 * @param name The channel name
	 */
	public void deleteChannel(CommandSender sender, String name) {
		db.i("ChannelManager: " + sender.getName() + " is deleting " + name);
		Channel channel = getChannel(name);
		
		List<Participant> participants = channel.getParticipants();
		
		for (Participant participant : participants) {
			channel.leave(participant.getName());
			
			if (participant.getPlayer() != null)
				plugin.send(MessageLevel.WARNING, participant.getPlayer(), channel.getName() + " has been deleted");
		}
		
		channels.remove(name.toLowerCase());
		
		sortChannels();
		new File(plugin.getChannelDir(), channel.getName() + ".yml").delete();
		
		plugin.getPermissions().unload(channel);
		plugin.send(MessageLevel.INFO, sender, "You have deleted " + channel.getName());
	}
	
	/**
	 * Checks if the channel exists
	 * 
	 * @param name The channel name
	 * 
	 * @return True if the channel exists
	 */
	public boolean exists(String name) {
		return channels.containsKey(name.toLowerCase());
	}
	
	/**
	 * Checks if a channel with the specified alias exists
	 * 
	 * @param alias The alias of the channel
	 * 
	 * @return True if a channel with the alias exists
	 */
	public boolean existsByAlias(String alias) {
		if (exists(alias))
			return true;
		
		if (!aliases.containsKey(alias.toLowerCase()))
			return false;
		
		return exists(aliases.get(alias.toLowerCase()));
	}
	
	/**
	 * Gets the channel with the specified name
	 * 
	 * @param name The channel name
	 * 
	 * @return The channel if found, otherwise null
	 */
	public Channel getChannel(String name) {
		return channels.get(name.toLowerCase());
	}
	
	/**
	 * Gets the current channel of the player
	 * 
	 * @param player The player to get from
	 * 
	 * @return The channel if the participant is found and is in a channel, otherwise null
	 */
	public Channel getChannel(Player player) {
		Participant participant = getParticipant(player);
		
		if (participant == null)
			return null;
		
		return participant.getCurrentChannel();
	}
	
	/**
	 * Gets the channel with the alias
	 * 
	 * @param alias The channel alias
	 * 
	 * @return The channel if found, otherwise null
	 */
	public Channel getChannelByAlias(String alias) {
		Channel channel = getChannel(alias);
		
		if (channel != null)
			return channel;
		
		if (!aliases.containsKey(alias.toLowerCase()))
			return null;
		
		return getChannel(aliases.get(alias.toLowerCase()));
	}
	
	/**
	 * Gets all the channels
	 * 
	 * @return All the channels
	 */
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels.values());
	}
	
	/**
	 * Gets the channel directory
	 * 
	 * @return The channel directory
	 */
	public File getChannelDir() {
		return new File(plugin.getManager().getAddonManager().getAddonDir(), "channels");
	}
	
	/**
	 * Gets the default channels
	 * 
	 * @return The default channels
	 */
	public List<Channel> getDefaultChannels() {
		List<Channel> defaults = new ArrayList<Channel>();
		
		for (Channel channel : channels.values())
			if (channel.getOption().equals(Option.DEFAULT))
				defaults.add(channel);
		
		return defaults;
	}
	
	public ChannelLoader getChannelLoader(String name) {
		return loaders.get(name.toLowerCase());
	}
	
	/**
	 * Gets the participant
	 * 
	 * @param name The name of the participant
	 * 
	 * @return The participant if found, otherwise null
	 */
	public Participant getParticipant(String name) {
		return participants.get(name.toLowerCase());
	}
	
	/**
	 * Gets the participant
	 * 
	 * @param player The player that the participant represents
	 * 
	 * @return The participant if found, otherwise null
	 */
	public Participant getParticipant(Player player) {
		return getParticipant(player.getName());
	}
	
	/**
	 * Gets all staff channels
	 * 
	 * @return The staff channels
	 */
	public List<Channel> getStaffChannels() {
		List<Channel> staffs = new ArrayList<Channel>();
		
		for (Channel channel : channels.values())
			if (channel.getOption().equals(Option.STAFF))
				staffs.add(channel);
		
		return staffs;
	}
	
	/**
	 * Gets the channel type
	 * 
	 * @param name The name of the type
	 * 
	 * @return The channel type if found, otherwise null
	 */
	public Channel getType(String name) {
		return types.get(name.toLowerCase());
	}
	
	/**
	 * Gets all the channel types
	 * 
	 * @return All the channel types
	 */
	public List<Channel> getTypes() {
		return new ArrayList<Channel>(types.values());
	}
	
	/**
	 * Checks if a channel is silenced
	 * 
	 * @param channel The channel to check
	 * 
	 * @return True if the channel is silenced
	 */
	public boolean isSilenced(Channel channel) {
		if (!silenced.containsKey(channel.getName().toLowerCase()))
			return false;
		
		return silenced.get(channel.getName().toLowerCase());
	}
	
	/**
	 * Loads the manager
	 */
	public void load() {
		if (!plugin.enableChannels()) {
			plugin.log(Level.INFO, "Channels disabled");
			register(new ServerChannelLoader());
			register(getChannelLoader("Server").load("Server", Option.DEFAULT));
			return;
		}
		
		Loader<Channel> loader = new Loader<Channel>(plugin, getChannelDir());
		
		register(new StandardChannelLoader());
		
		for (Channel channel : loader.load())
			register(channel);
		
		sortLoaders();
		
		for (File file : plugin.getChannelDir().listFiles(new FileExtensionFilter(".yml"))) {
			Channel channel = loadChannel(file);
			
			if (channel == null || exists(channel.getName()))
				continue;
			
			register(channel);
		}
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			loadParticipant(player);
		
		sortChannels();
		
		plugin.getPermissions().load(getChannels());
	}
	
	/**
	 * Loads the channel file
	 * 
	 * @param file The file to load
	 * 
	 * @return The channel if can be loaded, otherwise null
	 */
	private Channel loadChannel(File file) {
		String name = file.getName().replace(".yml", "");
		
		if (name == null || name.equals(""))
			return null;
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		ChannelLoader loader = getChannelLoader(config.getString("type", ""));
		
		if (loader == null) {
			plugin.log(Level.INFO, "The channel config " + file.getName() + " failed to load: Unknown Type");
			return null;
		}
		
		Option option = Option.fromName(config.getString("option", ""));
		
		if (option == null) {
			plugin.log(Level.INFO, "The channel config " + file.getName() + " failed to load: Unknown Option");
			return null;
		}
		
		return loader.load(name, option);
	}
	
	/**
	 * Loads the participant
	 * 
	 * @param player The player to load the participant from
	 * 
	 * @return The loaded participant
	 */
	public Participant loadParticipant(Player player) {
		Participant participant = getParticipant(player);
		
		if (participant == null)
			participant = new Participant(player);
		
		for (Channel channel : getChannels()) {
			if (!channel.access(player))
				continue;
			
			if (player.hasPermission("TitanChat.autojoin." + channel.getName()))
				channel.join(player);
			
			if (player.hasPermission("TitanChat.autoleave." + channel.getName()))
				channel.leave(player);
			
			List<String> participants = channel.getConfig().getStringList("participants");
			
			if (participants != null && participants.contains(player.getName()))
				channel.join(player);
		}
		
		if (participant.getChannels().isEmpty()) {
			for (Channel channel : getChannels())
				if (channel.access(player) && player.hasPermission("TitanChat.spawn." + channel.getName()))
					channel.join(player);
		}
		
		if (participant.getChannels().isEmpty()) {
			if (plugin.isStaff(player)) {
				for (Channel staff : getStaffChannels()) {
					if (!staff.access(player))
						continue;
					
					staff.join(player);
					break;
				}
				
			} else {
				for (Channel def : getDefaultChannels()) {
					if (!def.access(player))
						continue;
					
					def.join(player);
					break;
				}
			}
		}
		
		participants.put(participant.getName().toLowerCase(), participant);
		return participant;
	}
	
	/**
	 * Checks if the name is applicable as a channel name
	 * 
	 * @param name The name to check
	 * 
	 * @return True if the name is applicable
	 */
	public boolean nameCheck(String name) {
		if (name.contains("\\") || name.contains("|") || name.contains("/"))
			return false;
		
		if (name.contains(":") || name.contains("?"))
			return false;
		
		if (name.contains("*") || name.contains("\""))
			return false;
		
		if (name.contains("<") || name.contains(">"))
			return false;
		
		return true;
	}
	
	/**
	 * After reloading everything
	 */
	public void postReload() {
		load();
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			loadParticipant(player);
	}
	
	/**
	 * Before reloading everything
	 */
	public void preReload() {
		for (Channel channel : getChannels()) {
			channel.reloadConfig();
			channel.save();
			channel.saveParticipants();
		}
		
		this.aliases.clear();
		this.channels.clear();
		this.participants.clear();
		this.silenced.clear();
		this.types.clear();
	}
	
	/**
	 * Registers the channel
	 * 
	 * @param channel The channel to register
	 */
	public void register(Channel channel) {
		if (!exists(channel.getName())) {
			channels.put(channel.getName().toLowerCase(), channel);
			
			for (String alias : channel.getConfig().getStringList("aliases"))
				if (!aliases.containsKey(alias.toLowerCase()))
					aliases.put(alias.toLowerCase(), channel.getName());
		}
	}
	
	public void register(ChannelLoader loader) {
		if (getChannelLoader(loader.getName()) == null)
			loaders.put(loader.getName().toLowerCase(), loader);
	}
	
	/**
	 * Sets whether the channel is silenced
	 * 
	 * @param channel The channel to set
	 * 
	 * @param silenced Whether the channel is silenced
	 */
	public void setSilence(Channel channel, boolean silenced) {
		this.silenced.put(channel.getName().toLowerCase(), silenced);
	}
	
	/**
	 * Sorts the channels
	 */
	public void sortChannels() {
		List<Channel> channels = new ArrayList<Channel>(this.channels.values());
		Collections.sort(channels);
		this.channels.clear();
		
		for (Channel channel : channels)
			this.channels.put(channel.getName().toLowerCase(), channel);
	}
	
	/**
	 * Sorts the channel types
	 */
	public void sortLoaders() {
		List<ChannelLoader> loaders = new ArrayList<ChannelLoader>(this.loaders.values());
		Collections.sort(loaders);
		this.loaders.clear();
		
		for (ChannelLoader loader : loaders)
			this.loaders.put(loader.getName().toLowerCase(), loader);
	}
	
	/**
	 * Unloads the manager
	 */
	public void unload() {
		for (Channel channel : getChannels()) {
			channel.save();
			channel.saveParticipants();
		}
		
		plugin.getPermissions().unload(getChannels());
		
		this.aliases.clear();
		this.channels.clear();
		this.silenced.clear();
		this.types.clear();
	}
}