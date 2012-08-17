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

package com.titankingdoms.nodinchan.titanchat.util.info;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

/**
 * InfoHandler - Handles info
 * 
 * @author NodinChan
 *
 */
public final class InfoHandler {
	
	private final TitanChat plugin;
	
	private File configFile;
	private FileConfiguration config;
	
	private final Map<String, CachedInfo> cachedInfo;
	private final Map<String, LoadedInfo> loadedInfo;
	private final Map<String, PlayerInfo> playerInfo;
	
	public InfoHandler() {
		this.plugin = TitanChat.getInstance();
		this.cachedInfo = new HashMap<String, CachedInfo>();
		this.loadedInfo = new HashMap<String, LoadedInfo>();
		this.playerInfo = new HashMap<String, PlayerInfo>();
	}
	
	/**
	 * Gets all cached info of players
	 * 
	 * @return All cached info of players
	 */
	public List<CachedInfo> getAllCachedInfo() {
		return new ArrayList<CachedInfo>(cachedInfo.values());
	}
	
	/**
	 * Gets all loaded info
	 * 
	 * @return All loaded info
	 */
	public List<LoadedInfo> getAllLoadedInfo() {
		return new ArrayList<LoadedInfo>(loadedInfo.values());
	}
	
	/**
	 * Gets all player specific info
	 * 
	 * @return All player specific info
	 */
	public List<PlayerInfo> getAllPlayerInfo() {
		return new ArrayList<PlayerInfo>(playerInfo.values());
	}
	
	/**
	 * Gets the info config
	 * 
	 * @return The info config
	 */
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	/**
	 * Gets the cached info of the player
	 * 
	 * @param player The player to get for
	 * 
	 * @return The cached info if found, otherwise null
	 */
	public CachedInfo getCachedInfo(Player player) {
		return getCachedInfo(player.getName());
	}
	
	/**
	 * Gets the cached info of the player
	 * 
	 * @param player The name of the player to get for
	 * 
	 * @return The cached info if found, otherwise null
	 */
	public CachedInfo getCachedInfo(String player) {
		return cachedInfo.get(player.toLowerCase());
	}
	
	/**
	 * Gets the info of the player
	 * 
	 * @param player The player to get for
	 * 
	 * @param infoType The type of info
	 * 
	 * @param def The default to return if not found
	 * 
	 * @return The info if found, otherwise the default
	 */
	public String getInfo(Player player, String infoType, String def) {
		CachedInfo cachedInfo = getCachedInfo(player);
		
		if (cachedInfo == null)
			cachedInfo = loadCachedInfo(player);
		
		String info = cachedInfo.getInfo(infoType);
		return (info != null && !info.isEmpty()) ? info : def;
	}
	
	/**
	 * Gets the info of the player
	 * 
	 * @param player The name of the player to get for
	 * 
	 * @param infoType The type of info
	 * 
	 * @param def The default to return if not found
	 * 
	 * @return The info if found, otherwise the default
	 */
	public String getInfo(String name, String infoType, String def) {
		CachedInfo cachedInfo = getCachedInfo(name);
		
		if (cachedInfo == null) {
			Player player = plugin.getPlayer(name);
			
			if (player != null)
				cachedInfo = loadCachedInfo(player);
			else
				cachedInfo = loadCachedInfo(name);
		}
		
		String info = cachedInfo.getInfo(infoType);
		return (info != null && !info.isEmpty()) ? info : def;
	}
	
	/**
	 * Gets info by path from the config
	 * 
	 * @param path The path of the info
	 * 
	 * @param def The default to return if not found
	 * 
	 * @return The info if found, otherwise the default
	 */
	public Object getInfo(String path, Object def) {
		return getConfig().get(path, def);
	}
	
	/**
	 * Gets info by path from the config
	 * 
	 * @param path The path of the info
	 * 
	 * @param def The default to return if not found
	 * 
	 * @return The info if found, otherwise the default
	 */
	public String getInfo(String path, String def) {
		return getConfig().getString(path, def);
	}
	
	/**
	 * Gets info by path from the config
	 * 
	 * @param path The path of the info
	 * 
	 * @param def The default to return if not found
	 * 
	 * @return The info if found, otherwise the default
	 */
	public int getInfo(String path, int def) {
		return getConfig().getInt(path, def);
	}
	
	/**
	 * Gets info by path from the config
	 * 
	 * @param path The path of the info
	 * 
	 * @param def The default to return if not found
	 * 
	 * @return The info if found, otherwise the default
	 */
	public double getInfo(String path, double def) {
		return getConfig().getDouble(path, def);
	}
	
	/**
	 * Gets info by path from the config
	 * 
	 * @param path The path of the info
	 * 
	 * @param def The default to return if not found
	 * 
	 * @return The info if found, otherwise the default
	 */
	public long getInfo(String path, long def) {
		return getConfig().getLong(path, def);
	}
	
	/**
	 * Gets info by path from the config
	 * 
	 * @param path The path of the info
	 * 
	 * @param def The default to return if not found
	 * 
	 * @return The info if found, otherwise the default
	 */
	public boolean getInfo(String path, boolean def) {
		return getConfig().getBoolean(path, def);
	}
	
	/**
	 * Gets the loaded info by name
	 * 
	 * @param info The name of the info
	 * 
	 * @return The info if found, otherwise null
	 */
	public LoadedInfo getLoadedInfo(String info) {
		return loadedInfo.get(info.toLowerCase());
	}
	
	/**
	 * Gets the player specific info
	 * 
	 * @param player The player to get for
	 * 
	 * @return The info if found, otherwise null
	 */
	public PlayerInfo getPlayerInfo(Player player) {
		return getPlayerInfo(player.getName());
	}
	
	/**
	 * Gets the player specific info
	 * 
	 * @param player The name of the player to get for
	 * 
	 * @return The info if found, otherwise null
	 */
	public PlayerInfo getPlayerInfo(String player) {
		return playerInfo.get(player.toLowerCase());
	}
	
	/**
	 * Gets the configuration section by path
	 * 
	 * @param path The path of the configuration section
	 * 
	 * @return The configuration section if found, otherwise null
	 */
	public ConfigurationSection getSection(String path) {
		return getConfig().getConfigurationSection(path);
	}
	
	/**
	 * Loads cached info for the player
	 * 
	 * @param player The player to load for
	 * 
	 * @return The loaded cached info
	 */
	public CachedInfo loadCachedInfo(Player player) {
		CachedInfo cachedInfo = new CachedInfo(player.getName());
		
		PlayerInfo playerInfo = getPlayerInfo(player);
		
		if (playerInfo != null) {
			for (Entry<String, String> infoEntry : playerInfo.getAllInfo().entrySet())
				cachedInfo.setInfo(infoEntry.getKey(), infoEntry.getValue());
		}
		
		List<LoadedInfo> allLoadedInfo = getAllLoadedInfo();
		Collections.sort(allLoadedInfo);
		Collections.reverse(allLoadedInfo);
		
		for (LoadedInfo loadedInfo : allLoadedInfo) {
			if (!player.hasPermission(loadedInfo.getPermission()))
				continue;
			
			for (Entry<String, String> infoEntry : loadedInfo.getAllInfo().entrySet()) {
				if (!cachedInfo.hasInfo(infoEntry.getKey()))
					cachedInfo.setInfo(infoEntry.getKey(), infoEntry.getValue());
			}
		}
		
		this.cachedInfo.put(player.getName(), cachedInfo);
		return cachedInfo;
	}
	
	/**
	 * Loads cached info for the player
	 * 
	 * @param name The name of the player to load for
	 * 
	 * @return The loaded cached info
	 */
	public CachedInfo loadCachedInfo(String name) {
		CachedInfo cachedInfo = new CachedInfo(name);
		
		PlayerInfo playerInfo = getPlayerInfo(name);
		
		if (playerInfo != null) {
			for (Entry<String, String> infoEntry : playerInfo.getAllInfo().entrySet())
				cachedInfo.setInfo(infoEntry.getKey(), infoEntry.getValue());
		}
		
		Player player = plugin.getPlayer(name);
		
		if (player == null) {
			this.cachedInfo.put(name, cachedInfo);
			return cachedInfo;
		}
		
		List<LoadedInfo> allLoadedInfo = getAllLoadedInfo();
		Collections.sort(allLoadedInfo);
		Collections.reverse(allLoadedInfo);
		
		for (LoadedInfo loadedInfo : allLoadedInfo) {
			if (!player.hasPermission(loadedInfo.getPermission()))
				continue;
			
			for (Entry<String, String> infoEntry : loadedInfo.getAllInfo().entrySet()) {
				if (!cachedInfo.hasInfo(infoEntry.getKey()))
					cachedInfo.setInfo(infoEntry.getKey(), infoEntry.getValue());
			}
		}
		
		this.cachedInfo.put(player.getName(), cachedInfo);
		return cachedInfo;
	}
	
	/**
	 * Loads the loaded info from file
	 */
	public void loadLoadedInfo() {
		ConfigurationSection loadedSection = getSection("permission-specific");
		
		if (loadedSection == null)
			return;
		
		for (String sectionToLoad : loadedSection.getKeys(false)) {
			LoadedInfo loadedInfo = new LoadedInfo(this, sectionToLoad);
			
			for (String infoType : loadedInfo.getSection().getKeys(false))
				loadedInfo.setInfo(infoType, loadedInfo.getSection().getString(infoType, ""));
			
			this.loadedInfo.put(sectionToLoad.toLowerCase(), loadedInfo);
		}
	}
	
	/**
	 * Loads the player specific info from file
	 */
	public void loadPlayerInfo() {
		ConfigurationSection playerSection = getSection("player-specific");
		
		if (playerSection == null)
			return;
		
		for (String player : playerSection.getKeys(false)) {
			PlayerInfo playerInfo = new PlayerInfo(this, player);
			
			for (String infoType : playerInfo.getSection().getKeys(false))
				playerInfo.setInfo(infoType, playerInfo.getSection().getString(infoType, ""));
			
			this.playerInfo.put(player.toLowerCase(), playerInfo);
		}
	}
	
	/**
	 * Reloads the info config
	 */
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "info.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("info.yml");
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	/**
	 * Saves the info config
	 */
	public void saveConfig() {
		if (config == null || configFile == null)
			return;
		
		try { config.save(configFile); } catch (IOException e) { plugin.log(Level.SEVERE, "Could not save info config to " + configFile); }
	}
	
	/**
	 * Saves the loaded info to file
	 */
	public void saveLoadedInfo() {
		for (LoadedInfo loadedInfo : getAllLoadedInfo())
			for (Entry<String, String> infoEntry : loadedInfo.getAllInfo().entrySet())
				loadedInfo.getSection().set(infoEntry.getKey(), infoEntry.getValue());
	}
	
	/**
	 * Saves the player specific info to file
	 */
	public void savePlayerInfo() {
		for (PlayerInfo playerInfo : getAllPlayerInfo())
			for (Entry<String, String> infoEntry : playerInfo.getAllInfo().entrySet())
				playerInfo.getSection().set(infoEntry.getKey(), infoEntry.getValue());
	}
}