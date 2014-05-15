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

package com.nodinchan.dev.module.loadable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableSet;
import com.nodinchan.dev.module.AbstractModule;
import com.nodinchan.dev.tools.loading.Loader;

public final class AddonManager extends AbstractModule {
	
	private File directory;
	
	private final Map<String, Addon> addons;
	
	public AddonManager(Plugin plugin) {
		super("AddonManager");
		
		if (plugin != null)
			this.directory = new File(plugin.getDataFolder(), "addons");
		
		this.addons = new HashMap<>();
	}
	
	public Addon getAddon(String name) {
		return (name == null || name.isEmpty()) ? null : addons.get(name.toLowerCase());
	}
	
	public File getDirectory() {
		if (directory == null)
			throw new IllegalStateException("Directory not set");
		
		return directory;
	}
	
	public boolean isLoaded(String name) {
		return name != null && !name.isEmpty() && addons.containsKey(name.toLowerCase());
	}
	
	@Override
	public void load() {
		for (Addon addon : Loader.load(Addon.class, getDirectory()))
			addons.put(addon.getName().toLowerCase(), addon);
		
		for (Addon addon : addons.values())
			addon.onEnable();
	}
	
	@Override
	public void reload() {
		for (Addon addon : ImmutableSet.copyOf(addons.values())) {
			if (!addon.getFile().exists())
				addon.onDisable();
			
			addons.remove(addon.getName().toLowerCase());
		}
		
		for (Addon addon : Loader.load(Addon.class, getDirectory())) {
			if (isLoaded(addon.getName()))
				continue;
			
			addons.put(addon.getName().toLowerCase(), addon);
		}
		
		for (Addon addon : addons.values()) {
			if (addon.isEnabled())
				addon.onReload();
			else
				addon.onEnable();
		}
	}
	
	public void setDirectory(File directory) {
		this.directory = (directory == null || directory.isFile()) ? null : directory;
	}
	
	@Override
	public void unload() {
		for (Addon addon : addons.values())
			addon.onDisable();
		
		for (Addon addon : ImmutableSet.copyOf(addons.values()))
			addons.remove(addon.getName().toLowerCase());
	}
}