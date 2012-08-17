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

package com.titankingdoms.nodinchan.titanchat.channel.custom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;

public class CustomChannel extends Channel {
	
	private File configFile;
	private FileConfiguration config;
	
	public CustomChannel(String name) {
		super(name, Option.CUSTOM);
	}
	
	@Override
	public boolean access(Player player) {
		return true;
	}
	
	@Override
	public final Channel create(CommandSender sender, String name, Option option) {
		return this;
	}
	
	/**
	 * Gets the custom config of the custom channel
	 * 
	 * @return The custom config
	 */
	public FileConfiguration getCustomConfig() {
		if (config == null)
			reloadCustomConfig();
		
		return config;
	}

	@Override
	public final String getType() {
		return "Custom";
	}

	@Override
	public final Channel load(String name, Option option) {
		return this;
	}
	
	/**
	 * Reloads the custom config of the custom channel
	 */
	public void reloadCustomConfig() {
		if (configFile == null)
			configFile = new File(getDataFolder(), "config.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = getResource("config.yml");
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	/**
	 * Saves the custom config of the custom channel
	 */
	public void saveCustomConfig() {
		try { config.save(configFile); } catch (IOException e) { plugin.log(Level.SEVERE, "Failed to save custom config to " + configFile);}
	}
}