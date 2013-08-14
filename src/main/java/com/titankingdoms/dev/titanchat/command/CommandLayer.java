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

public abstract class CommandLayer implements CommandBase {
	
	private final String name;
	
	private final Map<String, CommandBase> commands;
	
	public CommandLayer(String name) {
		this.name = name;
		this.commands = new TreeMap<String, CommandBase>();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof CommandLayer)
			return toString().equals(object.toString());
		
		return false;
	}
	
	@Override
	public final void execute(CommandSender sender, String[] args) {
		if (args.length < 1 || !hasNextLayer(args[0]))
			return;
		
		getNextLayer(args[0]).execute(sender, Arrays.copyOfRange(args, 1, args.length));
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	protected final CommandBase getNextLayer(String name) {
		return (hasNextLayer(name)) ? commands.get(name.toLowerCase()) : null;
	}
	
	protected final List<CommandBase> getNextLayers() {
		return new ArrayList<CommandBase>(commands.values());
	}
	
	protected final boolean hasNextLayer(String name) {
		return (name != null) ? commands.containsKey(name.toLowerCase()) : false;
	}
	
	protected final boolean hasNextLayer(CommandBase command) {
		if (command == null || !hasNextLayer(command.getName()))
			return false;
		
		return getNextLayer(command.getName()).equals(command);
	}
	
	protected final void registerNextLayer(CommandBase command) {
		if (command == null || hasNextLayer(command))
			return;
		
		this.commands.put(command.getName().toLowerCase(), command);
	}
	
	@Override
	public final List<String> tab(CommandSender sender, String[] args) {
		List<String> tabCompletions = new ArrayList<String>();
		
		switch (args.length) {
		
		case 0:
			tabCompletions.addAll(commands.keySet());
			break;
			
		case 1:
			for (String name : commands.keySet()) {
				if (!name.startsWith(args[0]))
					continue;
				
				tabCompletions.add(name);
			}
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
	
	protected final void unregisterNextLayer(CommandBase command) {
		if (command == null || !hasNextLayer(command))
			return;
		
		this.commands.remove(command.getName().toLowerCase());
	}
}