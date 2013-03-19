package com.titankingdoms.dev.titanchat.core.participant;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.TitanChat;

public final class ParticipantManager {
	
	private final TitanChat plugin;
	
	private File configFile;
	private FileConfiguration config;
	
	private final Map<String, Participant> participants;
	
	public ParticipantManager() {
		this.plugin = TitanChat.getInstance();
		this.participants = new HashMap<String, Participant>();
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public ConsoleParticipant getConsoleParticipant() {
		return (ConsoleParticipant) getParticipant("CONSOLE");
	}
	
	public Participant getParticipant(String name) {
		if (!hasParticipant(name)) {
			Player player = plugin.getServer().getPlayer(name);
			
			if (player != null)
				return getParticipant(player);
			
			return new Participant(plugin.getServer().getOfflinePlayer(name).getName());
		}
		
		return participants.get(name.toLowerCase());
	}
	
	public Participant getParticipant(CommandSender sender) {
		if (!hasParticipant(sender.getName()))
			registerParticipants(new PlayerParticipant((Player) sender));
		
		return participants.get(sender.getName());
	}
	
	public boolean hasParticipant(String name) {
		return participants.containsKey(name.toLowerCase());
	}
	
	public boolean hasParticipant(Participant participant) {
		return hasParticipant(participant.getName());
	}
	
	public void load() {
		registerParticipants(new ConsoleParticipant());
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			registerParticipants(new PlayerParticipant(player));
	}
	
	public void registerParticipants(Participant... participants) {
		for (Participant participant : participants) {
			if (participant == null)
				continue;
			
			if (hasParticipant(participant)) {
				plugin.log(Level.WARNING, "Duplicate participant: " + participant.getName());
				continue;
			}
			
			this.participants.put(participant.getName().toLowerCase(), participant);
		}
	}
	
	public void reload() {
		this.participants.clear();
		
		registerParticipants(new ConsoleParticipant());
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			registerParticipants(new PlayerParticipant(player));
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
	
	public void unload() {
		this.participants.clear();
	}
}