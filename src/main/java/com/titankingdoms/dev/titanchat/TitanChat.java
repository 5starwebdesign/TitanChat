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

package com.titankingdoms.dev.titanchat;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.TitanChatSystem;
import com.titankingdoms.dev.titanchat.api.command.CommandManager;
import com.titankingdoms.dev.titanchat.api.conversation.Network;
import com.titankingdoms.dev.titanchat.api.guide.Enchiridion;
import com.titankingdoms.dev.titanchat.api.metadata.AdapterHandler;
import com.titankingdoms.dev.titanchat.user.UserManager;
import com.titankingdoms.dev.titanchat.listener.TitanChatListener;
import com.titankingdoms.dev.titanchat.tools.release.ReleaseHistory;
import com.titankingdoms.dev.titanchat.tools.release.ReleaseHistory.Version;
import com.titankingdoms.dev.titanchat.utility.VaultUtils;

public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private final Logger log = Logger.getLogger("TitanLog");
	
	private final TitanChatSystem system = new TitanChatSystem();
	
	public <T extends Manager<?>> T getManager(Class<T> manager) {
		return getSystem().getManager(manager);
	}
	
	public TitanChatSystem getSystem() {
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
					log(Level.INFO, "Enquiry Disabled");
					return;
				}
				
				ReleaseHistory history = new ReleaseHistory(35800, getDescription());
				history.searchHistory();
				
				if (history.getVersions().size() < 1) {
					log(Level.INFO, "No releases found");
					return;
				}
				
				Version runningVer = history.getRunningVersion();
				Version latestVer = history.getLatestVersion();
				
				String[] runningVersion = runningVer.getTitle().replaceAll("[^\\d\\.]", "").split("\\.");
				String[] latestVersion = latestVer.getTitle().replaceAll("[^\\d\\.]", "").split("\\.");
				
				for (int versioning = 0; versioning < 4; versioning++) {
					try {
						int running = NumberUtils.toInt(runningVersion[versioning], 0);
						int latest = NumberUtils.toInt(latestVersion[versioning], 0);
						
						if (latest > running)
							break;
						
						if (latest == running && versioning != 3)
							continue;
						
						log(Level.INFO, "No updates available");
						return;
						
					} catch (Exception e) {}
				}
				
				log(Level.INFO, "A new version of TitanChat is available! (" + latestVer.getTitle() + ")");
				log(Level.INFO, "You are running " + runningVer.getTitle() + " currently");
				
				if (!latestVer.getDownloadLink().isEmpty())
					log(Level.INFO, "Get files at " + latestVer.getDownloadLink());
				else
					log(Level.INFO, "Get files at http://dev.bukkit.org/bukkit-plugins/titanchat/files/");
			}
		});
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
		if (!system.hasManager(CommandManager.class))
			throw new UnsupportedOperationException("CommandManager not found");
		
		return getManager(CommandManager.class).run(sender, label, args);
	}
	
	@Override
	public void onDisable() {
		log(Level.INFO, "Now disabling...");
		
		log(Level.INFO, "Shutting down TitanChat System...");
		system.shutdown();
		
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
		
		if (!VaultUtils.initialise(getServer()))
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
		
		log(Level.INFO, "Registering managers...");
		system.registerManager(new AdapterHandler());
		system.registerManager(new CommandManager());
		system.registerManager(new Enchiridion());
		system.registerManager(new Network());
		system.registerManager(new UserManager());
		
		log(Level.INFO, "Now loaded");
	}
	
	public void onReload() {
		log(Level.INFO, "Now reloading...");
		
		if (instance == null)
			instance = this;
		
		log(Level.INFO, "Now reloaded");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!system.hasManager(CommandManager.class))
			throw new UnsupportedOperationException("CommandManager not found");
		
		return getManager(CommandManager.class).preview(sender, label, args);
	}
}