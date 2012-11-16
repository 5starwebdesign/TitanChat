package com.titankingdoms.nodinchan.titanchat.loading;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

public class Loadable implements Comparable<Loadable> {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private ClassLoader loader;
	private File file;
	private File dataFolder;
	
	private File configFile;
	private FileConfiguration config;
	
	private boolean initialised = false;
	
	public Loadable(String name) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
	}
	
	public int compareTo(Loadable loadable) {
		return getName().compareTo(loadable.getName());
	}
	
	public final ClassLoader getClassLoader() {
		return loader;
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public File getDataFolder() {
		return dataFolder;
	}
	
	protected final File getFile() {
		return file;
	}
	
	public final String getName() {
		return name;
	}
	
	public InputStream getResource(String name) {
		return loader.getResourceAsStream(name);
	}
	
	public InitResult init() {
		return new InitResult(true);
	}
	
	protected final void initialise(ClassLoader loader, File file, File dataFolder) {
		if (initialised)
			return;
		
		this.loader = loader;
		this.file = file;
		this.dataFolder = dataFolder;
		this.initialised = true;
	}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(dataFolder, "config.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = getResource("config.yml");
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) { plugin.log(Level.SEVERE, "Failed to save to " + configFile); }
	}
	
	public final class InitResult {
		
		private final boolean success;
		private final String message;
		
		public InitResult(boolean success) {
			this(success, "");
		}
		
		public InitResult(boolean success, String message) {
			this.success = success;
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		
		public boolean getResult() {
			return success;
		}
	}
}