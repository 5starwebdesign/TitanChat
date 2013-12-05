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

package com.titankingdoms.dev.titanchat.api.user;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang.Validate;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.conversation.Provider;
import com.titankingdoms.dev.titanchat.api.user.meta.Metadata;
import com.titankingdoms.dev.titanchat.user.Block;
import com.titankingdoms.dev.titanchat.user.Console;

public final class UserManager implements Manager<User>, Provider<User> {
	
	private final TitanChat plugin;
	
	private File configFile;
	private FileConfiguration config;
	
	private final Map<String, User> users;
	
	private final Console console;
	
	public UserManager() {
		this.plugin = TitanChat.getInstance();
		this.users = new HashMap<String, User>();
		this.console = new Console(plugin.getServer().getConsoleSender());
	}
	
	@Override
	public User get(String name) {
		return (has(name)) ? users.get(name.toLowerCase()) : null;
	}
	
	@Override
	public Set<User> getAll() {
		return new HashSet<User>(users.values());
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	@Override
	public String getName() {
		return "UserManager";
	}
	
	public User getUser(String name) {
		return get(name);
	}
	
	public User getUser(CommandSender sender) {
		Validate.notNull(sender, "Sender cannot be null");
		
		if (sender instanceof ConsoleCommandSender)
			return console;
		
		if (sender instanceof BlockCommandSender)
			return new Block((BlockCommandSender) sender);
		
		return get(sender.getName());
	}
	
	@Override
	public boolean has(String name) {
		return (name != null && !name.isEmpty()) ? users.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(User user) {
		return (user != null && has(user.getName())) ? get(user.getName()).equals(user) : false;
	}
	
	@Override
	public void load() {}
	
	public Metadata loadMetadata(User user) {
		Validate.notNull(user, "User cannot be null");
		
		Metadata metadata = new Metadata(user);
		
		
		
		return metadata;
	}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return new ArrayList<String>(users.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String user : users.keySet()) {
			if (!user.startsWith(name.toLowerCase()))
				continue;
			
			matches.add(user);
		}
		
		Collections.sort(matches);
		
		return matches;
	}
	
	@Override
	public void register(User user) {}
	
	@Override
	public void reload() {}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "users.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("users.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public void saveConfig() {
		if (config == null || configFile == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
	
	public void saveMetadata(Metadata metadata) {
		Validate.notNull(metadata, "Metadata cannot be null");
	}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(User user) {}
}