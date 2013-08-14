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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public abstract class CommandLayer extends Command {
	
	private final Map<String, Command> commands;
	
	public CommandLayer(String name, String[] aliases, String description) {
		super(name, aliases, description);
		this.commands = new TreeMap<String, Command>();
	}
	
	public CommandLayer(String name, String description, String... aliases) {
		this(name, aliases, description);
	}
	
	public CommandLayer(String name, String[] aliases) {
		this(name, aliases, "");
	}
	
	public CommandLayer(String name, String description) {
		this(name, new String[0], description);
	}
	
	public CommandLayer(String name) {
		this(name, new String[0], "");
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof CommandLayer)
			return toString().equals(object.toString());
		
		return false;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 1 || !hasNextLayer(args[0]))
			return;
		
		Command next = getNextLayer(args[0]);
		String[] arguments = Arrays.copyOfRange(args, 1, args.length);
		
		if (arguments.length < next.getMinArguments() || arguments.length > next.getMaxArguments())
			return;
		
		if (!next.isPermitted(sender, arguments))
			return;
		
		next.execute(sender, arguments);
	}
	
	protected final Command getNextLayer(String name) {
		return (hasNextLayer(name)) ? commands.get(name.toLowerCase()) : null;
	}
	
	protected final List<Command> getNextLayers() {
		return new ArrayList<Command>(commands.values());
	}
	
	protected final boolean hasNextLayer(String name) {
		return (name != null) ? commands.containsKey(name.toLowerCase()) : false;
	}
	
	protected final boolean hasNextLayer(Command command) {
		if (command == null || !hasNextLayer(command.getName()))
			return false;
		
		return getNextLayer(command.getName()).equals(command);
	}
	
	@Override
	public boolean isPermitted(CommandSender sender, String[] args) {
		boolean permitted = false;
		
		switch (args.length) {
		
		case 0:
			permitted = true;
			break;
			
		default:
			if (!hasNextLayer(args[0]))
				break;
			
			permitted = getNextLayer(args[0]).isPermitted(sender, Arrays.copyOfRange(args, 1, args.length));
			break;
		}
		
		return permitted;
	}
	
	protected final void registerNextLayer(Command command) {
		if (command == null || hasNextLayer(command))
			return;
		
		this.commands.put(command.getName().toLowerCase(), command);
	}
	
	@Override
	public List<String> tab(CommandSender sender, String[] args) {
		List<String> tabCompletions = new ArrayList<String>();
		
		switch (args.length) {
			
		case 1:
			if (!args[0].isEmpty()) {
				for (String name : commands.keySet()) {
					if (!name.startsWith(args[0]))
						continue;
					
					tabCompletions.add(name);
				}
				
				break;
			}
			
			tabCompletions.addAll(commands.keySet());
			break;
			
		default:
			if (!hasNextLayer(args[0]))
				break;
			
			tabCompletions.addAll(getNextLayer(args[0]).tab(sender, Arrays.copyOfRange(args, 1, args.length)));
			break;
		}
		
		Collections.sort(tabCompletions);
		
		return tabCompletions;
	}
	
	@Override
	public String toString() {
		return "Command: {" +
				"name: " + getName() + ", " +
				"aliases: [" + StringUtils.join(getAliases(), ", ") + "], " +
				"description: " + getDescription() + ", " +
				"range: {" +
				"minArgs: " + getMinArguments() + ", " +
				"maxArgs: " + getMaxArguments() +
				"}" +
				"}";
	}
	
	protected final void unregisterNextLayer(Command command) {
		if (command == null || !hasNextLayer(command))
			return;
		
		this.commands.remove(command.getName().toLowerCase());
	}
}