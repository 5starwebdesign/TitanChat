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

import java.util.*;

import org.apache.commons.lang.Validate;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.conversation.Provider;
import com.titankingdoms.dev.titanchat.api.user.storage.UserInfoStorage;
import com.titankingdoms.dev.titanchat.user.Block;
import com.titankingdoms.dev.titanchat.user.Console;

public final class UserManager implements Manager<User>, Provider<User> {
	
	private final TitanChat plugin;
	
	private final Map<String, User> users;
	
	private final Console console;
	
	private UserInfoStorage storage;
	
	private final Set<String> dependencies;
	
	public UserManager() {
		this.plugin = TitanChat.getInstance();
		this.users = new HashMap<String, User>();
		this.console = new Console(plugin.getServer().getConsoleSender());
		
		Set<String> dependencies = new HashSet<String>();
		dependencies.add("ProvisionManager");
		
		this.dependencies = Collections.unmodifiableSet(dependencies);
	}
	
	@Override
	public User get(String name) {
		return users.get(name.toLowerCase());
	}
	
	@Override
	public Set<User> getAll() {
		return new HashSet<User>(users.values());
	}
	
	@Override
	public Set<String> getDependencies() {
		return dependencies;
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
	
	public UserInfoStorage getStorage() {
		if (storage == null)
			throw new IllegalStateException("No storage method");
		
		return storage;
	}
	
	@Override
	public boolean has(String name) {
		return name != null && !name.isEmpty() && users.containsKey(name.toLowerCase());
	}
	
	@Override
	public boolean has(User user) {
		return user != null && has(user.getName()) && get(user.getName()).equals(user);
	}
	
	@Override
	public void load() {}
	
	public void load(User user) {}
	
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
	public void register(User user) {
		Validate.notNull(user, "User cannot be null");
		
		if (has(user.getName()))
			return;
		
		this.users.put(user.getName().toLowerCase(), user);
	}
	
	@Override
	public void reload() {}
	
	public void save(User user) {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(User user) {
		Validate.notNull(user, "User cannot be null");
		
		if (!has(user.getName()))
			return;
		
		this.users.remove(user.getName().toLowerCase());
	}
}