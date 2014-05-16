/*
 *     Copyright (C) 2014  Nodin Chan
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

package com.nodinchan.dev.titanchat.api.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.nodinchan.dev.module.AbstractModule;
import com.nodinchan.dev.titanchat.TitanChat;
import com.nodinchan.dev.titanchat.api.command.guide.CommandIndex;
import com.nodinchan.dev.titanchat.api.event.CommandEvent;
import com.nodinchan.dev.titanchat.api.guide.SimpleGuide;
import com.nodinchan.dev.titanchat.tools.Format;
import com.nodinchan.dev.titanchat.tools.Messaging;

public final class CommandManager extends AbstractModule {
	
	private final TitanChat plugin;
	
	private static final String[] DEPENDENCIES = new String[] { "Guide" };
	
	private final Map<String, Command> commands;
	
	private final CommandIndex index;
	
	public CommandManager() {
		super("CommandManager");
		this.plugin = TitanChat.instance();
		this.commands = new TreeMap<>();
		this.index = new CommandIndex();
	}
	
	public Command get(String label) {
		return (label == null || label.isEmpty()) ? null : commands.get(label.toLowerCase());
	}
	
	public List<Command> getAll() {
		return ImmutableList.copyOf(commands.values());
	}
	
	@Override
	public String[] getDependencies() {
		return DEPENDENCIES;
	}
	
	public CommandIndex getIndex() {
		return index;
	}
	
	public boolean has(String label) {
		return label != null && !label.isEmpty() && commands.containsKey(label.toLowerCase());
	}
	
	public boolean has(Command command) {
		return command != null && has(command.getLabel()) && get(command.getLabel()).equals(command);
	}
	
	@Override
	public void load() {
		plugin.getSystem().getModule(SimpleGuide.class).addChapter(index);
	}
	
	public List<String> match(String label) {
		if (label == null || label.isEmpty())
			return ImmutableList.copyOf(commands.keySet());
		
		Builder<String> matches = ImmutableList.builder();
		
		for (String match : commands.keySet()) {
			if (!match.startsWith(label))
				continue;
			
			matches.add(match);
		}
		
		return matches.build();
	}
	
	public List<String> preview(CommandSender sender, String label, String[] args) {
		switch (args.length) {
		
		case 0:
			return match(label);
			
		default:
			if (!has(label))
				return ImmutableList.of();
			
			return get(label).invokeTabCompletion(sender, regroup(args));
		}
	}
	
	public void register(Command command) {
		Validate.notNull(command, "Command cannot be null");
		Validate.isTrue(!has(command), "Command already registered");
		
		commands.put(command.getLabel().toLowerCase(), command);
		
		for (String alias : command.getAliases()) {
			if (alias.matches(".*\\W+.*"))
				continue;
			
			if (has(alias) && get(alias).getLabel().equalsIgnoreCase(alias))
				continue;
			
			commands.put(alias.toLowerCase(), command);
		}
		
		plugin.getServer().getPluginManager().callEvent(new CommandEvent(command, "Register"));
		
		index.index();
	}
	
	public static String[] regroup(String[] args) {
		if (args == null || args.length < 1)
			return new String[0];
		
		List<String> arguments = new ArrayList<>();
		
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
			Messaging.message(sender, Format.RED + "Invalid command");
			return false;
		}
		
		Command command = get(label);
		String[] arguments = regroup(args);
		
		if (!command.isPermittedExecution(sender, arguments)) {
			Messaging.message(sender, Format.RED + "You do not have permission");
			return true;
		}
		
		if (arguments.length > 0 && arguments[0].equals("?") && !command.isRegistered(arguments[0])) {
			command.invokeAssistanceDisplay(sender, Arrays.copyOfRange(arguments, 1, args.length));
			return true;
		}
		
		if (arguments.length < command.getMinArguments() || arguments.length > command.getMaxArguments()) {
			Messaging.message(sender, Format.RED + "Invalid argument length");
			Messaging.message(sender, "Syntax: " + command.getCanonicalSyntax());
			return true;
		}
		
		command.invokeExecution(sender, arguments);
		return true;
	}
	
	@Override
	public void unload() {
		for (Command command : getAll())
			unregister(command.getLabel());
		
		plugin.getSystem().getModule(SimpleGuide.class).removeChapter("Commands");
	}
	
	public void unregister(String label) {
		Validate.notEmpty(label, "Label cannot be empty");
		Validate.isTrue(has(label), "Command not registered");
		
		Command command = commands.remove(label.toLowerCase());
		
		for (String alias : command.getAliases()) {
			if (alias.matches(".*\\W+.*"))
				continue;
			
			if (has(alias) && get(alias).getLabel().equalsIgnoreCase(alias))
				continue;
			
			commands.remove(alias.toLowerCase());
		}
		
		plugin.getServer().getPluginManager().callEvent(new CommandEvent(command, "Unregister"));
		
		index.index();
	}
}