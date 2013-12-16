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

package com.titankingdoms.dev.titanchat.api.addon;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.tools.loading.Loader;

public final class AddonManager implements Manager<Addon> {
	
	private final TitanChat plugin;
	
	private final Map<String, Addon> addons;
	
	public AddonManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new TreeMap<String, Addon>();
	}
	
	@Override
	public Addon get(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		return (has(name)) ? addons.get(name.toLowerCase()) : null;
	}
	
	public Addon getAddon(String name) {
		return get(name);
	}
	
	public Collection<Addon> getAddons() {
		return getAll();
	}
	
	@Override
	public Collection<Addon> getAll() {
		return new HashSet<Addon>(addons.values());
	}
	
	public File getDirectory() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	@Override
	public String getName() {
		return "AddonManager";
	}
	
	@Override
	public boolean has(String name) {
		return name != null && !name.isEmpty() && addons.containsKey(name.toLowerCase());
	}
	
	@Override
	public boolean has(Addon addon) {
		return addon != null && has(addon.getName()) && get(addon.getName()).equals(addon);
	}
	
	public boolean hasAddon(String name) {
		return has(name);
	}
	
	public boolean hasAddon(Addon addon) {
		return has(addon);
	}
	
	@Override
	public void load() {
		for (Addon addon : getAll())
			addon.onEnable();
	}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return new ArrayList<String>(addons.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String addon : addons.keySet()) {
			if (!addon.startsWith(name.toLowerCase()))
				continue;
			
			matches.add(addon);
		}
		
		Collections.sort(matches);
		
		return matches;
	}
	
	@Override
	public void register(Addon addon) {
		Validate.notNull(addon, "Addon cannot be null");
		
		if (has(addon.getName())) {
			plugin.log(Level.WARNING, "Duplicate: " + addon);
			return;
		}
		
		this.addons.put(addon.getName().toLowerCase(), addon);
	}
	
	@Override
	public void reload() {
		for (Addon addon : getAll()) {
			if (addon.getFile().exists()) {
				addon.onReload();
				continue;
			}
			
			unregister(addon);
			addon.onDisable();
		}
		
		for (Addon addon : Loader.load(Addon.class, getDirectory())) {
			if (has(addon))
				continue;
			
			register(addon);
		}
	}
	
	@Override
	public void unload() {
		for (Addon addon : getAll())
			addon.onDisable();
		
		for (Addon addon : getAll())
			unregister(addon);
	}
	
	@Override
	public void unregister(Addon addon) {
		Validate.notNull(addon, "Addon cannot be null");
		
		if (!has(addon))
			return;
		
		this.addons.remove(addon.getName().toLowerCase());
	}
}