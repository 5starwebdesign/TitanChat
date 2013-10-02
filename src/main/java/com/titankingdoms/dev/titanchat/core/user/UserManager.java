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

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.Point;
import com.titankingdoms.dev.titanchat.api.event.user.UserRegistrationEvent;
import com.titankingdoms.dev.titanchat.api.event.user.UserUnregistrationEvent;
import com.titankingdoms.dev.titanchat.core.conversation.Provider;

public final class UserManager implements Manager<User>, Provider {
	
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
		return (has(name)) ? getOnlineUser(name) : getOfflineUser(name);
	}
	
	@Override
	public Collection<User> getAll() {
		return new HashSet<User>(users.values());
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public Console getConsole() {
		return (Console) get("CONSOLE");
	}
	
	@Override
	public String getName() {
		return "UserManager";
	}
	
	public User getOfflineUser(String name) {
		try { return new User(name); } catch (Exception e) { return null; }
	}
	
	public User getOnlineUser(String name) {
		if (!has(name) && plugin.getServer().getPlayerExact(name) != null)
			registerAll(new Participant(plugin.getServer().getPlayerExact(name)));
		
		return users.get(name.toLowerCase());
	}
	
	@Override
	public Collection<Point> getPoints(String... names) {
		if (names == null)
			return new HashSet<Point>();
		
		Set<Point> points = new HashSet<Point>();
		
		for (String name : names) {
			if (name == null || !has(name))
				continue;
			
			points.add(get(name));
		}
		
		return points;
	}
	
	@Override
	public String getType() {
		return "User";
	}
	
	public User getUser(CommandSender sender) {
		if (has(sender.getName()))
			return get(sender.getName());
		
		if (sender instanceof Player)
			registerAll(new Participant((Player) sender));
		
		return get(sender.getName());
	}
	
	public Collection<User> getUsers() {
		return getAll();
	}
	
	@Override
	public boolean has(String name) {
		return (isValid(name)) ? users.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(User user) {
		return (user != null && has(user.getName())) ? get(user.getName()).equals(user) : false;
	}
	
	public boolean hasUser(String name) {
		return has(name);
	}
	
	public boolean hasUser(User user) {
		return has(user);
	}
	
	@Override
	public void init() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		users.put("console", new Console());
		plugin.getServer().getPluginManager().callEvent(new UserRegistrationEvent(getConsole()));
	}
	
	public boolean isValid(String name) {
		if (name == null || name.isEmpty())
			return false;
		
		return (Pattern.compile("\\W").matcher(name).find() && name.length() > 16);
	}
	
	@Override
	public void load() {
		for (Player player : plugin.getServer().getOnlinePlayers())
			registerAll(new Participant(player));
	}
	
	private void loadUser(User user) {
		if (user == null)
			return;
		
		user.load();
	}
	
	public void loadUser(String name) {
		if (name == null)
			return;
		
		loadUser(get(name));
	}
	
	public void loadUsers() {
		for (User user : getAll())
			loadUser(user);
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
			if (user == null || !user.isOnline() || user.getName().equals("CONSOLE"))
				continue;
			
			if (has(user.getName())) {
				plugin.log(Level.WARNING, "Duplicate: " + user);
				continue;
			}
			
			this.users.put(user.getName().toLowerCase(), user);
			plugin.getServer().getPluginManager().callEvent(new UserRegistrationEvent(user));
		}
	}
	
	@Override
	public void reload() {
		reloadConfig();
		
		for (User user : getAll())
			unregister(user);
		
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
	
	private void saveUser(User user) {
		if (user == null)
			return;
		
		user.save();
	}
	
	public void saveUser(String name) {
		if (name == null)
			return;
		
		saveUser(get(name));
	}
	
	public void saveUsers() {
		for (User user : getAll())
			saveUser(user);
	}
	
	@Override
	public void unload() {
		for (User user : getAll()) {
			unregister(user);
			user.save();
		}
	}
	
	@Override
	public void unregister(User user) {
		if (user == null || !user.isOnline() || !has(user) || user.getName().equals("CONSOLE"))
			return;
		
		this.users.remove(user.getName().toLowerCase());
		plugin.getServer().getPluginManager().callEvent(new UserUnregistrationEvent(user));
	}
}