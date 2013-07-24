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

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.titankingdoms.dev.titanchat.addon.AddonManager;
import com.titankingdoms.dev.titanchat.command.CommandManager;
import com.titankingdoms.dev.titanchat.core.channel.ChannelManager;
import com.titankingdoms.dev.titanchat.metrics.Metrics;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private final Logger log = Logger.getLogger("TitanLog");
	
	private final Map<Class<?>, Manager<?>> managers = new HashMap<Class<?>, Manager<?>>();
	
	public static TitanChat getInstance() {
		if (instance == null)
			throw new UnsupportedOperationException();
		
		return instance;
	}
	
	public <T extends Manager<?>> T getManager(Class<T> manager) {
		if (manager == null || !hasManager(manager))
			return null;
		
		synchronized (managers) {
			return manager.cast(managers.get(manager));
		}
	}
	
	public Set<Manager<?>> getManagers() {
		return new HashSet<Manager<?>>(managers.values());
	}
	
	public <T extends Manager<?>> boolean hasManager(Class<T> manager) {
		if (manager == null)
			return false;
		
		synchronized (managers) {
			return managers.containsKey(manager);
		}
	}
	
	private boolean initMetrics() {
		log(Level.INFO, "Attempting to set up Metrics...");
		
		if (!getConfig().getBoolean("metrics-statistics", true)) {
			log(Level.INFO, "Metrics Disabled");
			return true;
		}
		
		try {
			Metrics metrics = new Metrics(this);
			
			if (metrics.isOptOut())
				return true;
			
			return metrics.start();
			
		} catch (Exception e) {}
		
		return false;
	}
	
	public void log(Level level, String message) {
		if (message == null || message.isEmpty())
			return;
		
		log.log((level != null) ? level : Level.INFO, "[TitanChat v5.0.0] " + message);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}
	
	@Override
	public void onDisable() {
		log(Level.INFO, "TitanChat is now disabling...");
		
		if (instance != null)
			instance = null;
		
		log(Level.INFO, "Unloading managers...");
		
		for (Manager<?> manager : getManagers())
			manager.unload();
		
		log(Level.INFO, "TitanChat is now disabled");
	}
	
	@Override
	public void onEnable() {
		log(Level.INFO, "TitanChat is now enabling...");
		
		if (instance == null)
			instance = this;
		
		log(Level.INFO, "Attempting to set up Vault...");
		
		if (!Vault.initialise(getServer()))
			log(Level.INFO, "Failed to set up Vault");
		
		getServer().getPluginManager().registerEvents(new TitanChatListener(), this);
		log(Level.INFO, "Registered listeners");
		
		if (!initMetrics())
			log(Level.INFO, "Failed to set up Metrics");
		
		log(Level.INFO, "Loading managers...");
		
		for (Manager<?> manager : getManagers())
			manager.load();
		
		log(Level.INFO, "TitanChat is now enabled");
	}
	
	@Override
	public void onLoad() {
		log(Level.INFO, "TitanChat is now loading...");
		
		instance = this;
		
		log(Level.INFO, "Registering managers...");
		
		registerManager(new CommandManager());
		registerManager(new ChannelManager());
		registerManager(new AddonManager());
		
		if (!new File(getDataFolder(), "config.yml").exists()) {
			log(Level.INFO, "Generating default config.yml...");
			saveResource("config.yml", false);
		}
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		log(Level.INFO, "TitanChat is now loaded");
	}
	
	public void onReload() {
		log(Level.INFO, "TitanChat is now reloading...");
		
		if (instance == null)
			instance = this;
		
		log(Level.INFO, "Reloading managers...");
		
		for (Manager<?> manager : getManagers())
			manager.reload();
		
		log(Level.INFO, "TitanChat is now reloaded");
	}
	
	public void registerManager(Manager<?> manager) {
		if (manager == null)
			return;
		
		if (hasManager(manager.getClass()))
			return;
		
		synchronized (managers) {
			managers.put(manager.getClass(), manager);
		}
	}
	
	public void unregisterManager(Manager<?> manager) {
		if (manager == null)
			return;
		
		if (!hasManager(manager.getClass()))
			return;
		
		synchronized (managers) {
			managers.remove(manager.getClass());
		}
	}
}