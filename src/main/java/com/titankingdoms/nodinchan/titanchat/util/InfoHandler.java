package com.titankingdoms.nodinchan.titanchat.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

public final class InfoHandler {
	
	private final TitanChat plugin;
	
	private File configFile;
	private FileConfiguration config;
	
	public InfoHandler() {
		this.plugin = TitanChat.getInstance();
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
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
	
	public ConfigurationSection getSection(String path) {
		return getConfig().getConfigurationSection(path);
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