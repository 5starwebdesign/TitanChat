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

package com.titankingdoms.dev.titanchat.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import com.titankingdoms.dev.titanchat.Manager;
import com.titankingdoms.dev.titanchat.TitanChat;

public final class AddonManager implements Manager<ChatAddon> {
	
	private final TitanChat plugin;
	
	private final Map<String, ChatAddon> addons;
	
	public AddonManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getAddonDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new TreeMap<String, ChatAddon>();
	}
	
	@Override
	public ChatAddon get(String name) {
		return (name != null) ? this.addons.get(name.toLowerCase()) : null;
	}
	
	@Override
	public List<ChatAddon> getAll() {
		return new ArrayList<ChatAddon>(this.addons.values());
	}
	
	public File getAddonDirectory() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? this.addons.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(ChatAddon addon) {
		if (addon == null)
			return false;
		
		if (!has(addon.getName()))
			return false;
		
		return get(addon.getName()).equals(addon);
	}
	
	@Override
	public void load() {
		
	}
	
	@Override
	public void registerAll(ChatAddon... addons) {
		if (addons == null)
			return;
		
		for (ChatAddon addon : addons) {
			if (addon == null)
				continue;
			
			if (has(addon.getName())) {
				plugin.log(Level.WARNING, "Duplicate: " + addon);
				continue;
			}
			
			this.addons.put(addon.getName().toLowerCase(), addon);
		}
	}
	
	@Override
	public void reload() {
		
	}
	
	@Override
	public void unload() {
		
	}
	
	@Override
	public void unregister(ChatAddon addon) {
		if (addon == null || !has(addon))
			return;
		
		if (!has(addon))
			return;
		
		this.addons.remove(addon.getName().toLowerCase());
	}
}