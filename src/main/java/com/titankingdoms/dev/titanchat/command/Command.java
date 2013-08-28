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

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.util.Messaging;

public abstract class Command {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private String[] aliases;
	private String description;
	private int maxArgs;
	private int minArgs;
	
	private final Layer layers;
	
	public Command(String name, String[] aliases, String description) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(StringUtils.isAlphanumeric(name), "Name cannot contain non-alphanumeric characters");
		
		this.plugin = TitanChat.getInstance();
		this.name = (name != null) ? name : "";
		this.aliases = (aliases != null) ? aliases : new String[0];
		this.description = (description != null) ? description : "";
		this.maxArgs = 0;
		this.minArgs = 0;
		this.layers = new Layer();
	}
	
	public Command(String name, String description, String... aliases) {
		this(name, aliases, description);
	}
	
	public Command(String name, String[] aliases) {
		this(name, aliases, "");
	}
	
	public Command(String name, String description) {
		this(name, new String[0], description);
	}
	
	public Command(String name) {
		this(name, new String[0], "");
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Command)
			return toString().equals(object.toString());
		
		return false;
	}
	
	public abstract void execute(CommandSender sender, String[] args);
	
	public String[] getAliases() {
		return aliases;
	}
	
	public String getDescription() {
		return description;
	}
	
	protected final Layer getLayer() {
		return layers;
	}
	
	public int getMaxArguments() {
		return maxArgs;
	}
	
	public int getMinArguments() {
		return minArgs;
	}
	
	public final String getName() {
		return name;
	}
	
	public boolean isPermitted(CommandSender sender, String[] args) {
		return true;
	}
	
	protected void setAliases(String... aliases) {
		this.aliases = (aliases != null) ? aliases : new String[0];
	}
	
	protected void setArgumentRange(int minArgs, int maxArgs) {
		this.maxArgs = (maxArgs >= minArgs) ? maxArgs : minArgs;
		this.minArgs = (minArgs >= 0) ? minArgs : 0;
	}
	
	protected void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
	
	public List<String> tab(CommandSender sender, String[] args) {
		return new ArrayList<String>();
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
	
	public final class Layer implements Manager<Command> {
		
		protected final TitanChat plugin;
		
		private final Map<String, Command> commands;
		
		public Layer() {
			this.plugin = TitanChat.getInstance();
			this.commands = new TreeMap<String, Command>();
		}
		
		public void execute(CommandSender sender, String[] args) {
			if (args.length < 1)
				return;
			
			if (!has(args[0])) {
				Messaging.sendNotice(sender, "&4Invalid command");
				return;
			}
			
			Command next = get(args[0]);
			String[] arguments = Arrays.copyOfRange(args, 1, args.length);
			
			if (arguments.length < next.getMinArguments() || arguments.length > next.getMaxArguments()) {
				Messaging.sendNotice(sender, "&4Invalid argument length");
				return;
			}
			
			if (!next.isPermitted(sender, arguments)) {
				Messaging.sendNotice(sender, "&4You do not have permission");
				return;
			}
			
			next.execute(sender, arguments);
		}
		
		@Override
		public Command get(String name) {
			return (has(name)) ? commands.get(name.toLowerCase()) : null;
		}
		
		@Override
		public List<Command> getAll() {
			return new ArrayList<Command>(commands.values());
		}
		
		@Override
		public String getName() {
			return "Layer";
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
		
		@Override
		public void load() {}
		
		@Override
		public List<String> match(String name) {
			if (name == null || name.isEmpty())
				return new ArrayList<String>(commands.keySet());
			
			List<String> matches = new ArrayList<String>();
			
			for (String command : commands.keySet()) {
				if (!command.startsWith(name.toLowerCase()))
					continue;
				
				matches.add(command);
			}
			
			Collections.sort(matches);
			
			return matches;
		}
		
		@Override
		public void registerAll(Command... commands) {
			if (commands == null)
				return;
			
			for (Command command : commands) {
				if (command == null || has(command))
					continue;
				
				this.commands.put(command.getName().toLowerCase(), command);
			}
		}
		
		@Override
		public void reload() {}
		
		public List<String> tab(CommandSender sender, String[] args) {
			if (args.length < 2)
				return match(args[0]);
			
			if (has(args[0]))
				return get(args[0]).tab(sender, Arrays.copyOfRange(args, 1, args.length));
			
			return new ArrayList<String>();
		}
		
		@Override
		public void unload() {}
		
		@Override
		public void unregister(Command command) {
			if (command == null || !has(command))
				return;
			
			this.commands.remove(command.getName().toLowerCase());
		}
	}
}