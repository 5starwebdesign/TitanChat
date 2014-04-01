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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.tools.loading.Loader;

public final class AddonManager implements Manager<Addon> {
	
	private final TitanChat plugin;
	
	private final Map<String, Addon> addons;
	
	private final Set<String> dependencies = ImmutableSet.<String>builder().build();
	
	public AddonManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new TreeMap<String, Addon>();
	}
	
	@Override
	public Addon get(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		return addons.get(name.toLowerCase());
	}
	
	@Override
	public Collection<Addon> getAll() {
		return new HashSet<Addon>(addons.values());
	}
	
	@Override
	public Set<String> getDependencies() {
		return dependencies;
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
		Validate.isTrue(!has(addon.getName()), "Addon already registered");
		
		addons.put(addon.getName().toLowerCase(), addon);
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
		Validate.isTrue(has(addon.getName()), "Addon not registered");
		
		addons.remove(addon.getName().toLowerCase());
	}
}