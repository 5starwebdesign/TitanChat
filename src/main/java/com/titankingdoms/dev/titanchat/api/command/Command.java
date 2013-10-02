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

package com.titankingdoms.dev.titanchat.api.command;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.tools.Messaging;
import com.titankingdoms.dev.titanchat.tools.util.VaultUtils;

public abstract class Command implements Manager<Command> {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private String[] aliases;
	private String description;
	private int maxArgs;
	private int minArgs;
	
	private final Map<String, Command> commands;
	
	public Command(String name, String[] aliases, String description) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(StringUtils.isAlphanumeric(name), "Name cannot contain non-alphanumeric characters");
		
		this.plugin = TitanChat.getInstance();
		this.name = (name != null) ? name : "";
		this.aliases = (aliases != null) ? aliases : new String[0];
		this.description = (description != null) ? description : "";
		this.maxArgs = 0;
		this.minArgs = 0;
		this.commands = new TreeMap<String, Command>();
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
		return (object instanceof Command) ? toString().equals(object.toString()) : false;
	}
	
	public abstract void execute(CommandSender sender, String[] args, CommandData data);
	
	public final void execute(CommandSender sender, String[] args) {
		execute(sender, args, new CommandData());
	}
	
	public void executeLayer(CommandSender sender, String[] args, CommandData data) {
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
		
		if (!next.isPermitted(sender, arguments, data)) {
			Messaging.sendNotice(sender, "&4You do not have permission");
			return;
		}
		
		next.execute(sender, arguments, data);
	}
	
	public final void executeLayer(CommandSender sender, String[] args) {
		executeLayer(sender, args, new CommandData());
	}
	
	@Override
	public Command get(String name) {
		return (has(name)) ? commands.get(name.toLowerCase()) : null;
	}
	
	public String[] getAliases() {
		return aliases;
	}
	
	@Override
	public Collection<Command> getAll() {
		return new HashSet<Command>(commands.values());
	}
	
	public String getDescription() {
		return description;
	}
	
	public final String getLabel() {
		return name;
	}
	
	public int getMaxArguments() {
		return maxArgs;
	}
	
	public int getMinArguments() {
		return minArgs;
	}
	
	@Override
	public String getName() {
		return getLabel();
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? commands.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(Command command) {
		return (command != null && has(command.getLabel())) ? get(command.getLabel()).equals(command) : false;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public final boolean hasPermission(CommandSender sender, String node) {
		return VaultUtils.hasPermission(sender, node);
	}
	
	@Override
	public void init() {}
	
	public boolean isPermitted(CommandSender sender, String[] args, CommandData data) {
		return true;
	}
	
	public final boolean isPermitted(CommandSender sender, String[] args) {
		return isPermitted(sender, args, new CommandData());
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
			if (command == null)
				continue;
			
			String name = command.getLabel();
			
			if (has(name) && get(name).getLabel().equals(name))
				continue;
			
			this.commands.put(command.getLabel().toLowerCase(), command);
			
			for (String alias : command.getAliases()) {
				if (has(alias))
					continue;
				
				this.commands.put(alias.toLowerCase(), command);
			}
		}
	}
	
	@Override
	public void reload() {}
	
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
	
	public List<String> tab(CommandSender sender, String[] args, CommandData data) {
		return new ArrayList<String>();
	}
	
	public final List<String> tab(CommandSender sender, String[] args) {
		return tab(sender, args, new CommandData());
	}
	
	public List<String> tabLayer(CommandSender sender, String[] args, CommandData data) {
		if (args.length < 2)
			return (args.length > 0) ? match(args[0]) : new ArrayList<String>();
		
		if (has(args[0])) {
			String[] arguments = Arrays.copyOfRange(args, 1, args.length);
			return get(args[0]).tab(sender, arguments, new CommandData());
		}
		
		return new ArrayList<String>();
	}
	
	public final List<String> tabLayer(CommandSender sender, String[] args) {
		return tabLayer(sender, args, new CommandData());
	}
	
	@Override
	public String toString() {
		return "\"Command\": {" +
				"\"label\": \"" + getLabel() + "\", " +
				"\"aliases\": [\"" + StringUtils.join(getAliases(), "\", \"") + "\"], " +
				"\"description\": \"" + getDescription() + "\", " +
				"\"range\": {" +
				"\"minArgs\": \"" + getMinArguments() + "\", " +
				"\"maxArgs\": " + getMaxArguments() + "\"" +
				"}" +
				"}";
	}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Command command) {
		if (command == null || !has(command))
			return;
		
		this.commands.remove(command.getLabel().toLowerCase());
		
		for (String alias : command.getAliases()) {
			if (!has(alias) && !get(alias).equals(command))
				continue;
			
			this.commands.remove(alias);
		}
	}
	
	public class CommandData {
		
		private final Map<String, String> data;
		
		public CommandData() {
			this.data = new HashMap<String, String>();
		}
		
		public String getData(String key, String def) {
			return (hasData(key)) ? data.get(key) : def;
		}
		
		public String getData(String key) {
			return getData(key, "");
		}
		
		public boolean hasData() {
			return !data.isEmpty();
		}
		
		public boolean hasData(String key) {
			return (key != null) ? data.containsKey(key) : false;
		}
		
		public void setData(String key, String value) {
			if (key == null)
				return;
			
			if (value == null || value.isEmpty())
				data.remove(key);
			else
				data.put(key, value);
		}
	}
}