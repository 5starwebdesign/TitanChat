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

package com.titankingdoms.dev.titanchat;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.titankingdoms.dev.titanchat.addon.AddonManager;
import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.command.CommandManager;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelManager;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.core.participant.ParticipantManager;
import com.titankingdoms.dev.titanchat.format.tag.TagManager;
import com.titankingdoms.dev.titanchat.metrics.Metrics;
import com.titankingdoms.dev.titanchat.util.Messaging;
import com.titankingdoms.dev.titanchat.vault.Vault;

public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private final Logger log = Logger.getLogger("TitanLog");
	
	private AddonManager addon;
	private ChannelManager channel;
	private CommandManager command;
	private ParticipantManager participant;
	private TagManager tag;
	
	public AddonManager getAddonManager() {
		return addon;
	}
	
	public ChannelManager getChannelManager() {
		return channel;
	}
	
	public CommandManager getCommandManager() {
		return command;
	}
	
	public ParticipantManager getParticipantManager() {
		return participant;
	}
	
	public static TitanChat getInstance() {
		return instance;
	}
	
	public TagManager getTagManager() {
		return tag;
	}
	
	private boolean initMetrics() {
		if (!getConfig().getBoolean("metrics-statistics", true))
			return true;
		
		try {
			Metrics metrics = new Metrics(this);
			
			if (metrics.isOptOut())
				return true;
			
			return metrics.start();
			
		} catch (Exception e) { return false; }
	}
	
	public void log(Level level, String message) {
		log.log(level, "[TitanChat v4.0] " + message);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String lbl, String[] args) {
		if (cmd.getName().equals("titanchat")) {
			if (args.length < 1 || (args[0].startsWith("@") && args.length < 2)) {
				Messaging.sendMessage(sender, "§5You are running §6" + this);
				Messaging.sendMessage(sender, "§6\"/titanchat help [page]\" for help");
				return true;
			}
			
			String cmdName = "";
			String chName = "";
			
			if (args[0].startsWith("@")) {
				cmdName = args[1];
				chName = args[0].substring(1);
				args = Arrays.copyOfRange(args, 2, args.length);
				
			} else {
				cmdName = args[0];
				args = Arrays.copyOfRange(args, 1, args.length);
			}
			
			Channel ch = null;
			
			if (!chName.isEmpty()) {
				if (!channel.hasChannel(chName)) {
					Messaging.sendMessage(sender, "§4Channel does not exist");
					return true;
				}
				
				ch = channel.getChannel(chName);
				
			} else { ch = participant.getParticipant(sender).getCurrent(); }
			
			onCommand(sender, ch, cmdName, args);
			return true;
		}
		
		return false;
	}
	
	private void onCommand(CommandSender sender, Channel ch, String label, String[] args) {
		if (command.hasLabel(label)) {
			Command cmd = command.getCommand(label);
			
			if (args.length < cmd.getMinArguments() || args.length > cmd.getMaxArguments()) {
				Messaging.sendMessage(sender, "§4Invalid argument length");
				
				String usage = "/titanchat <@[channel]> " + cmd.getName() + " " + cmd.getUsage();
				Messaging.sendMessage(sender, "§6" + usage);
				return;
			}
			
			if (!cmd.permissionCheck(sender, ch)) {
				Messaging.sendMessage(sender, "§4You do not have permission");
				return;
			}
			
			cmd.execute(sender, ch, args);
			return;
		}
		
		Messaging.sendMessage(sender, "§4Invalid command", "§6\"/titanchat help [page]\" for help");
	}
	
	@Override
	public void onDisable() {
		log(Level.INFO, "TitanChat is now disabling...");
		
		log(Level.INFO, "Unloading managers...");
		
		addon.unload();
		channel.unload();
		command.unload();
		participant.unload();
		
		log(Level.INFO, "TitanChat is now disabled");
	}
	
	@Override
	public void onEnable() {
		log(Level.INFO, "TitanChat is now enabling...");
		
		if (instance == null)
			instance = this; 
		
		log(Level.INFO, "Attempting to hook into Vault...");
		
		if (!Vault.initialise(getServer()))
			log(Level.WARNING, "Failed to hook into Vault");
		
		getServer().getPluginManager().registerEvents(new TitanChatListener(), this);
		
		log(Level.INFO, "Registered events");
		
		log(Level.INFO, "Attempting to hook into Metrics...");
		
		if (!initMetrics())
			log(Level.WARNING, "Failed to hook into Metrics");
		
		log(Level.INFO, "Loading managers...");
		
		addon.load();
		channel.load();
		command.load();
		participant.load();
		
		if (channel.getChannels(Status.DEFAULT).isEmpty())
			log(Level.WARNING, "No default channel has been defined");
		
		log(Level.INFO, "TitanChat is now enabled");
	}
	
	@Override
	public void onLoad() {
		log(Level.INFO, "TitanChat is now loading...");
		
		instance = this;
		
		log(Level.INFO, "Initialising managers...");
		
		this.addon = new AddonManager();
		this.channel = new ChannelManager();
		this.command = new CommandManager();
		this.participant = new ParticipantManager();
		this.tag = new TagManager();
		
		if (!new File(getDataFolder(), "config.yml").exists())
			log(Level.INFO, "Generating default config.yml...");
		
		reloadConfig();
		saveConfig();
		
		if (channel.getChannelDirectory().listFiles().length == 0) {
			log(Level.INFO, "Generating default channel configs...");
			saveResource("channels/Default.yml", false);
			saveResource("channels/Global.yml", false);
			saveResource("channels/Local.yml", false);
			saveResource("channels/Staff.yml", false);
			saveResource("channels/World.yml", false);
			saveResource("channels/README.txt", false);
		}
		
		if (!new File(getDataFolder(), "participants.yml").exists())
			log(Level.INFO, "Generating default participants.yml...");
		
		participant.reloadConfig();
		participant.saveConfig();
		
		log(Level.INFO, "TitanChat is now loaded");
	}
	
	public void onReload() {
		log(Level.INFO, "TitanChat is now reloading...");
		
		log(Level.INFO, "Reloading managers...");
		
		addon.reload();
		channel.reload();
		command.reload();
		participant.reload();
		
		log(Level.INFO, "TitanChat is now reloaded");
	}
}