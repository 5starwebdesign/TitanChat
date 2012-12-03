package com.titankingdoms.nodinchan.titanchat.core.channel.standard;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.channel.Range;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public final class StandardChannel extends Channel {
	
	private final StandardLoader loader;
	private final StandardInfo info;
	
	private File configFile;
	private FileConfiguration config;
	
	public StandardChannel(String name, Type type, StandardLoader loader) {
		super(name, type);
		this.loader = loader;
		this.info = new StandardInfo(this);
	}

	@Override
	public ChannelLoader getChannelLoader() {
		return loader;
	}
	
	@Override
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}

	@Override
	public ChannelInfo getInfo() {
		return info;
	}
	
	@Override
	public Range getRange() {
		Range range = Range.fromName(getInfo().getSetting("range", "channel"));
		return (range != null) ? range : Range.CHANNEL;
	}
	
	@Override
	public void reload() {
		reloadConfig();
		getConfig().set("admins", getAdmins());
		getConfig().set("blacklist", getBlacklist());
		getConfig().set("whitelist", getWhitelist());
		saveConfig();
	}
	
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
	
	@Override
	public List<Participant> selectRecipants(Player sender, String message) {
		List<Participant> participants = new ArrayList<Participant>();
		
		for (String name : getParticipants())
			participants.add(plugin.getParticipant(name));
		
		return participants;
	}
}