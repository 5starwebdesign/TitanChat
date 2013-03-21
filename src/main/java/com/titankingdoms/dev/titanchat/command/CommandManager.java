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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.command.defaults.*;
import com.titankingdoms.dev.titanchat.loading.Loader;

public final class CommandManager {
	
	private final TitanChat plugin;
	
	private final Map<String, Command> commands;
	private final Map<String, Command> labels;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getCommandDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating commands directory...");
		
		this.commands = new HashMap<String, Command>();
		this.labels = new HashMap<String, Command>();
	}
	
	public Command getCommand(String label) {
		return labels.get(label.toLowerCase());
	}
	
	public File getCommandDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "commands");
	}
	
	public boolean hasCommand(String name) {
		return commands.containsKey(name.toLowerCase());
	}
	
	public boolean hasCommand(Command command) {
		return hasCommand(command.getName());
	}
	
	public boolean hasLabel(String label) {
		return labels.containsKey(label.toLowerCase());
	}
	
	public void load() {
		registerCommands(
				new BlacklistCommand(), new KickCommand(), new PardonCommand(),
				new InviteCommand(),
				new JoinCommand(), new LeaveCommand(),
				new SendCommand()
		);
		
		registerCommands(Loader.load(Command.class, getCommandDirectory()).toArray(new Command[0]));
	}
	
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
		}
	}
	
	public void reload() {
		this.commands.clear();
		
		registerCommands(
				new InviteCommand(),
				new JoinCommand(),
				new LeaveCommand()
		);
	}
	
	public void unload() {
		this.commands.clear();
	}
}