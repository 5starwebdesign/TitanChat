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

package com.titankingdoms.dev.titanchat.command;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.command.defaults.*;
import com.titankingdoms.dev.titanchat.loading.Loader;
import com.titankingdoms.dev.titanchat.util.Debugger;

/**
 * {@link CommandManager} - Manages {@link Command}s
 * 
 * @author NodinChan
 *
 */
public final class CommandManager {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(2, "CommandManager");
	
	private final Map<String, Command> commands;
	private final Map<String, Command> labels;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getCommandDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating commands directory...");
		
		this.commands = new TreeMap<String, Command>();
		this.labels = new HashMap<String, Command>();
	}
	
	/**
	 * Gets the specified {@link Command}
	 * 
	 * @param label The label of the {@link Command}
	 * 
	 * @return The specified {@link Command} if found, otherwise null
	 */
	public Command getCommand(String label) {
		return labels.get((label != null) ? label.toLowerCase() : "");
	}
	
	/**
	 * Gets the directory that holds the {@link Command}s
	 * 
	 * @return The directory of the {@link Command}s
	 */
	public File getCommandDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "commands");
	}
	
	/**
	 * Gets all {@link Command}s
	 * 
	 * @return All registered {@link Command}s
	 */
	public List<Command> getCommands() {
		return new ArrayList<Command>(commands.values());
	}
	
	/**
	 * Checks if the {@link Command} has been registered
	 * 
	 * @param name The name of the {@link Command}
	 * 
	 * @return True if found
	 */
	public boolean hasCommand(String name) {
		return commands.containsKey((name != null) ? name.toLowerCase() : "");
	}
	
	/**
	 * Checks if the {@link Command} has been registered
	 * 
	 * @param command The {@link Command}
	 * 
	 * @return True if found
	 */
	public boolean hasCommand(Command command) {
		return hasCommand((command != null) ? command.getName() : "");
	}
	
	/**
	 * Checks if the label has been registered for a {@link Command}
	 * 
	 * @param label The label
	 * 
	 * @return True if found
	 */
	public boolean hasLabel(String label) {
		return labels.containsKey((label != null) ? label.toLowerCase() : "");
	}
	
	/**
	 * Loads the manager
	 */
	public void load() {
		registerCommands(
				new CreateCommand(), new DeleteCommand(), new ListCommand(),
				new BlacklistCommand(), new KickCommand(), new PlaceCommand(), new WhitelistCommand(),
				new DemoteCommand(), new PromoteCommand(),
				new FocusCommand(), new JoinCommand(), new LeaveCommand(),
				new DebugCommand(), new HelpCommand(), new ReloadCommand(),
				new ChatCommand(), new EmoteCommand(), new PMCommand(),
				new WhoCommand()
		);
		
		registerCommands(Loader.load(Command.class, getCommandDirectory()).toArray(new Command[0]));
		
		if (!commands.isEmpty())
			plugin.log(Level.INFO, "Commands loaded: " + StringUtils.join(commands.keySet(), ", "));
	}
	
	/**
	 * Registers the {@link Command}s
	 * 
	 * @param commands The {@link Command}s to register
	 */
	public void registerCommands(Command... commands) {
		if (commands == null)
			return;
		
		for (Command command : commands) {
			if (command == null)
				continue;
			
			if (hasCommand(command)) {
				plugin.log(Level.WARNING, "Duplicate command: " + command.getName());
				continue;
			}
			
			this.commands.put(command.getName().toLowerCase(), command);
			this.labels.put(command.getName().toLowerCase(), command);
			
			for (String alias : command.getAliases())
				if (!hasLabel(alias))
					labels.put(alias.toLowerCase(), command);
			
			db.debug(Level.INFO, "Registered command: " + command.getName());
		}
	}
	
	/**
	 * Reloads the manager
	 */
	public void reload() {
		this.commands.clear();
		this.labels.clear();
		
		registerCommands(
				new CreateCommand(), new DeleteCommand(), new ListCommand(),
				new BlacklistCommand(), new KickCommand(), new PlaceCommand(), new WhitelistCommand(),
				new DemoteCommand(), new PromoteCommand(),
				new FocusCommand(), new JoinCommand(), new LeaveCommand(),
				new DebugCommand(), new HelpCommand(), new ReloadCommand(),
				new ChatCommand(), new EmoteCommand(), new PMCommand(),
				new WhoCommand()
		);
		
		registerCommands(Loader.load(Command.class, getCommandDirectory()).toArray(new Command[0]));
		
		if (!commands.isEmpty())
			plugin.log(Level.INFO, "Commands loaded: " + StringUtils.join(commands.keySet(), ", "));
	}
	
	/**
	 * Unloads the manager
	 */
	public void unload() {
		this.commands.clear();
		this.labels.clear();
	}
	
	/**
	 * Unregisters the {@link Command}
	 * 
	 * @param command The {@link Command} to unregister
	 */
	public void unregisterCommand(Command command) {
		if (command == null || !hasCommand(command))
			return;
		
		this.commands.remove(command.getName().toLowerCase());
		this.labels.remove(command.getName().toLowerCase());
		
		for (String alias : command.getAliases())
			if (hasLabel(alias) && !hasCommand(alias))
				this.labels.remove(alias.toLowerCase());
		
		db.debug(Level.INFO, "Unregistered command: " + command.getName());
	}
}