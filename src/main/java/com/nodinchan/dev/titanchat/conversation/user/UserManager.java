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

package com.nodinchan.dev.titanchat.conversation.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.nodinchan.dev.conversation.NodeManager;
import com.nodinchan.dev.metadata.DataConversionHandler;
import com.nodinchan.dev.module.AbstractModule;
import com.nodinchan.dev.titanchat.TitanChat;
import com.nodinchan.dev.titanchat.conversation.user.storage.UserStorage;
import com.nodinchan.dev.titanchat.conversation.user.storage.yml.YMLUserStorage;

public final class UserManager extends AbstractModule implements NodeManager<User> {
	
	private final TitanChat plugin;
	
	private static final String TYPE = "User";
	
	private static final String[] DEPENDENCIES = new String[] { "Network" };
	
	private final Map<String, UUID> ids;
	private final Map<UUID, User> users;
	
	private final DataConversionHandler dataHandler;
	
	private UserStorage storage;
	
	public UserManager() {
		super("UserManager");
		this.plugin = TitanChat.instance();
		this.ids = new HashMap<>();
		this.users = new HashMap<>();
		this.dataHandler = new DataConversionHandler();
		this.storage = new YMLUserStorage();
	}
	
	public User get(UUID id) {
		if (id == null)
			return null;
		
		if (!isRegistered(id)) {
			User user = new User(plugin.getServer().getOfflinePlayer(id));
			
			if (!user.isOnline())
				return user;
			
			register(user);
		}
		
		return users.get(id);
	}
	
	@Override
	public User get(String name) {
		return (name == null || name.isEmpty()) ? null : get(ids.get(name.toLowerCase()));
	}
	
	@Override
	public Set<User> getAll() {
		return ImmutableSet.copyOf(users.values());
	}
	
	public DataConversionHandler getDataHandler() {
		return dataHandler;
	}
	
	@Override
	public String[] getDependencies() {
		return DEPENDENCIES;
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
	
	@Override
	public boolean has(String name) {
		return isOnline(name);
	}
	
	public boolean isOnline(String name) {
		return name != null && !name.isEmpty() && ids.containsKey(name.toLowerCase());
	}
	
	public boolean isRegistered(UUID id) {
		return id != null && users.containsKey(id);
	}
	
	@Override
	public void load() {}
	
	public Set<String> match(String name) {
		if (name == null || name.isEmpty())
			return ImmutableSet.copyOf(ids.keySet());
		
		Builder<String> matches = ImmutableSet.builder();
		
		for (String user : ids.keySet()) {
			if (!user.startsWith(name))
				continue;
			
			matches.add(user);
		}
		
		return matches.build();
	}
	
	@Override
	public void register(User user) {
		Validate.notNull(user, "User cannot be null");
		Validate.isTrue(!isRegistered(user.getUniqueId()), "User already registered");
		Validate.isTrue(user.isOnline(), "User cannot be offline");
		
		users.put(user.getUniqueId(), user);
		ids.put(user.getName().toLowerCase(), user.getUniqueId());
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
		Validate.isTrue(isRegistered(user.getUniqueId()), "User not registered");
		Validate.isTrue(user.isOnline(), "User cannot be offline");
		
		users.remove(user.getUniqueId());
		ids.remove(user.getName().toLowerCase());
	}
}