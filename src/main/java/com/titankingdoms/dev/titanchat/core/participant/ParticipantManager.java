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

package com.titankingdoms.dev.titanchat.core.participant;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.util.Debugger;

public final class ParticipantManager {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(4, "ParticipantManager");
	
	private File configFile;
	private FileConfiguration config;
	
	private final Map<String, Participant> participants;
	
	public ParticipantManager() {
		this.plugin = TitanChat.getInstance();
		this.participants = new HashMap<String, Participant>();
	}
	
	/**
	 * Gets the configuration for {@link Participant}s
	 * 
	 * @return The configuration
	 */
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	/**
	 * Gets the {@link ConsoleParticipant}
	 * 
	 * @return The {@link ConsoleParticipant}
	 */
	public ConsoleParticipant getConsoleParticipant() {
		if (!hasParticipant("CONSOLE"))
			registerParticipants(new ConsoleParticipant());
		
		return (ConsoleParticipant) participants.get("CONSOLE".toLowerCase());
	}
	
	/**
	 * Gets the specified {@link Participant}
	 * 
	 * @param name The name of the {@link Participant}
	 * 
	 * @return The specified {@link Participant}
	 */
	public Participant getParticipant(String name) {
		if (name.equals("CONSOLE"))
			return getConsoleParticipant();
		
		if (!hasParticipant(name)) {
			Player player = plugin.getServer().getPlayerExact(name);
			
			if (player != null)
				return getParticipant(player);
			
			return new Participant(plugin.getServer().getOfflinePlayer(name).getName());
		}
		
		return participants.get(name.toLowerCase());
	}
	
	/**
	 * Gets the specified {@link Participant}
	 * 
	 * @param sender The {@link CommandSender} represented by the {@link Participant}
	 * 
	 * @return The specified {@link Participant}
	 */
	public Participant getParticipant(CommandSender sender) {
		if (sender.getName().equals("CONSOLE"))
			return getConsoleParticipant();
		
		if (!hasParticipant(sender.getName()))
			registerParticipants(new PlayerParticipant((Player) sender));
		
		return participants.get(sender.getName().toLowerCase());
	}
	
	/**
	 * Gets all {@link Participant}s
	 * 
	 * @return All registered {@link Participant}s
	 */
	public Set<Participant> getParticipants() {
		return new HashSet<Participant>(participants.values());
	}
	
	/**
	 * Checks if the {@link Participant} has been registered
	 * 
	 * @param name The name of the {@link Participant}
	 * 
	 * @return True if found
	 */
	public boolean hasParticipant(String name) {
		return participants.containsKey((name != null) ? name.toLowerCase() : "");
	}
	
	/**
	 * Checks if the {@link Participant} has been registered
	 * 
	 * @param participant The {@link Participant}
	 * 
	 * @return True if found
	 */
	public boolean hasParticipant(Participant participant) {
		return hasParticipant((participant != null) ? participant.getName() : "");
	}
	
	/**
	 * Loads the manager
	 */
	public void load() {
		registerParticipants(new ConsoleParticipant());
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			registerParticipants(new PlayerParticipant(player));
	}
	
	/**
	 * Registers the {@link Participant}s
	 * 
	 * @param participants The {@link Participant}s to register
	 */
	public void registerParticipants(Participant... participants) {
		if (participants == null)
			return;
		
		for (Participant participant : participants) {
			if (participant == null)
				continue;
			
			if (hasParticipant(participant)) {
				plugin.log(Level.WARNING, "Duplicate participant: " + participant.getName());
				continue;
			}
			
			this.participants.put(participant.getName().toLowerCase(), participant);
			db.debug(Level.INFO, "Registered participant: " + participant.getName());
		}
	}
	
	/**
	 * Reloads the manager
	 */
	public void reload() {
		for (Participant participant : getParticipants())
			unregisterParticipant(participant);
		
		saveConfig();
		
		registerParticipants(new ConsoleParticipant());
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			registerParticipants(new PlayerParticipant(player));
	}
	
	/**
	 * Reloads the configuration for {@link Participant}s
	 */
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "participants.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("participants.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	/**
	 * Saves the configuration for {@link Participant}s
	 */
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
	
	/**
	 * Unloads the manager
	 */
	public void unload() {
		for (Participant participant : getParticipants())
			unregisterParticipant(participant);
		
		saveConfig();
	}
	
	/**
	 * Unregisters the {@link Participant}
	 * 
	 * @param participant The {@link Participant} to unregister
	 */
	public void unregisterParticipant(Participant participant) {
		if (participant == null || !hasParticipant(participant))
			return;
		
		participant.save();
		
		this.participants.remove(participant.getName().toLowerCase());
		db.debug(Level.INFO, "Unregistered participant: " + participant.getName());
	}
}