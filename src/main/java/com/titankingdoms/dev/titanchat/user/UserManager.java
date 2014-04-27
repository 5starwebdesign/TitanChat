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

package com.titankingdoms.dev.titanchat.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.conversation.NodeManager;
import com.titankingdoms.dev.titanchat.user.storage.UserStorage;
import com.titankingdoms.dev.titanchat.user.storage.yml.YMLUserStorage;

public final class UserManager implements Manager<User>, NodeManager<User> {
	
	private final TitanChat plugin;
	
	private static final String NAME = "UserManager";
	private static final String TYPE = "User";
	
	private static final Set<String> DEPENDENCIES = ImmutableSet.<String>builder().add("Network").build();
	
	private final Map<UUID, User> users;
	
	private UserStorage storage;
	
	public UserManager() {
		this.plugin = TitanChat.instance();
		this.users = new HashMap<UUID, User>();
		this.storage = new YMLUserStorage();
	}
	
	public User get(UUID id) {
		return (has(id)) ? users.get(id) : new User(plugin.getServer().getOfflinePlayer(id));
	}
	
	@Override
	public User get(String id) {
		try { return get(UUID.fromString(id)); } catch (Exception e) { return null; }
	}
	
	@Override
	public Collection<User> getAll() {
		return ImmutableSet.<User>builder().addAll(users.values()).build();
	}
	
	@Override
	public Collection<String> getDependencies() {
		return DEPENDENCIES;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public UserStorage getStorage() {
		if (storage == null)
			throw new IllegalStateException("No storage method");
		
		return storage;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	public boolean has(UUID id) {
		return id != null && users.containsKey(id);
	}
	
	@Override
	public boolean has(String id) {
		try { return has(UUID.fromString(id)); } catch (Exception e) { return false; }
	}
	
	@Override
	public boolean has(User user) {
		return user != null && has(user.getUniqueId()) && get(user.getUniqueId()).equals(user);
	}
	
	@Override
	public void load() {}
	
	@Override
	public Collection<String> match(String name) {
		List<String> matches = new ArrayList<String>();
		
		for (User user : users.values()) {
			if ((name == null || name.isEmpty()) && !user.getName().startsWith(name))
				continue;
			
			matches.add(user.getName());
		}
		
		Collections.sort(matches);
		
		return matches;
	}
	
	@Override
	public void register(User user) {
		Validate.notNull(user, "User cannot be null");
		
		if (has(user.getUniqueId()))
			return;
		
		users.put(user.getUniqueId(), user);
	}
	
	@Override
	public void reload() {}
	
	public void setStorage(UserStorage storage) {
		this.storage = storage;
	}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(User user) {
		Validate.notNull(user, "User cannot be null");
		
		if (!has(user.getUniqueId()))
			return;
		
		users.remove(user.getUniqueId());
	}
}