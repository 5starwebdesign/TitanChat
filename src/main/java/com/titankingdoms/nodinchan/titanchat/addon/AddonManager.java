/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;

/**
 * AddonManager - Manages addons
 * 
 * @author NodinChan
 *
 */
public final class AddonManager {
	
	private final TitanChat plugin;
	
	private static final Debugger db = new Debugger(1, "AddonManager");
	
	private Map<String, Addon> addons;
	
	public AddonManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getAddonDirectory().mkdir())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new HashMap<String, Addon>();
	}
	
	/**
	 * Gets the addon by name
	 * 
	 * @param name The name of the addon
	 * 
	 * @return The addon if found, otherwise null
	 */
	public Addon getAddon(String name) {
		return addons.get(name.toLowerCase());
	}
	
	/**
	 * Gets the addon directory
	 * 
	 * @return The directory of addons
	 */
	public File getAddonDirectory() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	/**
	 * Gets all addons
	 * 
	 * @return The list of addons
	 */
	public List<Addon> getAddons() {
		return new ArrayList<Addon>(addons.values());
	}
	
	/**
	 * Loads the manager
	 */
	public void load() {
		for (Addon addon : plugin.getLoader().load(Addon.class, getAddonDirectory()))
			register(addon);
		
		sortAddons();
		
		if (addons.size() < 1)
			return;
		
		StringBuilder str = new StringBuilder();
		
		for (Addon addon : getAddons()) {
			if (str.length() > 0)
				str.append(", ");
			
			str.append(addon.getName());
		}
		
		plugin.log(Level.INFO, "Addons loaded: " + str.toString());
	}
	
	/**
	 * After reloading everything
	 */
	public void postReload() {
		load();
	}
	
	/**
	 * Before reloading everything
	 */
	public void preReload() {
		unload();
	}
	
	/**
	 * Registers the addon
	 * 
	 * @param addon The addon to register
	 */
	public void register(Addon addon) {
		db.debug(DebugLevel.I, "Registering addon: " + addon.getName());
		
		if (!addons.containsKey(addon.getName().toLowerCase()))
			addons.put(addon.getName().toLowerCase(), addon);
	}
	
	private void sortAddons() {
		List<Addon> addons = new ArrayList<Addon>(this.addons.values());
		Collections.sort(addons);
		this.addons.clear();
		
		for (Addon addon : addons)
			this.addons.put(addon.getName().toLowerCase(), addon);
	}
	
	/**
	 * Unloads the manager
	 */
	public void unload() {
		addons.clear();
	}
}