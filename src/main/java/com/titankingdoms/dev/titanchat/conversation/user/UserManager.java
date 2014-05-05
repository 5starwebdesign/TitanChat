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

package com.titankingdoms.dev.titanchat.conversation.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList.Builder;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.AbstractModule;
import com.titankingdoms.dev.titanchat.api.conversation.NodeManager;
import com.titankingdoms.dev.titanchat.conversation.user.storage.UserStorage;
import com.titankingdoms.dev.titanchat.conversation.user.storage.yml.YMLUserStorage;
import com.titankingdoms.dev.titanchat.tools.metadata.DataConversionHandler;

public final class UserManager extends AbstractModule implements NodeManager<User> {
	
	private final TitanChat plugin;
	
	private static final String TYPE = "User";
	
	private static final String[] DEPENDENCIES = new String[] { "Network" };
	
	private final Map<UUID, User> users;
	
	private final DataConversionHandler dataHandler;
	
	private UserStorage storage;
	
	public UserManager() {
		super("UserManager");
		this.plugin = TitanChat.instance();
		this.users = new HashMap<>();
		this.dataHandler = new DataConversionHandler();
		this.storage = new YMLUserStorage();
	}
	
	public User get(UUID id) {
		if (id == null)
			return null;
		
		if (!has(id)) {
			User user = new User(plugin.getServer().getOfflinePlayer(id));
			
			if (!user.isOnline())
				return user;
			
			register(user);
		}
		
		return users.get(id);
	}
	
	@Override
	public User get(String id) {
		try { return get(UUID.fromString(id)); } catch (Exception e) { return null; }
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
	
	public boolean has(UUID id) {
		return id != null && users.containsKey(id);
	}
	
	@Override
	public boolean has(String id) {
		try { return has(UUID.fromString(id)); } catch (Exception e) { return false; }
	}
	
	public boolean has(User user) {
		return user != null && has(user.getUniqueId()) && get(user.getUniqueId()).equals(user);
	}
	
	@Override
	public void load() {}
	
	public List<String> match(String name) {
		Builder<String> matches = ImmutableList.builder();
		
		for (User user : users.values()) {
			if (name != null && !user.getName().startsWith(name))
				continue;
			
			matches.add(user.getName());
		}
		
		return matches.build();
	}
	
	@Override
	public void register(User user) {
		Validate.notNull(user, "User cannot be null");
		Validate.isTrue(!has(user.getUniqueId()), "User already registered");
		
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
		Validate.isTrue(has(user.getUniqueId()), "User not registered");
		
		users.remove(user.getUniqueId());
	}
}