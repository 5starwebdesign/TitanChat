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

package com.titankingdoms.dev.titanchat.core.user;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.Manager;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.user.console.Console;
import com.titankingdoms.dev.titanchat.core.user.participant.Participant;

public final class UserManager implements Manager<User> {
	
	private final TitanChat plugin;
	
	private final Map<String, User> users;
	
	private File configFile;
	private FileConfiguration config;
	
	public UserManager() {
		this.plugin = TitanChat.getInstance();
		this.users = new HashMap<String, User>();
	}
	
	@Override
	public User get(String name) {
		if (name == null || Pattern.compile("\\W").matcher(name).find())
			return null;
		
		return (has(name)) ? users.get(name.toLowerCase()) : new OfflineUser(name);
	}
	
	@Override
	public List<User> getAll() {
		return new ArrayList<User>(users.values());
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public Console getConsole() {
		return (Console) get("CONSOLE");
	}
	
	public User getUser(CommandSender sender) {
		return get(sender.getName());
	}
	
	public List<User> getUsers() {
		return getAll();
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? users.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(User user) {
		if (user == null || !has(user.getName()))
			return false;
		
		return get(user.getName()).equals(user);
	}
	
	public boolean hasUser(String name) {
		return has(name);
	}
	
	public boolean hasUser(User user) {
		return has(user);
	}
	
	@Override
	public void load() {
		registerAll(new Console());
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			registerAll(new Participant(player));
	}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return new ArrayList<String>(users.keySet());
		
		if (Pattern.compile("\\W").matcher(name).find())
			return new ArrayList<String>();
		
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
	public void registerAll(User... users) {
		if (users == null)
			return;
		
		for (User user : users) {
			if (user == null)
				continue;
			
			if (Pattern.compile("\\W").matcher(user.getName()).find()) {
				plugin.log(Level.WARNING, "Invalid user name: " + user.getName());
				continue;
			}
			
			if (has(user)) {
				plugin.log(Level.WARNING, "Duplicate: " + user);
				continue;
			}
			
			this.users.put(user.getName().toLowerCase(), user);
		}
	}
	
	@Override
	public void reload() {
		for (User user : getAll())
			unregister(user);
		
		registerAll(new Console());
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			registerAll(new Participant(player));
	}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "users.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("users.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
	
	@Override
	public void unload() {
		for (User user : getAll())
			unregister(user);
	}
	
	@Override
	public void unregister(User user) {
		if (user == null || !has(user))
			return;
		
		this.users.remove(user.getName().toLowerCase());
	}
}