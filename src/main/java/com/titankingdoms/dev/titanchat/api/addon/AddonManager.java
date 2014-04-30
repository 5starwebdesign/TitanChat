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

package com.titankingdoms.dev.titanchat.api.addon;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.tools.loading.Loader;

public final class AddonManager implements Manager<Addon> {
	
	private final TitanChat plugin;
	
	private static final String NAME = "AddonManager";
	
	private static final Set<Class<? extends Manager<?>>> DEPENDENCIES;
	
	private final Map<String, Addon> addons;
	
	private boolean loaded = false;
	
	public AddonManager() {
		this.plugin = TitanChat.instance();
		
		if (getDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new HashMap<>();
	}
	
	static {
		DEPENDENCIES = ImmutableSet.of();
	}
	
	@Override
	public Addon get(String name) {
		return (name == null || name.isEmpty()) ? null : addons.get(name.toLowerCase());
	}
	
	@Override
	public Set<Addon> getAll() {
		return ImmutableSet.copyOf(addons.values());
	}
	
	@Override
	public Set<Class<? extends Manager<?>>> getDependencies() {
		return DEPENDENCIES;
	}
	
	public File getDirectory() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	@Override
	public String getName() {
		return NAME;
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
		if (loaded)
			return;
		
		for (Addon addon : Loader.load(Addon.class, getDirectory()))
			register(addon);
		
		for (Addon addon : getAll())
			addon.onEnable();
		
		this.loaded = true;
	}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return ImmutableList.copyOf(addons.keySet());
		
		Builder<String> matches = ImmutableList.builder();
		
		for (String addon : addons.keySet()) {
			if (!addon.startsWith(name.toLowerCase()))
				continue;
			
			matches.add(addon);
		}
		
		return matches.build();
	}
	
	@Override
	public void register(Addon addon) {
		Validate.notNull(addon, "Addon cannot be null");
		Validate.isTrue(!has(addon.getName()), "Addon already registered");
		
		addons.put(addon.getName().toLowerCase(), addon);
	}
	
	@Override
	public void reload() {
		if (!loaded)
			this.loaded = true;
		
		for (Addon addon : getAll()) {
			if (addon.getFile().exists())
				addon.onReload();
			else
				addon.onDisable();
			
			unregister(addon);
		}
		
		for (Addon addon : Loader.load(Addon.class, getDirectory()))
			register(addon);
	}
	
	@Override
	public void unload() {
		if (!loaded)
			return;
		
		for (Addon addon : getAll())
			addon.onDisable();
		
		for (Addon addon : getAll())
			unregister(addon);
		
		this.loaded = false;
	}
	
	@Override
	public void unregister(Addon addon) {
		Validate.notNull(addon, "Addon cannot be null");
		Validate.isTrue(has(addon.getName()), "Addon not registered");
		
		addons.remove(addon.getName().toLowerCase());
	}
}