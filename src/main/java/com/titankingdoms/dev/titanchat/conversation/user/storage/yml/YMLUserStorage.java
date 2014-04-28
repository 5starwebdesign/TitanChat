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

package com.titankingdoms.dev.titanchat.conversation.user.storage.yml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.conversation.user.storage.UserData;
import com.titankingdoms.dev.titanchat.conversation.user.storage.UserStorage;

public final class YMLUserStorage implements UserStorage {
	
	private final TitanChat plugin;
	
	private static final String NAME = "YMLStorage";
	
	private File ymlFile;
	private FileConfiguration yml;
	
	public YMLUserStorage() {
		this.plugin = TitanChat.instance();
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public FileConfiguration getYML() {
		if (yml == null)
			reloadYML();
		
		return yml;
	}
	
	@Override
	public void loadData(UserData data) {
		Validate.notNull(data, "Data cannot be null");
		
		String id = data.getUniqueId().toString();
		
		reloadYML();
		
		if (!getYML().isConfigurationSection(id))
			getYML().createSection(id);
		
		ConfigurationSection user = getYML().getConfigurationSection(id);
		
		data.setViewing(user.getString("connections.viewing", ""));
		
		data.setConnected(user.getStringList("connections.connected"));
		
		ConfigurationSection metaSection = user.getConfigurationSection("metadata");
		
		if (metaSection != null) {
			Map<String, String> metadata = new HashMap<String, String>();
			
			for (String key : metaSection.getKeys(false))
				metadata.put(key, metaSection.getString(key));
			
			data.setMetadata(metadata);
		}
	}
	
	public void reloadYML() {
		if (ymlFile == null)
			ymlFile = new File(plugin.getDataFolder(), "users.yml");
		
		yml = YamlConfiguration.loadConfiguration(ymlFile);
	}
	
	@Override
	public void saveData(UserData data) {
		Validate.notNull(data, "Data cannot be null");
		
		String id = data.getUniqueId().toString();
		
		getYML().createSection(id);
		
		ConfigurationSection user = getYML().getConfigurationSection(id);
		
		user.set("connections.viewing", data.getViewing());
		
		user.set("connections.connected", new ArrayList<String>(data.getConnected()));
		
		user.createSection("metadata", data.getMetadata());
		
		saveYML();
	}
	
	public void saveYML() {
		if (yml == null || ymlFile == null)
			return;
		
		try { yml.save(ymlFile); } catch (Exception e) {}
	}
}