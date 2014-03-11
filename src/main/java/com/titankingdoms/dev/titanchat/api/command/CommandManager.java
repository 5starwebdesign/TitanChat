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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.command.TitanChatCommand;
import com.titankingdoms.dev.titanchat.utility.FormatUtils;
import com.titankingdoms.dev.titanchat.utility.Messaging;

public final class CommandManager implements Manager<Command> {
	
	private final Map<String, Command> commands;
	
	private final Set<String> dependencies = Collections.unmodifiableSet(new HashSet<String>());
	
	public CommandManager() {
		this.commands = new TreeMap<String, Command>();
	}
	
	@Override
	public Command get(String label) {
		return (has(label)) ? commands.get(label) : null;
	}
	
	@Override
	public List<Command> getAll() {
		return new ArrayList<Command>(commands.values());
	}
	
	@Override
	public Set<String> getDependencies() {
		return dependencies;
	}
	
	@Override
	public String getName() {
		return "CommandManager";
	}
	
	@Override
	public boolean has(String label) {
		return label != null && !label.isEmpty() && commands.containsKey(label.toLowerCase());
	}
	
	@Override
	public boolean has(Command command) {
		return command != null && has(command.getLabel()) && get(command.getLabel()).equals(command);
	}
	
	@Override
	public void load() {
		register(new TitanChatCommand());
	}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return new ArrayList<String>(commands.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String match : commands.keySet()) {
			if (!match.startsWith(name))
				continue;
			
			matches.add(match);
		}
		
		Collections.sort(matches);
		return matches;
	}
	
	public List<String> preview(CommandSender sender, String label, String[] args) {
		switch (args.length) {
		
		case 0:
			return match(label);
			
		default:
			if (!has(label))
				return new ArrayList<String>();
			
			return get(label).invokeTabCompletion(sender, regroup(args));
		}
	}
	
	@Override
	public void register(Command command) {
		Validate.notNull(command, "Command cannot be null");
		Validate.isTrue(!has(command), "Command already registered");
		
		this.commands.put(command.getLabel().toLowerCase(), command);
		
		for (String alias : command.getAliases()) {
			if (has(alias))
				continue;
			
			this.commands.put(alias.toLowerCase(), command);
		}
	}
	
	public static String[] regroup(String[] args) {
		if (args == null || args.length < 1)
			return new String[0];
		
		List<String> arguments = new ArrayList<String>();
		
		Matcher match = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(StringUtils.join(args, ' '));
		
		while (match.find())
			arguments.add(match.group().replace("\"", "").trim());
		
		return arguments.toArray(new String[0]);
	}
	
	@Override
	public void reload() {}
	
	public boolean run(CommandSender sender, String label, String[] args) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notEmpty(label, "Command cannot be empty");
		
		if (!has(label)) {
			Messaging.message(sender, FormatUtils.RED + "Invalid command");
			return false;
		}
		
		Command command = get(label);
		String[] arguments = regroup(args);
		
		if (!command.validateAuthorisation(sender, arguments)) {
			Messaging.message(sender, FormatUtils.RED + "You do not have permission");
			return true;
		}
		
		if (arguments.length < command.getMinArguments() || arguments.length > command.getMaxArguments()) {
			Messaging.message(sender, FormatUtils.RED + "Invalid argument length");
			Messaging.message(sender, "Syntax: " + command.getCanonicalSyntax());
			return true;
		}
		
		command.invokeExecution(sender, arguments);
		return true;
	}
	
	@Override
	public void unload() {
		for (Command command : getAll())
			unregister(command);
	}
	
	@Override
	public void unregister(Command command) {
		Validate.notNull(command, "Command cannot be null");
		Validate.isTrue(has(command), "Command not registered");
		
		this.commands.remove(command.getLabel().toLowerCase());
		
		for (String alias : command.getAliases()) {
			if (has(alias) && !get(alias).equals(command))
				continue;
			
			this.commands.remove(alias.toLowerCase());
		}
	}
}