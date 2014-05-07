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
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.addon.AddonManager;
import com.titankingdoms.dev.titanchat.api.event.ModuleEvent;

public final class ModularSystem {
	
	private final Map<Class<? extends Module>, String> labels;
	private final Map<String, Module> modules;
	private final Map<String, Module> registered;
	
	private AddonManager addon;
	
	private boolean running;
	
	public ModularSystem() {
		this.labels = new HashMap<>();
		this.modules = new LinkedHashMap<>();
		this.registered = new HashMap<>();
		this.running = false;
	}
	
	public AddonManager getAddonManager() {
		if (addon == null)
			throw new IllegalStateException("AddonManager not found");
		
		return addon;
	}
	
	public Module getModule(String name) {
		return modules.get(name);
	}
	
	public <T extends Module> T getModule(Class<T> clazz) {
		return clazz.cast(getModule(labels.get(clazz)));
	}
	
	public boolean isLoaded(String name) {
		return name != null && !name.isEmpty() && modules.containsKey(name);
	}
	
	public boolean isLoaded(Module module) {
		return module != null && modules.containsValue(module);
	}
	
	public boolean isLoaded(Class<? extends Module> clazz) {
		return isLoaded(labels.get(clazz));
	}
	
	public boolean isRegistered(String name) {
		return name != null && !name.isEmpty() && registered.containsKey(name);
	}
	
	public boolean isRegistered(Module module) {
		return module != null && registered.containsValue(module);
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public Module loadModule(Module module) {
		Validate.notNull(module, "Module cannot be null");
		
		if (!isRegistered(module))
			return null;
		
		if (module.isLoaded() && isLoaded(module))
			return module;
		
		for (String dependency : module.getDependencies()) {
			if (dependency == null || dependency.isEmpty() || isLoaded(dependency))
				continue;
			
			if (!isRegistered(dependency))
				return null;
			
			loadModule(registered.get(dependency));
		}
		
		module.setLoaded(true);
		
		labels.put(module.getClass(), module.getName());
		modules.put(module.getName(), module);
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ModuleEvent(module, "Load"));
		return module;
	}
	
	public void loadModules() {
		for (Module module : registered.values())
			loadModule(module);
	}
	
	public void registerModule(Module module) {
		Validate.notNull(module, "Module cannot be null");
		Validate.isTrue(!isRegistered(module.getName()), "Module already registered");
		
		registered.put(module.getName(), module);
	}
	
	public void reload() {
		if (!running)
			this.running = true;
		
		if (addon == null)
			addon = new AddonManager();
		
		if (!addon.isLoaded())
			addon.setLoaded(true);
		
		addon.reload();
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ModuleEvent(addon, "Reload"));
		
		reloadModules();
	}
	
	public Module reloadModule(Module module) {
		Validate.notNull(module, "Module cannot be null");
		
		if (!isRegistered(module) || loadModule(module) == null)
			return null;
		
		module.reload();
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ModuleEvent(module, "Reload"));
		return module;
	}
	
	public void reloadModules() {
		for (Module module : modules.values())
			reloadModule(module);
	}
	
	public void start() {
		if (running)
			return;
		
		this.addon = new AddonManager();
		
		addon.setLoaded(true);
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ModuleEvent(addon, "Load"));
		
		loadModules();
		
		this.running = true;
	}
	
	public void stop() {
		if (!running)
			return;
		
		unloadModules();
		
		addon.setLoaded(false);
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ModuleEvent(addon, "Unload"));
		
		this.addon = null;
		
		this.running = false;
	}
	
	public Module unloadModule(Module module) {
		Validate.notNull(module, "Module cannot be null");
		
		if (!isRegistered(module))
			return null;
		
		if (!module.isLoaded() && !isLoaded(module))
			return module;
		
		module.setLoaded(false);
		
		labels.remove(module.getClass());
		modules.remove(module.getName());
		
		TitanChat.instance().getServer().getPluginManager().callEvent(new ModuleEvent(module, "Unload"));
		return module;
	}
	
	public void unloadModules() {
		Module[] loaded = modules.values().toArray(new Module[0]);
		
		for (int index = loaded.length - 1; index >= 0; index--)
			unloadModule(loaded[index]);
	}
	
	public void unregisterModule(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(isRegistered(name), "Module not registered");
		
		registered.remove(unloadModule(getModule(name)).getName());
	}
}