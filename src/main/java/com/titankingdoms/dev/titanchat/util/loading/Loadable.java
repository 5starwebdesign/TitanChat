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

package com.titankingdoms.dev.titanchat.util.loading;

import java.io.File;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import com.titankingdoms.dev.titanchat.TitanChat;

/**
 * {@link Loadable} - Loadable by the {@link Loader}
 * 
 * @author NodinChan
 *
 */
public abstract class Loadable implements Comparable<Loadable>, Listener {
	
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
	
	/**
	 * Gets the {@link ClassLoader} that loaded the {@link Loadable}
	 * 
	 * @return The {@link ClassLoader}
	 */
	public final ClassLoader getClassLoader() {
		return loader;
	}
	
	/**
	 * Gets the configuration
	 * 
	 * @return The configuration
	 */
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	/**
	 * Gets the configuration {@link File}
	 * 
	 * @return The {@link File} with the configuration
	 */
	public File getConfigFile() {
		return new File(getDataFolder(), "config.yml");
	}
	
	/**
	 * Gets the data folder of the {@link Loadable}
	 * 
	 * @return The data folder
	 */
	public File getDataFolder() {
		return dataFolder;
	}
	
	/**
	 * Gets the {@link InputStream} for the default configuration
	 * 
	 * @return The {@link InputStream} for default configuration
	 */
	public InputStream getDefaultConfigStream() {
		return getResource("config.yml");
	}
	
	/**
	 * Gets the Jar {@link File} that the {@link Loadable} was loaded from
	 * 
	 * @return The Jar {@link File}
	 */
	protected final File getFile() {
		return file;
	}
	
	/**
	 * Gets the name of the {@link Loadable}
	 * 
	 * @return The name
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Gets the resource from the {@link JarFile} of the {@link Loadable}
	 * 
	 * @param name The name of the resource
	 * 
	 * @return The {@link InputStream} of the resource if found, otherwise null
	 */
	public InputStream getResource(String name) {
		return loader.getResourceAsStream(name);
	}
	
	/**
	 * Called after initialising the {@link Loadable}
	 * 
	 * @return The {@link InitResult}
	 */
	public InitResult onInitialise() {
		return new InitResult(true);
	}
	
	/**
	 * Called to initialise the {@link Loadable}
	 * 
	 * @param loader The {@link ClassLoader} that loaded the {@link Loadable}
	 * 
	 * @param file The {@link File} that the {@link Loadable} was loaded from
	 * 
	 * @param dataFolder The data folder of the {@link Loadable
	 */
	protected final void initialise(ClassLoader loader, File file, File dataFolder) {
		if (initialised)
			return;
		
		this.loader = loader;
		this.file = file;
		this.dataFolder = dataFolder;
		this.initialised = true;
	}
	
	/**
	 * Registers the {@link Listener}
	 * 
	 * @param listener The {@link Listener} to register
	 */
	public final void registerListener(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	
	/**
	 * Reloads the configuration
	 */
	public void reloadConfig() {
		if (configFile == null)
			configFile = getConfigFile();
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = getDefaultConfigStream();
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	/**
	 * Saves the configuration
	 */
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
	
	/**
	 * {@link InitResult} - Result of initialisation
	 * 
	 * @author NodinChan
	 *
	 */
	public final class InitResult {
		
		private final boolean success;
		private final String message;
		
		public InitResult(boolean success) {
			this(success, "");
		}
		
		public InitResult(boolean success, String message) {
			this.success = success;
			this.message = (message != null) ? message : "";
		}
		
		/**
		 * Gets the message of the result
		 * 
		 * @return The message
		 */
		public String getMessage() {
			return message;
		}
		
		/**
		 * Gets the result of the the initialisation
		 * 
		 * @return True if successful
		 */
		public boolean isSuccessful() {
			return success;
		}
	}
}