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

package com.nodinchan.dev.titanchat;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.nodinchan.dev.module.ModularSystem;
import com.nodinchan.dev.titanchat.api.command.CommandManager;
import com.nodinchan.dev.titanchat.api.conversation.SimpleNetwork;
import com.nodinchan.dev.titanchat.api.guide.SimpleGuide;
import com.nodinchan.dev.titanchat.conversation.user.UserManager;
import com.nodinchan.dev.titanchat.listener.TitanChatListener;
import com.nodinchan.dev.titanchat.tools.Vault;
import com.nodinchan.dev.titanchat.tools.metrics.Metrics;
import com.nodinchan.dev.tools.release.ReleaseHistory;
import com.nodinchan.dev.tools.release.ReleaseHistory.Version;

public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private final Logger log = Logger.getLogger("TitanLog");
	
	private final ModularSystem system = new ModularSystem(this);
	
	public ModularSystem getSystem() {
		if (instance == null)
			throw new IllegalStateException("TitanChat is not in operation");
		
		return system;
	}
	
	private void enquireUpdate() {
		getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
			
			@Override
			public void run() {
				log(Level.INFO, "Attempting to enquire for updates...");
				
				if (!getConfig().getBoolean("tools.update-enquiry", false)) {
					log(Level.INFO, "Enquiry is disabled");
					return;
				}
				
				ReleaseHistory history = new ReleaseHistory(35800, getDescription());
				history.searchHistory();
				
				if (!history.hasVersions()) {
					log(Level.INFO, "No releases found");
					return;
				}
				
				Version rVersion = history.getRunningVersion();
				Version lVersion = history.getLatestVersion();
				
				String[] rVersioning = rVersion.getTitle().replaceAll("[^\\d\\.]", "").split("\\.");
				String[] lVersioning = lVersion.getTitle().replaceAll("[^\\d\\.]", "").split("\\.");
				
				int comparison = Math.min(rVersioning.length, lVersioning.length);
				
				for (int index = 0; index < comparison; index++) {
					int running = NumberUtils.toInt(rVersioning[index], 0);
					int latest = NumberUtils.toInt(lVersioning[index], 0);
					
					if (latest > running)
						break;
					
					if (latest == running && index < comparison - 1)
						continue;
					
					log(Level.INFO, "No updates available");
					return;
				}
				
				log(Level.INFO, "A new version of TitanChat is available! (" + lVersion.getTitle() + ")");
				log(Level.INFO, "You are running " + rVersion.getTitle() + " currently");
				
				if (!lVersion.getDownloadLink().isEmpty())
					log(Level.INFO, "Get files at " + lVersion.getDownloadLink());
				else
					log(Level.INFO, "Get files at http://dev.bukkit.org/bukkit-plugins/titanchat/files/");
			}
		});
	}
	
	private void initiateMetrics() {
		log(Level.INFO, "Attempting to initialise metrics...");
		
		if (!getConfig().getBoolean("tools.metrics-statistics", false)) {
			log(Level.INFO, "Metrics is disabled");
			return;
		}
		
		try {
			Metrics metrics = new Metrics(this);
			
			if (metrics.isOptOut()) {
				log(Level.INFO, "Metrics is disabled");
				return;
			}
			
			if (metrics.start())
				log(Level.INFO, "Metrics is initialised");
			
		} catch (Exception e) {
			log(Level.WARNING, "An error occured while initialising Metrics");
		}
	}
	
	public static TitanChat instance() {
		if (instance == null)
			throw new IllegalStateException("TitanChat is not in operation");
		
		return instance;
	}
	
	public void log(Level level, String message) {
		Validate.notEmpty(message, "Message cannot be empty");
		log.log((level != null) ? level : Level.INFO, message);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!system.isLoaded(CommandManager.class))
			throw new IllegalStateException("CommandManager not found");
		
		return system.getModule(CommandManager.class).run(sender, label, args);
	}
	
	@Override
	public void onDisable() {
		log(Level.INFO, "Now disabling...");
		
		log(Level.INFO, "Stopping TitanChat System...");
		system.stop();
		
		if (instance != null)
			instance = null;
		
		log(Level.INFO, "Now disabled");
	}
	
	@Override
	public void onEnable() {
		log(Level.INFO, "Now enabling...");
		
		if (instance == null)
			instance = this;
		
		enquireUpdate();
		
		log(Level.INFO, "Attempting to set up VaultUtils...");
		
		if (!Vault.initialise(getServer()))
			log(Level.INFO, "Failed to set up VaultUtils");
		
		log(Level.INFO, "Starting TitanChat System...");
		system.start();
		
		getServer().getPluginManager().registerEvents(new TitanChatListener(), this);
		log(Level.INFO, "Registered listeners");
		
		log(Level.INFO, "Now enabled");
	}
	
	@Override
	public void onLoad() {
		log(Level.INFO, "Now loading...");
		
		instance = this;
		
		if (!new File(getDataFolder(), "config.yml").exists())
			log(Level.INFO, "Generating default configuration...");
			
		saveResource("config.yml", false);
		
		log(Level.INFO, "Registering managers...");
		system.registerModule(new CommandManager());
		system.registerModule(new SimpleGuide());
		system.registerModule(new SimpleNetwork());
		system.registerModule(new UserManager());
		
		log(Level.INFO, "Now loaded");
	}
	
	public void onReload() {
		log(Level.INFO, "Now reloading...");
		
		if (instance == null)
			instance = this;
		
		system.reload();
		
		log(Level.INFO, "Now reloaded");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!system.isLoaded(CommandManager.class))
			throw new IllegalStateException("CommandManager not found");
		
		return system.getModule(CommandManager.class).preview(sender, label, args);
	}
	
	public static ModularSystem system() {
		return instance.getSystem();
	}
}