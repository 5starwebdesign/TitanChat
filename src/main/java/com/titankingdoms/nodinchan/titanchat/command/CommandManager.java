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

package com.titankingdoms.nodinchan.titanchat.command;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nodinchan.ncbukkit.loader.Loader;
import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.command.commands.*;
import com.titankingdoms.nodinchan.titanchat.command.info.Command;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

public final class CommandManager {
	
	private final TitanChat plugin;
	
	private static final Debugger db = new Debugger(3);
	
	private ShortcutManager shortcuts;
	
	private final List<String> commands;
	
	private final Map<String, Executor> executors;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getCommandDir().mkdir())
			plugin.log(Level.INFO, "Creating commands directory...");
		
		this.shortcuts = new ShortcutManager();
		this.commands = new LinkedList<String>();
		this.executors = new LinkedHashMap<String, Executor>();
	}
	
	public void execute(CommandSender sender, String command, String chName, String[] args) {
		if (hasCommand(command)) {
			Executor executor = getCommandExecutor(command);
			
			if (!(sender instanceof Player) && !executor.allowConsoleUsage()) {
				plugin.send(MessageLevel.WARNING, sender, "Please use this command in game");
				return;
			}
			
			Channel channel = null;
			
			if (chName == null || chName.isEmpty()) {
				if (sender instanceof Player)
					channel = plugin.getManager().getChannelManager().getChannel((Player) sender);
				
			} else {
				if (plugin.getManager().getChannelManager().existsByAlias(chName))
					channel = plugin.getManager().getChannelManager().getChannelByAlias(chName);
				
				if (executor.requireChannel() && channel == null) {
					plugin.send(MessageLevel.WARNING, sender, "No such channel");
					return;
				}
			}
			
			if (executor.requireChannel() && channel == null) {
				if (sender instanceof Player)
					plugin.send(MessageLevel.WARNING, sender, "Please specify a channel or join a channel");
				else
					plugin.send(MessageLevel.WARNING, sender, "Please specify a channel");
				
				executor.getCommand().usage(sender, command);
				return;
			}
			
			try {
				if (executor.allowConsoleUsage())
					executor.execute(sender, channel, args);
				else
					executor.execute((Player) sender, channel, args);
				
			} catch (IllegalAccessException e) {
				plugin.send(MessageLevel.WARNING, sender, "An error seems to have occured, please check console");
				plugin.log(Level.SEVERE, "An IllegalAccessException has occured while using command: " + executor.getName());
				
				if (db.isDebugging())
					e.printStackTrace();
				
			} catch (IllegalArgumentException e) {
				plugin.send(MessageLevel.WARNING, sender, "An error seems to have occured, please check console");
				plugin.log(Level.SEVERE, "An IllgealArgumentException has occured while using command: " + executor.getName());
				
				if (db.isDebugging())
					e.printStackTrace();
				
			} catch (InvocationTargetException e) {
				plugin.send(MessageLevel.WARNING, sender, "An error seems to have occured, please check console");
				plugin.log(Level.SEVERE, "An InvocationTargetException has occured while using command: " + executor.getName());
				
				if (db.isDebugging()) {
					e.printStackTrace();
					e.getTargetException().printStackTrace();
				}
			}
			
			return;
		}
		
		plugin.send(MessageLevel.WARNING, sender, "Invalid Command");
		plugin.send(MessageLevel.INFO, sender, "\"/titanchat commands [page]\" for command list");
	}
	
	public int getCommandAmount() {
		return executors.values().size();
	}
	
	public File getCommandDir() {
		return new File(plugin.getManager().getAddonManager().getAddonDir(), "commands");
	}
	
	public Executor getCommandExecutor(String alias) {
		return executors.get(alias.toLowerCase());
	}
	
	public List<String> getCommands() {
		return new LinkedList<String>(commands);
	}
	
	public boolean hasCommand(String alias) {
		return executors.containsKey(alias.toLowerCase());
	}
	
	public ShortcutManager getShortcutManager() {
		return shortcuts;
	}
	
	public void load() {
		register(
				new AdministrationCommand(),
				new ChannelCommand(),
				new ChatCommand(),
				new InformationCommand(),
				new InvitationCommand(),
				new PluginCommand(),
				new RankingCommand(),
				new SettingsCommand()
		);
		
		for (CommandBase command : new Loader<CommandBase>(plugin, getCommandDir()).load()) { register(command); }
		
		sortCommands();
		Collections.sort(commands);
	}
	
	public void postReload() {
		load();
	}
	
	public void preReload() {
		unload();
	}
	
	public void register(CommandBase... commands) {
		for (CommandBase command : commands) {
			db.i("Try to register command " + command.toString());
			
			for (Method method : command.getClass().getDeclaredMethods()) {
				if (method.isAnnotationPresent(Command.class)) {
					db.i("Adding new executor: " + method.getName());
					
					Executor executor = new Executor(command, method);
					executors.put(executor.getName().toLowerCase(), executor);
					
					for (String alias : executor.getAliases())
						executors.put(alias.toLowerCase(), executor);
					
					this.commands.add(executor.getName());
				}
			}
		}
	}
	
	public void sortCommands() {
		List<Executor> executors = new ArrayList<Executor>(this.executors.values());
		Collections.sort(executors);
		this.executors.clear();
		
		for (Executor executor : executors) {
			this.executors.put(executor.getName().toLowerCase(), executor);
			
			for (String alias : executor.getAliases())
				this.executors.put(alias.toLowerCase(), executor);
		}
	}
	
	public void unload() {
		executors.clear();
	}
}