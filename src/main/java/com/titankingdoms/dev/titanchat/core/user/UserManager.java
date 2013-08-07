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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.Manager;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.user.console.Console;

public final class UserManager implements Manager<User> {
	
	private final TitanChat plugin;
	
	private final Map<String, User> users;
	
	public UserManager() {
		this.plugin = TitanChat.getInstance();
		this.users = new HashMap<String, User>();
	}
	
	@Override
	public User get(String name) {
		return (name != null) ? users.get(name.toLowerCase()) : null;
	}
	
	@Override
	public List<User> getAll() {
		return new ArrayList<User>(users.values());
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
		
	}
	
	@Override
	public void registerAll(User... users) {
		if (users == null)
			return;
		
		for (User user : users) {
			if (user == null)
				continue;
			
			if (has(user)) {
				plugin.log(Level.WARNING, "Duplicate: " + user);
				continue;
			}
			
			this.users.put(user.getName().toLowerCase(), user);
		}
	}
	
	@Override
	public void reload() {
		
	}
	
	@Override
	public void unload() {
		
	}
	
	@Override
	public void unregister(User user) {
		if (user == null || !has(user))
			return;
		
		this.users.remove(user.getName().toLowerCase());
	}
}