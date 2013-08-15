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

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.Manager;
import com.titankingdoms.dev.titanchat.TitanChat;

public final class CommandManager extends Command implements Manager<Command> {
	
	private final TitanChat plugin;
	
	public CommandManager() {
		super("TitanChat");
		this.plugin = TitanChat.getInstance();
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		getLayer().execute(sender, args);
	}
	
	@Override
	public Command get(String name) {
		return getLayer().get(name);
	}
	
	@Override
	public String[] getAliases() {
		return plugin.getCommand("titanchat").getAliases().toArray(new String[0]);
	}
	
	@Override
	public List<Command> getAll() {
		return getLayer().getAll();
	}
	
	public Command getCommand(String name) {
		return get(name);
	}
	
	public List<Command> getCommands() {
		return getAll();
	}
	
	@Override
	public String getDescription() {
		return plugin.getCommand("titanchat").getDescription();
	}
	
	@Override
	public int getMaxArguments() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int getMinArguments() {
		return 0;
	}
	
	@Override
	public boolean has(String name) {
		return getLayer().has(name);
	}
	
	@Override
	public boolean has(Command command) {
		return getLayer().has(command);
	}
	
	public boolean hasCommand(String name) {
		return has(name);
	}
	
	public boolean hasCommand(Command command) {
		return has(command);
	}
	
	@Override
	public boolean isPermitted(CommandSender sender, String[] args) {
		return true;
	}
	
	@Override
	public void load() {}
	
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
			
			getLayer().registerAll(command);
		}
	}
	
	@Override
	public void reload() {}
	
	@Override
	public List<String> tab(CommandSender sender, String[] args) {
		return getLayer().tab(sender, args);
	}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Command command) {
		getLayer().unregister(command);
	}
}