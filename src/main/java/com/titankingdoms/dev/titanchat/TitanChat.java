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

package com.titankingdoms.dev.titanchat;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.addon.AddonManager;
import com.titankingdoms.dev.titanchat.api.command.CommandManager;
import com.titankingdoms.dev.titanchat.tools.feed.UpdateFeedReader;
import com.titankingdoms.dev.titanchat.utility.VaultUtils;

public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private final Logger log = Logger.getLogger("TitanLog");
	
	private final Map<Class<?>, Manager<?>> managers = new LinkedHashMap<Class<?>, Manager<?>>();
	
	public static TitanChat getInstance() {
		if (instance == null)
			throw new IllegalStateException("TitanChat is not in operation");
		
		return instance;
	}
	
	public <T extends Manager<?>> T getManager(Class<T> manager) {
		Validate.notNull(manager, "Manager Class cannot be null");
		
		synchronized (managers) {
			return (hasManager(manager)) ? manager.cast(managers.get(manager)) : null;
		}
	}
	
	public List<Manager<?>> getManagers() {
		synchronized (managers) {
			return new ArrayList<Manager<?>>(managers.values());
		}
	}
	
	public <T extends Manager<?>> boolean hasManager(Class<T> manager) {
		Validate.notNull(manager, "Manager Class cannot be null");
		
		synchronized (managers) {
			return managers.containsKey(manager);
		}
	}
	
	private boolean inquireUpdate() {
		log(Level.INFO, "Attempting to inquire for updates...");
		
		if (!getConfig().getBoolean("update-inquiry", false)) {
			log(Level.INFO, "Inquiry Disabled");
			return true;
		}
		
		UpdateFeedReader reader = new UpdateFeedReader("titanchat", getDescription());
		
		reader.readFeed();
		reader.checkAvailability();
		
		if (!reader.hasUpdate())
			return false;
		
		log(Level.INFO, "A new version of TitanChat is available! (" + reader.getNewName() + ")");
		log(Level.INFO, "You are running " + reader.getCurrentName() + " currently");
		log(Level.INFO, "Get files at http://dev.bukkit.org/bukkit-plugins/titanchat/files/");
		return true;
	}
	
	public void log(Level level, String message) {
		Validate.notEmpty(message, "Message cannot be empty");
		log.log((level != null) ? level : Level.INFO, message);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return getManager(CommandManager.class).run(sender, label, args);
	}
	
	@Override
	public void onDisable() {
		log(Level.INFO, "Now disabling...");
		
		if (instance != null)
			instance = null;
		
		log(Level.INFO, "Unloading managers...");
		
		for (Manager<?> manager : getManagers())
			manager.unload();
		
		log(Level.INFO, "Now disabled");
	}
	
	@Override
	public void onEnable() {
		log(Level.INFO, "Now enabling...");
		
		if (instance == null)
			instance = this;
		
		if (!inquireUpdate())
			log(Level.INFO, "No updates available");
		
		log(Level.INFO, "Attempting to set up VaultUtils...");
		
		if (!VaultUtils.initialise(getServer()))
			log(Level.INFO, "Failed to set up VaultUtils");
		
		log(Level.INFO, "Loading managers...");
		
		for (Manager<?> manager : getManagers())
			manager.load();
		
		log(Level.INFO, "Now enabled");
	}
	
	@Override
	public void onLoad() {
		log(Level.INFO, "Now loading...");
		
		instance = this;
		
		log(Level.INFO, "Registering managers...");
		registerManager(new CommandManager());
		registerManager(new AddonManager());
		
		log(Level.INFO, "Now loaded");
	}
	
	public void onReload() {
		log(Level.INFO, "Now reloading...");
		
		if (instance == null)
			instance = this;
		
		log(Level.INFO, "Reloading managers...");
		
		for (Manager<?> manager : getManagers())
			manager.reload();
		
		log(Level.INFO, "Now reloaded");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return getManager(CommandManager.class).preview(sender, args);
	}
	
	public void registerManager(Manager<?> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		
		if (hasManager(manager.getClass()))
			return;
		
		synchronized (managers) {
			managers.put(manager.getClass(), manager);
		}
	}
	
	public void unregisterManager(Manager<?> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		
		if (!hasManager(manager.getClass()))
			return;
		
		synchronized (managers) {
			managers.remove(manager.getClass());
		}
	}
}