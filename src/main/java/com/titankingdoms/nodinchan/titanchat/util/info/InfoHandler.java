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
	
	public List<CachedInfo> getAllCachedInfo() {
		return new ArrayList<CachedInfo>(cachedInfo.values());
	}
	
	public List<LoadedInfo> getAllLoadedInfo() {
		return new ArrayList<LoadedInfo>(loadedInfo.values());
	}
	
	public List<PlayerInfo> getAllPlayerInfo() {
		return new ArrayList<PlayerInfo>(playerInfo.values());
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public CachedInfo getCachedInfo(Player player) {
		return getCachedInfo(player.getName());
	}
	
	public CachedInfo getCachedInfo(String player) {
		return cachedInfo.get(player.toLowerCase());
	}
	
	public String getInfo(Player player, String infoType, String def) {
		CachedInfo cachedInfo = getCachedInfo(player);
		
		if (cachedInfo == null)
			cachedInfo = loadCachedInfo(player);
		
		String info = cachedInfo.getInfo(infoType);
		return (info != null && !info.isEmpty()) ? info : def;
	}
	
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
	
	public Object getInfo(String path, Object def) {
		return getConfig().get(path, def);
	}
	
	public String getInfo(String path, String def) {
		return getConfig().getString(path, def);
	}
	
	public int getInfo(String path, int def) {
		return getConfig().getInt(path, def);
	}
	
	public double getInfo(String path, double def) {
		return getConfig().getDouble(path, def);
	}
	
	public long getInfo(String path, long def) {
		return getConfig().getLong(path, def);
	}
	
	public boolean getInfo(String path, boolean def) {
		return getConfig().getBoolean(path, def);
	}
	
	public LoadedInfo getLoadedInfo(String info) {
		return loadedInfo.get(info.toLowerCase());
	}
	
	public PlayerInfo getPlayerInfo(Player player) {
		return getPlayerInfo(player.getName());
	}
	
	public PlayerInfo getPlayerInfo(String player) {
		return playerInfo.get(player.toLowerCase());
	}
	
	public ConfigurationSection getSection(String path) {
		return getConfig().getConfigurationSection(path);
	}
	
	public boolean isConfigLoaded() {
		return new File(plugin.getDataFolder(), "info.yml").exists();
	}
	
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
	
	public void saveConfig() {
		if (config == null || configFile == null)
			return;
		
		try { config.save(configFile); } catch (IOException e) { plugin.log(Level.SEVERE, "Could not save info config to " + configFile); }
	}
}