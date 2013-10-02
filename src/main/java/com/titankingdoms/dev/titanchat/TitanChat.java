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
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.Point;
import com.titankingdoms.dev.titanchat.api.addon.AddonManager;
import com.titankingdoms.dev.titanchat.api.command.CommandManager;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent;
import com.titankingdoms.dev.titanchat.api.event.manager.ManagerLoadEvent;
import com.titankingdoms.dev.titanchat.api.event.manager.ManagerReloadEvent;
import com.titankingdoms.dev.titanchat.api.event.manager.ManagerUnloadEvent;
import com.titankingdoms.dev.titanchat.api.format.var.VarFormat;
import com.titankingdoms.dev.titanchat.core.CoreListener;
import com.titankingdoms.dev.titanchat.core.conversation.ProvisionManager;
import com.titankingdoms.dev.titanchat.core.user.UserManager;
import com.titankingdoms.dev.titanchat.tools.Debugger;
import com.titankingdoms.dev.titanchat.tools.metrics.Metrics;
import com.titankingdoms.dev.titanchat.tools.util.FormatUtils;
import com.titankingdoms.dev.titanchat.tools.util.UpdateUtil;
import com.titankingdoms.dev.titanchat.tools.util.VaultUtils;

public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private final Logger log = Logger.getLogger("TitanLog");
	
	private final Map<Class<?>, Manager<?>> managers = new LinkedHashMap<Class<?>, Manager<?>>();
	
	private final Debugger db = new Debugger(0);
	
	public void converse(Point sender, Point recipient, String format, String message) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(recipient, "Recipient cannot be null");
		Validate.notEmpty(format, "Format cannot be empty");
		Validate.notEmpty(message, "Message cannot be empty");
		
		ConverseEvent event = new ConverseEvent(sender, recipient, format, message);
		
		if (!event.getRelayPoints().contains(sender))
			event.getRelayPoints().add(sender);
		
		getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		String processedFormat = getManager(VarFormat.class).parse(event);
		
		List<String> items = getConfig().getStringList("censorship.items");
		String censor = getConfig().getString("censorship.censor", "*");
		
		String processedMessage = FormatUtils.censor(message, items, censor);
		
		String line = processedFormat.replace("%message", processedMessage);
		
		if (!event.getRelayPoints().contains(sender))
			event.getRelayPoints().add(sender);
		
		for (Point relay : event.getRelayPoints())
			relay.sendRawLine(line);
		
		if (event.getRelayPoints().size() < 2)
			sender.sendRawLine("&7Nobody heard you...");
	}
	
	public static TitanChat getInstance() {
		if (instance == null)
			throw new IllegalStateException("TitanChat is not in operation");
		
		return instance;
	}
	
	public <T extends Manager<?>> T getManager(Class<T> manager) {
		Validate.notNull(manager, "Manager Class cannot be null");
		
		if (!hasManager(manager))
			return null;
		
		synchronized (managers) {
			return manager.cast(managers.get(manager));
		}
	}
	
	public List<Manager<?>> getManagers() {
		synchronized (managers) {
			return new LinkedList<Manager<?>>(managers.values());
		}
	}
	
	public <T extends Manager<?>> boolean hasManager(Class<T> manager) {
		Validate.notNull(manager, "Manager Class cannot be null");
		
		synchronized (managers) {
			return managers.containsKey(manager);
		}
	}
	
	private boolean initMetrics() {
		log(Level.INFO, "Attempting to initialise Metrics...");
		
		if (!getConfig().getBoolean("metrics-statistics", true)) {
			log(Level.INFO, "Metrics Disabled");
			return true;
		}
		
		try {
			Metrics metrics = new Metrics(this);
			
			if (metrics.isOptOut())
				return true;
			
			boolean initialised = metrics.start();
			
			if (!initialised)
				return false;
			
			log(Level.INFO, "Metrics has been successfully initialised");
			return true;
			
		} catch (Exception e) {}
		
		return false;
	}
	
	public void log(Level level, String message) {
		Validate.notEmpty(message, "Message cannot be empty");
		log.log((level != null) ? level : Level.INFO, "[TitanChat v5.0.0] " + message);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(command, "Command cannot be null");
		Validate.notEmpty(label, "Label cannot be empty");
		Validate.notNull(args, "Arguments cannot be null");
		
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
		
		HandlerList.unregisterAll(this);
		log(Level.INFO, "Unregistered listeners");
		
		log(Level.INFO, "Unloading managers...");
		
		long managers = System.currentTimeMillis();
		
		for (Manager<?> manager : getManagers()) {
			manager.unload();
			getServer().getPluginManager().callEvent(new ManagerUnloadEvent(manager));
		}
		
		db.debug(Level.INFO, "Time: " + (System.currentTimeMillis() - managers) + "ms");
		
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
		
		getServer().getPluginManager().registerEvents(new CoreListener(), this);
		log(Level.INFO, "Registered listeners");
		
		if (!initMetrics())
			log(Level.INFO, "Failed to initialise Metrics");
		
		if (!searchUpdate())
			log(Level.INFO, "No updates available");
		
		log(Level.INFO, "Loading managers...");
		
		long managers = System.currentTimeMillis();
		
		for (Manager<?> manager : getManagers()) {
			manager.load();
			getServer().getPluginManager().callEvent(new ManagerLoadEvent(manager));
		}
		
		db.debug(Level.INFO, "Time: " + (System.currentTimeMillis() - managers) + "ms");
		
		getServer().getPluginManager().registerEvents(new CoreListener(), this);
		log(Level.INFO, "Registered listeners");
		
		log(Level.INFO, "TitanChat is now enabled");
	}
	
	@Override
	public void onLoad() {
		log(Level.INFO, "TitanChat is now loading...");
		
		instance = this;
		
		log(Level.INFO, "Registering managers...");
		
		registerManager(new CommandManager());
		registerManager(new VarFormat());
		registerManager(new ProvisionManager());
		registerManager(new UserManager());
		registerManager(new AddonManager());
		
		if (!new File(getDataFolder(), "config.yml").exists()) {
			log(Level.INFO, "Generating default config.yml...");
			saveResource("config.yml", false);
		}
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		for (Manager<?> manager : getManagers())
			manager.init();
		
		log(Level.INFO, "TitanChat is now loaded");
	}
	
	public void onReload() {
		log(Level.INFO, "TitanChat is now reloading...");
		
		if (instance == null)
			instance = this;
		
		if (!searchUpdate())
			log(Level.INFO, "No updates available");
		
		log(Level.INFO, "Reloading managers...");
		
		long managers = System.currentTimeMillis();
		
		for (Manager<?> manager : getManagers()) {
			manager.reload();
			getServer().getPluginManager().callEvent(new ManagerReloadEvent(manager));
		}
		
		db.debug(Level.INFO, "Time: " + (System.currentTimeMillis() - managers) + "ms");
		
		log(Level.INFO, "TitanChat is now reloaded");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(command, "Command cannot be null");
		Validate.notEmpty(label, "Label cannot be empty");
		Validate.notNull(args, "Arguments cannot be null");
		
		if (command.getName().equalsIgnoreCase("TitanChat"))
			return getManager(CommandManager.class).tab(sender, reassembleArguments(args));
		
		return new ArrayList<String>();
	}
	
	private String[] reassembleArguments(String[] args) {
		Validate.notNull(args, "Arguments cannot be null");
		
		if (args.length < 1)
			return new String[0];
		
		List<String> arguments = new ArrayList<String>();
		
		Matcher match = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(StringUtils.join(args, ' '));
		
		while (match.find())
			arguments.add(match.group().replace("\"", "").trim());
		
		return arguments.toArray(new String[0]);
	}
	
	public void registerManager(Manager<?> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		
		if (hasManager(manager.getClass()))
			return;
		
		synchronized (managers) {
			managers.put(manager.getClass(), manager);
		}
	}
	
	private boolean searchUpdate() {
		log(Level.INFO, "Attempting to search for updates...");
		
		if (!getConfig().getBoolean("update-search", false)) {
			log(Level.INFO, "Search Disabled");
			return true;
		}
		
		UpdateUtil update = new UpdateUtil("titanchat", getDescription());
		
		long search = System.currentTimeMillis();
		
		update.readFeed();
		update.checkAvailability();
		
		db.debug(Level.INFO, "Time: " + (System.currentTimeMillis() - search) + "ms");
		
		boolean available = update.hasUpdate();
		
		if (!available)
			return false;
		
		log(Level.INFO, "A new version of TitanChat is available! (" + update.getNewName() + ")");
		log(Level.INFO, "You are running " + update.getCurrentName() + " currently");
		log(Level.INFO, "Update at http://dev.bukkit.org/bukkit-plugins/titanchat/files/");
		return true;
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