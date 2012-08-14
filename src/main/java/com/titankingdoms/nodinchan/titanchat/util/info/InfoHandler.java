package com.titankingdoms.nodinchan.titanchat.util.info;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		PlayerInfo pI = getPlayerInfo(player);
		
		if (pI != null) {
			String info = pI.getInfo(infoType);
			
			if (info != null)
				return info;
		}
		
		CachedInfo cached = getCachedInfo(player);
		
		if (cached != null) {
			String info = cached.getInfo(infoType);
			
			if (info != null)
				return info;
		}
		
		String info = "";
		int lastPriority = -1;
		
		for (LoadedInfo loaded : loadedInfo.values()) {
			if (!player.hasPermission(loaded.getPermission()))
				continue;
			
			int priority = loaded.getSection().getInt("priority", 0);
			String loadedInfo = loaded.getInfo(infoType);
			
			if (loadedInfo != null && priority > lastPriority)
				info = loadedInfo;
		}
		
		return (!info.isEmpty()) ? info : def;
	}
	
	public String getInfo(String name, String infoType, String def) {
		PlayerInfo pI = getPlayerInfo(name);
		
		if (pI != null) {
			String info = pI.getInfo(infoType);
			
			if (info != null)
				return info;
		}
		
		CachedInfo cached = getCachedInfo(name);
		
		if (cached != null) {
			String info = cached.getInfo(infoType);
			
			if (info != null)
				return info;
		}
		
		Player player = plugin.getPlayer(name);
		
		if (player == null)
			return def;
		
		String info = "";
		int lastPriority = -1;
		
		for (LoadedInfo loaded : loadedInfo.values()) {
			if (!player.hasPermission(loaded.getPermission()))
				continue;
			
			int priority = loaded.getSection().getInt("priority", 0);
			String loadedInfo = loaded.getInfo(infoType);
			
			if (loadedInfo != null && priority > lastPriority)
				info = loadedInfo;
		}
		
		return (!info.isEmpty()) ? info : def;
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