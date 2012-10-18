package com.titankingdoms.nodinchan.titanchat.channel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.addon.Addon;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Access;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Range;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Type;
import com.titankingdoms.nodinchan.titanchat.loading.Loadable;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

public abstract class Channel extends Loadable implements Listener {
	
	protected final Debugger db = new Debugger(2, "Channel");
	
	private File configFile;
	private FileConfiguration config;
	
	private final Type type;
	
	private final List<String> admins;
	private final List<String> blacklist;
	private final List<String> followers;
	private final List<String> whitelist;
	
	private final Map<String, Participant> participants;
	
	public Channel(String name, Type type) {
		super(name);
		this.type = type;
		this.admins = new ArrayList<String>();
		this.blacklist = new ArrayList<String>();
		this.followers = new ArrayList<String>();
		this.whitelist = new ArrayList<String>();
		this.participants = new HashMap<String, Participant>();
	}
	
	public void broadcast(MessageLevel level, String message) {
		for (Participant participant : getParticipants())
			participant.send(level, message);
	}
	
	public List<String> getAdmins() {
		return admins;
	}
	
	public List<String> getBlacklist() {
		return blacklist;
	}
	
	public abstract ChannelLoader getChannelLoader();
	
	@Override
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public List<String> getFollowers() {
		return followers;
	}
	
	public abstract ChannelInfo getInfo();
	
	public List<Participant> getParticipants() {
		return new ArrayList<Participant>(participants.values());
	}
	
	public abstract Range getRange();
	
	public final Type getType() {
		return type;
	}
	
	public List<String> getWhitelist() {
		return whitelist;
	}
	
	public abstract boolean hasAccess(Player player, Access access);
	
	public boolean isAdmin(Participant participant) {
		return admins.contains(participant.getName());
	}
	
	public boolean isAdmin(OfflinePlayer player) {
		return admins.contains(player.getName());
	}
	
	public boolean isBlacklisted(Participant participant) {
		return blacklist.contains(participant.getName());
	}
	
	public boolean isBlacklisted(OfflinePlayer player) {
		return blacklist.contains(player.getName());
	}
	
	public boolean isFollower(Participant participant) {
		return followers.contains(participant.getName());
	}
	
	public boolean isFollower(OfflinePlayer player) {
		return followers.contains(player.getName());
	}
	
	public boolean isParticipating(Participant participant) {
		return participants.containsKey(participant.getName().toLowerCase());
	}
	
	public boolean isParticipating(OfflinePlayer player) {
		return participants.containsKey(player.getName().toLowerCase());
	}
	
	public boolean isWhitelisted(Participant participant) {
		return whitelist.contains(participant.getName());
	}
	
	public boolean isWhitelisted(OfflinePlayer player) {
		return whitelist.contains(player.getName());
	}
	
	public void join(Participant participant) {
		if (participant == null)
			return;
		
		if (!participants.containsKey(participant.getName().toLowerCase()))
			participants.put(participant.getName().toLowerCase(), participant);
		
		if (!participant.isParticipating(this))
			participant.join(this);
	}
	
	public void leave(Participant participant) {
		if (participant == null)
			return;
		
		if (participants.containsKey(participant.getName().toLowerCase()))
			participants.remove(participant.getName().toLowerCase());
		
		if (participant.isParticipating(this))
			participant.leave(this);
	}
	
	public final void register(Addon addon) {
		plugin.getAddonManager().register(addon);
	}
	
	public abstract void reload();
	
	@Override
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getChannelManager().getChannelDirectory(), getName() + ".yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("channel.yml");
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	@Override
	public void saveConfig() {
		if (config == null || configFile == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) { plugin.log(Level.SEVERE, "Failed to save to " + configFile); }
	}
	
	public abstract List<Participant> selectRecipants(Player sender, String message);
}