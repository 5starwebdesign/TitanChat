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

package com.titankingdoms.dev.titanchat.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import com.titankingdoms.dev.titanchat.Manager;
import com.titankingdoms.dev.titanchat.TitanChat;

public final class CommandManager implements Manager<Command> {
	
	private final TitanChat plugin;
	
	private final Map<String, Command> commands;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		this.commands = new TreeMap<String, Command>();
	}
	
	@Override
	public Command get(String name) {
		return (name != null) ? commands.get(name.toLowerCase()) : null;
	}
	
	@Override
	public List<Command> getAll() {
		return new ArrayList<Command>(commands.values());
	}
	
	public Command getCommand(String name) {
		return get(name);
	}
	
	public List<Command> getCommands() {
		return getAll();
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? commands.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(Command command) {
		if (command == null || !has(command.getName()))
			return false;
		
		return get(command.getName()).equals(command);
	}
	
	public boolean hasCommand(String name) {
		return has(name);
	}
	
	public boolean hasCommand(Command command) {
		return has(command);
	}
	
	@Override
	public void load() {
		
	}
	
	@Override
	public void registerAll(Command... commands) {
		if (commands == null)
			return;
		
		for (Command command : commands) {
			if (command == null)
				continue;
			
			if (has(command.getName())) {
				plugin.log(Level.INFO, "Duplicate: " + command);
				continue;
			}
			
			this.commands.put(command.getName().toLowerCase(), command);
		}
	}
	
	@Override
	public void reload() {
		
	}
	
	@Override
	public void unload() {
		
	}
	
	@Override
	public void unregister(Command command) {
		if (command == null || !has(command))
			return;
		
		this.commands.remove(command.getName().toLowerCase());
	}
}