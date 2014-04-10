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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.user.storage.UserInfo;
import com.titankingdoms.dev.titanchat.api.user.storage.UserInfoStorage;

public final class YMLUserInfoStorage implements UserInfoStorage {
	
	private final TitanChat plugin;
	
	private File ymlFile;
	private FileConfiguration yml;
	
	private final Map<String, UserInfo> info;
	
	public YMLUserInfoStorage() {
		this.plugin = TitanChat.instance();
		this.info = new HashMap<String, UserInfo>();
	}
	
	@Override
	public UserInfo get(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		
		if (!info.containsKey(name.toLowerCase()))
			return revise(name);
		
		return info.get(name.toLowerCase());
	}
	
	@Override
	public String getName() {
		return "YML";
	}
	
	@Override
	public void load() {}
	
	public FileConfiguration getYML() {
		if (yml == null)
			reloadYML();
		
		return yml;
	}
	
	@Override
	public void reload() {}
	
	public void reloadYML() {
		if (ymlFile == null)
			ymlFile = new File(plugin.getDataFolder(), "users.yml");
		
		yml = YamlConfiguration.loadConfiguration(ymlFile);
	}
	
	@Override
	public UserInfo revise(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		
		if (!getYML().contains(name.toLowerCase()))
			throw new UnsupportedOperationException("Unable to revise without YML entry");
		
		YMLUserInfo info = new YMLUserInfo(getYML().getConfigurationSection(name.toLowerCase()));
		this.info.put(name.toLowerCase(), info);
		return info;
	}
	
	@Override
	public void save() {}
	
	@Override
	public void save(UserInfo info) {
		ConfigurationSection user = getYML().createSection(info.getName().toLowerCase());
		
		String[] viewing = info.getViewing().split("::");
		
		user.set("connections.viewing.name", viewing[0]);
		user.set("connections.viewing.type", viewing[1]);
		
		user.set("connections.connected", info.getConnected());
		
		for (Entry<String, String> metadata : info.getMetadata().entrySet())
			user.set("metadata." + metadata.getKey(), metadata.getValue());
		
		saveYML();
	}
	
	public void saveYML() {
		if (ymlFile == null || yml == null)
			return;
		
		try { yml.save(ymlFile); } catch (Exception e) {}
	}
	
	public final class YMLUserInfo extends UserInfo {
		
		public YMLUserInfo(ConfigurationSection user) {
			super((user != null) ? user.getName() : "");
			
			String vName = user.getString("connections.viewing.name", "");
			String vType = user.getString("connections.viewing.type", "");
			
			if (!vName.isEmpty() && !vType.isEmpty())
				this.viewing = vName + "::" + vType;
			
			List<String> connected = user.getStringList("connections.connected");
			
			if (connected != null) {
				for (String node : connected) {
					String[] cNode = node.split("::");
					
					if (cNode.length < 2)
						continue;
					
					this.connected.add(cNode[0] + "::" + cNode[1]);
				}
			}
			
			ConfigurationSection metadata = user.getConfigurationSection("metadata");
			
			if (metadata != null) {
				for (String key : metadata.getKeys(false))
					this.metadata.put(key, metadata.getString(key));
			}
		}
	}
}