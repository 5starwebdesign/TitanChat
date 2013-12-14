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

package com.titankingdoms.dev.titanchat.user.storage;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.user.storage.UserSection;
import com.titankingdoms.dev.titanchat.api.user.storage.UserStorage;

public final class YMLUserStorage implements UserStorage {
	
	private final TitanChat plugin;
	
	private File ymlFile;
	private FileConfiguration yml;
	
	public YMLUserStorage() {
		this.plugin = TitanChat.getInstance();
	}
	
	@Override
	public String getName() {
		return "YML";
	}
	
	public FileConfiguration getYML() {
		if (yml == null)
			reloadYML();
		
		return yml;
	}
	
	@Override
	public void load() {}
	
	@Override
	public UserSection loadSection(String name) {
		UserSection user = new UserSection(name);
		
		if (!getYML().contains(name))
			return user;
		
		ConfigurationSection section = getYML().getConfigurationSection(name);
		
		if (section.contains("conversation")) {
			ConfigurationSection conversation = section.getConfigurationSection("conversation");
			
			if (conversation.contains("current")) {
				ConfigurationSection current = conversation.getConfigurationSection("current");
				
				if (current.contains("name") && current.contains("type")) {
					String nName = current.getString("name");
					String nType = current.getString("type");
					
					if (!nName.isEmpty() && !nType.isEmpty())
						user.setCurrentNode(nName, nType);
				}
			}
			
			if (conversation.contains("nodes")) {
				ConfigurationSection nodes = conversation.getConfigurationSection("nodes");
				
				for (String nName : nodes.getKeys(false)) {
					String nType = nodes.getString(nName);
					
					if (!nType.isEmpty())
						user.addNode(nName, nType);
				}
			}
		}
		
		if (section.contains("metadata")) {
			ConfigurationSection metadata = section.getConfigurationSection("metadata");
			
			for (String key : metadata.getKeys(false))
				user.setMetadata(key, metadata.getString(key));
		}
		
		return user;
	}
	
	@Override
	public void reload() {
		reloadYML();
	}
	
	public void reloadYML() {
		if (ymlFile == null)
			ymlFile = new File(plugin.getDataFolder(), "users.yml");
		
		yml = YamlConfiguration.loadConfiguration(ymlFile);
	}
	
	@Override
	public void save() {
		saveYML();
	}
	
	@Override
	public void saveSampleStorage() {}
	
	public void saveYML() {
		if (ymlFile == null || yml == null)
			return;
		
		try { yml.save(ymlFile); } catch (Exception e) {}
	}
	
	@Override
	public void saveSection(UserSection section) {}
}