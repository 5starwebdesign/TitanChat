/*
 *     Copyright (C) 2013  Nodin Chan
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

package com.titankingdoms.dev.titanchat.channel;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.EndPoint;

public abstract class Channel implements EndPoint {
	
	protected final TitanChat plugin;
	
	private final String name;
	private final String factory;
	
	private File configFile;
	private FileConfiguration config;
	
	private final Set<String> blacklist;
	private final Set<String> whitelist;
	private final Set<String> operators;
	
	public Channel(String name, String factory) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(StringUtils.isAlphanumeric(name), "Name cannot contain non-alphanumeric chars");
		Validate.notEmpty(factory, "Factory cannot be empty");
		Validate.isTrue(StringUtils.isAlphanumeric(factory), "Factory cannot contain non-alphanumeric chars");
		
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.factory = factory;
		this.blacklist = new HashSet<String>();
		this.whitelist = new HashSet<String>();
		this.operators = new HashSet<String>();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Channel)
			return toString().equals(object.toString());
		
		return false;
	}
	
	public Set<String> getBlacklist() {
		return blacklist;
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public final String getFactory() {
		return factory;
	}
	
	@Override
	public String getFormat() {
		return "";
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	public Set<String> getOperators() {
		return operators;
	}
	
	@Override
	public final String getType() {
		return "Channel";
	}
	
	public Set<String> getWhitelist() {
		return whitelist;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public void linkPoint(EndPoint point) {}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getManager(ChannelManager.class).getDirectory(), getName() + ".yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("channel.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
	
	@Override
	public String toString() {
		return "Channel: {" +
				"name: " + getName() +
				"}";
	}
	
	@Override
	public void unlinkPoint(EndPoint point) {}
}