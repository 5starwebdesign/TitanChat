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

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.Manager;
import com.titankingdoms.dev.titanchat.TitanChat;

public final class CommandManager implements Manager<Command> {
	
	private final TitanChat plugin;
	
	private final MainCommand command;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		this.command = new MainCommand();
	}
	
	public void execute(CommandSender sender, String[] args) {
		command.execute(sender, args);
	}
	
	public Command get(String name) {
		return command.getLayer().get(name);
	}
	
	@Override
	public List<Command> getAll() {
		return command.getLayer().getAll();
	}
	
	public Command getCommand(String name) {
		return get(name);
	}
	
	public List<Command> getCommands() {
		return getAll();
	}
	
	@Override
	public String getName() {
		return "CommandManager";
	}
	
	@Override
	public String getStatusMessage() {
		if (getAll().isEmpty())
			return "No commands loaded";
		
		return "Commands loaded: " + StringUtils.join(match(""), ", ");
	}
	
	@Override
	public boolean has(String name) {
		return command.getLayer().has(name);
	}
	
	@Override
	public boolean has(Command command) {
		return command.getLayer().has(command);
	}
	
	public boolean hasCommand(String name) {
		return has(name);
	}
	
	public boolean hasCommand(Command command) {
		return has(command);
	}
	
	@Override
	public void load() {}
	
	@Override
	public List<String> match(String name) {
		return command.getLayer().match(name);
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
			
			command.getLayer().registerAll(command);
		}
	}
	
	@Override
	public void reload() {}
	
	public List<String> tab(CommandSender sender, String[] args) {
		return command.tab(sender, args);
	}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Command command) {
		command.getLayer().unregister(command);
	}
	
	private final class MainCommand extends Command {
		
		public MainCommand() {
			super("TitanChat");
			setAliases(plugin.getCommand("titanchat").getAliases().toArray(new String[0]));
			setArgumentRange(0, 256);
			setDescription(plugin.getCommand("titanchat").getDescription());
		}
		
		@Override
		public void execute(CommandSender sender, String[] args) {
			getLayer().execute(sender, args);
		}
		
		@Override
		public List<String> tab(CommandSender sender, String[] args) {
			return getLayer().tab(sender, args);
		}
	}
}