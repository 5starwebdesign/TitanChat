package com.titankingdoms.nodinchan.titanchat.util.info;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
	
	public InfoHandler() {
		this.plugin = TitanChat.getInstance();
		this.cachedInfo = new HashMap<String, CachedInfo>();
		this.loadedInfo = new HashMap<String, LoadedInfo>();
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
		return loadedInfo.get(info);
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