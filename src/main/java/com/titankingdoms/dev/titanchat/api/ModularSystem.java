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

package com.titankingdoms.dev.titanchat.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableList;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.addon.AddonManager;
import com.titankingdoms.dev.titanchat.api.event.ManagerEvent;

public final class ModularSystem {
	
	private AddonManager addon;
	
	private final Map<Class<? extends Manager<?>>, Manager<?>> managers = new LinkedHashMap<>();
	private final Map<Class<?>, Manager<?>> registered = new HashMap<>();
	
	public AddonManager getAddonManager() {
		if (addon == null)
			throw new IllegalStateException("AddonManager not found");
		
		return addon;
	}
	
	public <T extends Manager<?>> T getManager(Class<T> clazz) {
		return clazz.cast(managers.get(clazz));
	}
	
	public List<Manager<?>> getManagers() {
		return ImmutableList.copyOf(managers.values());
	}
	
	public List<Manager<?>> getRegisteredManagers() {
		synchronized (registered) {
			return ImmutableList.copyOf(registered.values());
		}
	}
	
	public <T extends Manager<?>> boolean isLoaded(Class<T> clazz) {
		return clazz != null && managers.containsKey(clazz);
	}
	
	public <T extends Manager<?>> boolean isRegistered(Class<T> clazz) {
		synchronized (registered) {
			return clazz != null && registered.containsKey(clazz);
		}
	}
	
	public <T extends Manager<?>> void loadManager(Class<T> clazz) {
		if (!isRegistered(clazz) || isLoaded(clazz))
			return;
		
		Manager<?> manager = getManager(clazz);
		
		for (Class<? extends Manager<?>> dependency : manager.getDependencies()) {
			if (dependency == null)
				continue;
			
			if (!isRegistered(dependency))
				return;
			
			loadManager(dependency);
		}
		
		manager.load();
		managers.put(clazz, manager);
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ManagerEvent(manager, "Load"));
	}
	
	public void loadManagers() {
		synchronized (registered) {
			Manager<?>[] registered = this.registered.values().toArray(new Manager<?>[0]);
			
			for (int manager = 0; manager < registered.length; manager++)
				loadManager(registered[manager].getClass());
		}
	}
	
	public void registerManager(Manager<?> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		
		if (isRegistered(manager.getClass()))
			return;
		
		registered.put(manager.getClass(), manager);
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ManagerEvent(manager, "Register"));
	}
	
	public void reload() {
		if (addon == null)
			this.addon = new AddonManager();
		
		addon.reload();
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ManagerEvent(addon, "Reload"));
		
		reloadManagers();
	}
	
	public <T extends Manager<?>> void reloadManager(Class<T> clazz) {
		if (!isRegistered(clazz))
			return;
		
		if (!isLoaded(clazz))
			loadManager(clazz);
		
		Manager<?> manager = getManager(clazz);
		
		manager.reload();
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ManagerEvent(manager, "Reload"));
	}
	
	public void reloadManagers() {
		for (Manager<?> manager : managers.values())
			reloadManager(manager.getClass());
	}
	
	public void start() {
		this.addon = new AddonManager();
		
		addon.load();
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ManagerEvent(addon, "Load"));
		
		loadManagers();
	}
	
	public void stop() {
		unloadManagers();
		
		addon.unload();
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ManagerEvent(addon, "Unload"));
		
		this.addon = null;
	}
	
	public <T extends Manager<?>> void unloadManager(Class<T> clazz) {
		if (!isRegistered(clazz) || !isLoaded(clazz))
			return;
		
		Manager<?> manager = getManager(clazz);
		
		manager.unload();
		managers.remove(clazz);
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ManagerEvent(manager, "Unload"));
	}
	
	public void unloadManagers() {
		Manager<?>[] managers = this.managers.values().toArray(new Manager<?>[0]);
		
		for (int manager = managers.length - 1; manager >= 0; manager--)
			unloadManager(managers[manager].getClass());
	}
	
	public void unregisterManager(Manager<?> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		
		if (!isRegistered(manager.getClass()))
			return;
		
		registered.remove(manager.getClass());
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ManagerEvent(manager, "Unregister"));
	}
}