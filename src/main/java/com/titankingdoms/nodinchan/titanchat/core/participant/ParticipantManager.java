package com.titankingdoms.nodinchan.titanchat.core.participant;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;

public final class ParticipantManager {
	
	private final TitanChat plugin;
	
	private File configFile;
	private FileConfiguration config;
	
	private Map<String, ChannelParticipant> participants;
	
	public ParticipantManager() {
		this.plugin = TitanChat.getInstance();
		this.participants = new HashMap<String, ChannelParticipant>();
	}
	
	public boolean existingParticipant(String name) {
		return participants.containsKey(name.toLowerCase());
	}
	
	public boolean existingParticipant(Player player) {
		return existingParticipant(player.getName());
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public ChannelParticipant getParticipant(String name) {
		return participants.get(name.toLowerCase());
	}
	
	public ChannelParticipant getParticipant(Player player) {
		return getParticipant(player.getName());
	}
	
	public void registerParticipant(Player player) {
		if (!existingParticipant(player))
			participants.put(player.getName().toLowerCase(), new ChannelParticipant(player.getName()));
		
		ChannelParticipant participant = getParticipant(player);
		
		for (Channel channel : plugin.getChannelManager().getChannels()) {
			if (participant.hasPermission("TitanChat.autojoin." + channel.getName()))
				channel.join(participant);
			
			if (participant.hasPermission("TitanChat.autoleave." + channel.getName()))
				channel.leave(participant);
			
			if (participant.hasPermission("TitanChat.autodirect." + channel.getName()))
				participant.direct(channel);
		}
	}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "participants.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("participants.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
}