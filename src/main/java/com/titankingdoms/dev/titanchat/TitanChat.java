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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.titankingdoms.dev.titanchat.addon.AddonManager;
import com.titankingdoms.dev.titanchat.command.CommandManager;
import com.titankingdoms.dev.titanchat.core.channel.ChannelManager;
import com.titankingdoms.dev.titanchat.core.user.UserManager;
import com.titankingdoms.dev.titanchat.format.TagParser;
import com.titankingdoms.dev.titanchat.listener.TitanChatListener;
import com.titankingdoms.dev.titanchat.metrics.Metrics;
import com.titankingdoms.dev.titanchat.util.UpdateUtil;
import com.titankingdoms.dev.titanchat.util.VaultUtils;

public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private final Logger log = Logger.getLogger("TitanLog");
	
	private final Map<Class<?>, Manager<?>> managers = new LinkedHashMap<Class<?>, Manager<?>>();
	
	private UpdateUtil update;
	
	public static TitanChat getInstance() {
		if (instance == null)
			throw new IllegalStateException("TitanChat is in operation");
		
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
	
	public boolean hasUpdate() {
		return update.hasUpdate();
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
			
			boolean initialised = metrics.start();
			
			if (initialised)
				log(Level.INFO, "Metrics has been successfully initialised");
			
			return initialised;
			
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
		if (command.getName().equalsIgnoreCase("TitanChat")) {
			getManager(CommandManager.class).execute(sender, reassembleArguments(args));
			return true;
		}
		
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
		
		HandlerList.unregisterAll(this);
		log(Level.INFO, "Unregistered listeners");
		
		log(Level.INFO, "TitanChat is now disabled");
	}
	
	@Override
	public void onEnable() {
		log(Level.INFO, "TitanChat is now enabling...");
		
		if (instance == null)
			instance = this;
		
		log(Level.INFO, "Attempting to set up VaultUtils...");
		
		if (!VaultUtils.initialise(getServer()))
			log(Level.INFO, "Failed to set up VaultUtils");
		
		getServer().getPluginManager().registerEvents(new TitanChatListener(), this);
		log(Level.INFO, "Registered listeners");
		
		if (!initMetrics())
			log(Level.INFO, "Failed to initialise Metrics");
		
		if (!searchUpdate())
			log(Level.INFO, "No updates available");
		
		log(Level.INFO, "Loading managers...");
		
		for (Manager<?> manager : getManagers())
			manager.load();
		
		getServer().getPluginManager().registerEvents(new TitanChatListener(), this);
		log(Level.INFO, "Registered listeners");
		
		log(Level.INFO, "TitanChat is now enabled");
	}
	
	@Override
	public void onLoad() {
		log(Level.INFO, "TitanChat is now loading...");
		
		instance = this;
		
		log(Level.INFO, "Registering managers...");
		
		registerManager(new TagParser());
		registerManager(new ChannelManager());
		registerManager(new UserManager());
		registerManager(new CommandManager());
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
		
		if (!searchUpdate())
			log(Level.INFO, "No updates available");
		
		log(Level.INFO, "Reloading managers...");
		
		for (Manager<?> manager : getManagers())
			manager.reload();
		
		log(Level.INFO, "TitanChat is now reloaded");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("TitanChat"))
			return getManager(CommandManager.class).tab(sender, reassembleArguments(args));
		
		return new ArrayList<String>();
	}
	
	private String[] reassembleArguments(String[] args) {
		List<String> arguments = new ArrayList<String>();
		
		Matcher match = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(StringUtils.join(args, ' '));
		
		while (match.find())
			arguments.add(match.group().replace("\"", ""));
		
		return arguments.toArray(new String[0]);
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
	
	private boolean searchUpdate() {
		log(Level.INFO, "Attempting to search for updates...");
		
		if (update == null)
			this.update = new UpdateUtil("titanchat", getDescription());
		
		if (!getConfig().getBoolean("update-search", false)) {
			log(Level.INFO, "Search Disabled");
			return true;
		}
		
		update.readFeed();
		update.checkAvailability();
		
		boolean available = update.hasUpdate();
		
		if (available) {
			log(Level.INFO, "A new version of TitanChat is available! (" + update.getNewName() + ")");
			log(Level.INFO, "You are running " + update.getCurrentName() + " currently");
			log(Level.INFO, "Update at http://dev.bukkit.org/bukkit-plugins/titanchat/files/");
		}
		
		return available;
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