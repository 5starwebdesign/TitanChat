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
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.AbstractModule;
import com.titankingdoms.dev.titanchat.tools.loading.Loader;

public final class AddonManager extends AbstractModule {
	
	private final TitanChat plugin;
	
	private final Map<String, Addon> addons;
	
	public AddonManager() {
		super("AddonManager");
		this.plugin = TitanChat.instance();
		
		if (getDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new HashMap<>();
	}
	
	public Addon getAddon(String name) {
		return (name == null || name.isEmpty()) ? null : addons.get(name.toLowerCase());
	}
	
	public Set<Addon> getAddons() {
		return ImmutableSet.copyOf(addons.values());
	}
	
	public File getDirectory() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	public boolean isLoaded(String name) {
		return name != null && !name.isEmpty() && addons.containsKey(name.toLowerCase());
	}
	
	@Override
	public void load() {
		for (Addon addon : Loader.load(Addon.class, getDirectory()))
			addons.put(addon.getName().toLowerCase(), addon);
		
		for (Addon addon : getAddons())
			addon.onEnable();
	}
	
	@Override
	public void reload() {
		for (Addon addon : getAddons()) {
			if (!addon.getFile().exists())
				addon.onDisable();
			
			addons.remove(addon.getName().toLowerCase());
		}
		
		for (Addon addon : Loader.load(Addon.class, getDirectory())) {
			if (isLoaded(addon.getName()))
				continue;
			
			addons.put(addon.getName().toLowerCase(), addon);
		}
		
		for (Addon addon : getAddons()) {
			if (addon.isEnabled())
				addon.onReload();
			else
				addon.onEnable();
		}
	}
	
	@Override
	public void unload() {
		for (Addon addon : getAddons())
			addon.onDisable();
		
		for (Addon addon : getAddons())
			addons.remove(addon.getName().toLowerCase());
	}
}