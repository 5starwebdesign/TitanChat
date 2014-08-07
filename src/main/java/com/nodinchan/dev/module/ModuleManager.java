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

package com.nodinchan.dev.module;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

import com.nodinchan.dev.module.event.ModuleEvent;

public final class ModuleManager {
	
	private final Plugin plugin;
	
	private final Map<Class<? extends Module>, Module> modules;
	
	public ModuleManager(Plugin plugin) {
		Validate.notNull(plugin, "Plugin cannot be null");
		
		this.plugin = plugin;
		this.modules = new HashMap<>();
	}
	
	public <T extends Module> T getModule(Class<T> clazz) {
		return (clazz == null) ? null : clazz.cast(modules.get(clazz));
	}
	
	public boolean isLoaded(Class<? extends Module> clazz) {
		return isRegistered(clazz) && modules.get(clazz).isLoaded();
	}
	
	public boolean isRegistered(Class<? extends Module> clazz) {
		return modules.containsKey(clazz);
	}
	
	public void loadModule(Class<? extends Module> clazz) {
		Validate.notNull(clazz, "Class cannot be null");
		Validate.isTrue(isRegistered(clazz), "Module has not been registered");
		
		Module module = modules.get(clazz);
		
		Validate.isTrue(!module.isLoaded(), module.getName() + " has already been loaded");
		
		if (module.setLoaded(true))
			plugin.getServer().getPluginManager().callEvent(new ModuleEvent(module, "Load"));
		else
			plugin.getLogger().log(Level.WARNING, module.getName() + " failed to load");
	}
	
	public void registerModule(Module module) {
		Validate.notNull(module, "Module cannot be null");
		Validate.isTrue(!isRegistered(module.getClass()), module.getName() + " has already been registered");
		
		modules.put(module.getClass(), module);
		
		plugin.getServer().getPluginManager().callEvent(new ModuleEvent(module, "Register"));
	}
	
	public void unloadModule(Class<? extends Module> clazz) {
		Validate.notNull(clazz, "Class cannot be null");
		Validate.isTrue(isRegistered(clazz), "Module has not been registered");
		
		Module module = modules.get(clazz);
		
		Validate.isTrue(module.isLoaded(), module.getName() + " has not been loaded");
		
		if (module.setLoaded(false))
			plugin.getServer().getPluginManager().callEvent(new ModuleEvent(module, "Unload"));
		else
			plugin.getLogger().log(Level.WARNING, module.getName() + " failed to unload");
	}
	
	public void unregisterModule(Class<? extends Module> clazz) {
		Validate.notNull(clazz, "Class cannot be null");
		Validate.isTrue(isRegistered(clazz), "Module has not been registered");
		
		Module module = modules.remove(clazz);
		
		plugin.getServer().getPluginManager().callEvent(new ModuleEvent(module, "Unregister"));
	}
}