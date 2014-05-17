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
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.nodinchan.dev.conversation.NodeManager;
import com.nodinchan.dev.metadata.DataConversionHandler;
import com.nodinchan.dev.module.AbstractModule;
import com.nodinchan.dev.titanchat.TitanChat;
import com.nodinchan.dev.titanchat.api.conversation.SimpleNetwork;
import com.nodinchan.dev.titanchat.conversation.user.storage.UserData;
import com.nodinchan.dev.titanchat.conversation.user.storage.UserStorage;
import com.nodinchan.dev.titanchat.conversation.user.storage.yml.YMLUserStorage;

public final class UserManager extends AbstractModule implements NodeManager<User> {
	
	private final TitanChat plugin;
	
	private static final String TYPE = "User";
	
	private static final String[] DEPENDENCIES = new String[] { "Network" };
	
	private final Map<String, UUID> ids;
	private final Map<UUID, User> users;
	
	private final DataConversionHandler conversion;
	
	private UserStorage storage;
	
	public UserManager() {
		super("UserManager");
		this.plugin = TitanChat.instance();
		this.ids = new HashMap<>();
		this.users = new HashMap<>();
		this.conversion = new DataConversionHandler();
		this.storage = new YMLUserStorage();
	}
	
	@Override
	public User get(String name) {
		return getByName(name);
	}
	
	@Override
	public Set<User> getAll() {
		return ImmutableSet.copyOf(users.values());
	}
	
	public User getByName(String name) {
		return (name == null || name.isEmpty()) ? null : getByUniqueId(ids.get(name.toLowerCase()));
	}
	
	public User getByUniqueId(UUID id) {
		if (id == null)
			return null;
		
		if (!isRegistered(id)) {
			User user = new User(id);
			
			if (!user.isOnline())
				return user;
			
			register(user);
		}
		
		return users.get(id);
	}
	
	public DataConversionHandler getDataHandler() {
		return conversion;
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
		return name != null && !name.isEmpty() && ids.containsKey(name.toLowerCase());
	}
	
	public boolean isRegistered(UUID id) {
		return id != null && users.containsKey(id);
	}
	
	public boolean isRegistered(OfflinePlayer player) {
		return player != null && isRegistered(player.getUniqueId());
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
	
	public void onJoin(Player player) {
		Validate.notNull(player, "Player cannot be null");
		Validate.isTrue(!isRegistered(player.getUniqueId()), "Player already registered");
		Validate.isTrue(plugin.getSystem().isLoaded(SimpleNetwork.class), "Network not found");
		
		SimpleNetwork network = plugin.getSystem().getModule(SimpleNetwork.class);
		
		User user = new User(player.getUniqueId());
		
		UserData data = getStorage().loadData(user.getUniqueId().toString());
		
		String[] viewing = data.getViewing().split("::");
		
		if (network.hasNode(viewing[1], viewing[0]))
			user.setViewing(network.getNode(viewing[1], viewing[0]));
		
		for (String connected : data.getConnected()) {
			String[] node = connected.split("::");
			
			if (!network.hasNode(node[1], node[0]))
				continue;
			
			user.getConnection().connect(network.getNode(node[1], node[0]));
		}
		
		for (Entry<String, String> meta : data.getMetadata().entrySet())
			user.getMetadata().set(meta.getKey(), conversion.fromString(meta.getKey(), meta.getValue()));
		
		users.put(user.getUniqueId(), user);
	}
	
	public void onQuit(Player player) {
		Validate.notNull(player, "Player cannot be null");
		Validate.isTrue(isRegistered(player.getUniqueId()), "Player not registered");
		Validate.isTrue(plugin.getSystem().isLoaded(SimpleNetwork.class), "Network not found");
		
		getStorage().saveData(new UserData(users.remove(player.getUniqueId())));
	}
	
	@Override
	public void register(User user) {
		onJoin((user == null) ? null : user.getPlayer());
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
		onQuit((user == null) ? null : user.getPlayer());
	}
}