/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.titankingdoms.dev.titanchat.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.loading.Loader;

/**
 * AddonManager - Manages {@link ChatAddon}s
 * 
 * @author NodinChan
 *
 */
public final class AddonManager {
	
	private final TitanChat plugin;
	
	private final Map<String, ChatAddon> addons;
	
	public AddonManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getAddonDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new TreeMap<String, ChatAddon>();
	}
	
	/**
	 * Gets the specified {@link ChatAddon}
	 * 
	 * @param name The name of the {@link ChatAddon}
	 * 
	 * @return The specified {@link ChatAddon} if found, otherwise null
	 */
	public ChatAddon getAddon(String name) {
		return addons.get(name);
	}
	
	/**
	 * Gets the directory that holds the {@link ChatAddon}s
	 * 
	 * @return The directory of the {@link ChatAddon}s
	 */
	public File getAddonDirectory() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	/**
	 * Gets all {@link ChatAddon}s
	 * 
	 * @return All registered {@link ChatAddon}s
	 */
	public List<ChatAddon> getAddons() {
		return new ArrayList<ChatAddon>(addons.values());
	}
	
	/**
	 * Checks if the {@link ChatAddon} has been registered
	 * 
	 * @param name The name of the {@link ChatAddon}
	 * 
	 * @return True if found
	 */
	public boolean hasAddon(String name) {
		return addons.containsKey(name.toLowerCase());
	}
	
	/**
	 * Checks if the {@link ChatAddon} has been registered
	 * 
	 * @param addon The {@link ChatAddon}
	 * 
	 * @return True if found
	 */
	public boolean hasAddon(ChatAddon addon) {
		return hasAddon(addon.getName());
	}
	
	/**
	 * Loads the manager
	 */
	public void load() {
		registerAddons(Loader.load(ChatAddon.class, getAddonDirectory()).toArray(new ChatAddon[0]));
		
		if (!addons.isEmpty())
			plugin.log(Level.INFO, "Addons loaded: " + StringUtils.join(addons.keySet(), ", "));
	}
	
	/**
	 * Registers the {@link ChatAddon}s
	 * 
	 * @param addons The {@link ChatAddon}s to register
	 */
	public void registerAddons(ChatAddon... addons) {
		if (addons == null)
			return;
		
		for (ChatAddon addon : addons) {
			if (addon == null)
				continue;
			
			if (hasAddon(addon)) {
				plugin.log(Level.WARNING, "Duplicate addon: " + addon.getName());
				continue;
			}
			
			this.addons.put(addon.getName().toLowerCase(), addon);
		}
	}
	
	/**
	 * Reloads the manager
	 */
	public void reload() {
		addons.clear();
		
		registerAddons(Loader.load(ChatAddon.class, getAddonDirectory()).toArray(new ChatAddon[0]));
		
		if (!addons.isEmpty())
			plugin.log(Level.INFO, "Addons loaded: " + StringUtils.join(addons.keySet(), ", "));
	}
	
	/**
	 * Unloads the manager
	 */
	public void unload() {
		addons.clear();
	}
	
	/**
	 * Unregisters the {@link ChatAddon}
	 * 
	 * @param addon The {@link ChatAddon} to unregister
	 */
	public void unregisterAddon(ChatAddon addon) {
		if (addon == null || !hasAddon(addon))
			return;
		
		this.addons.remove(addon.getName().toLowerCase());
	}
}