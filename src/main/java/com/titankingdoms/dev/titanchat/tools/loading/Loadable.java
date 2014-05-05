/*
 *     Copyright (C) 2014  Nodin Chan
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
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.tools.loading;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.titankingdoms.dev.titanchat.TitanChat;

public class Loadable implements Comparable<Loadable>, Listener {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private ClassLoader loader;
	private File file;
	private File dataFolder;
	
	private File configFile;
	private FileConfiguration config;
	
	private boolean initialised;
	
	public Loadable(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(StringUtils.isAlphanumeric(name), "Name cannot contain non-alphanumeric chars");
		
		this.plugin = TitanChat.instance();
		this.name = name;
		this.initialised = false;
	}
	
	protected final void init(ClassLoader loader, File file, File dataFolder) {
		if (initialised)
			return;
		
		this.loader = loader;
		this.file = file;
		this.dataFolder = dataFolder;
		this.initialised = true;
	}
	
	@Override
	public int compareTo(Loadable loadable) {
		return getName().compareTo((loadable != null) ? loadable.getName() : "");
	}
	
	public final ClassLoader getClassLoader() {
		return loader;
	}
	
	public final FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public File getConfigFile() {
		return new File(getDataFolder(), "config.yml");
	}
	
	public File getDataFolder() {
		return dataFolder;
	}
	
	public InputStream getDefaultConfigStream() {
		return getResource("config.yml");
	}
	
	public final File getFile() {
		return file;
	}
	
	public final String getName() {
		return name;
	}
	
	public final InputStream getResource(String name) {
		return loader.getResourceAsStream(name);
	}
	
	public final boolean isInitialised() {
		return initialised;
	}
	
	public void onLoad() {}
	
	public final void registerListener(Listener listener, Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	
	public final void registerListener(Plugin plugin) {
		registerListener(this, plugin);
	}
	
	protected final void registerListener(Listener listener) {
		registerListener(listener, plugin);
	}
	
	protected final void registerListener() {
		registerListener(this, plugin);
	}
	
	public final void reloadConfig() {
		if (configFile == null)
			configFile = getConfigFile();
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = getDefaultConfigStream();
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public final void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
}