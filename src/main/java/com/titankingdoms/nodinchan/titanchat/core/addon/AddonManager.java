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

package com.titankingdoms.nodinchan.titanchat.core.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.loading.Loader;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

public final class AddonManager {
	
	private final TitanChat plugin;
	
	private static final Debugger db = new Debugger(1, "AddonManager");
	
	private Map<String, Addon> addons;
	
	public AddonManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getAddonDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new TreeMap<String, Addon>();
	}
	
	public boolean existingAddon(String name) {
		return addons.containsKey(name.toLowerCase());
	}
	
	private boolean existingAddon(Addon addon) {
		return existingAddon(addon.getName());
	}
	
	public Addon getAddon(String name) {
		return addons.get(name.toLowerCase());
	}
	
	public File getAddonDirectory() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	public List<Addon> getAddons() {
		return new ArrayList<Addon>(addons.values());
	}
	
	public void load() {
		for (Addon addon : Loader.load(Addon.class, getAddonDirectory()))
			register(addon);
		
		if (!addons.isEmpty())
			plugin.log(Level.INFO, "Addons loaded: " + StringUtils.join(addons.keySet(), ", "));
	}
	
	public void register(Addon... addons) {
		for (Addon addon : addons) {
			if (existingAddon(addon))
				continue;
			
			this.addons.put(addon.getName().toLowerCase(), addon);
			db.i("Registered addon: " + addon.getName());
		}
	}
	
	public void unload() {
		addons.clear();
	}
}