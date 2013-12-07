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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.utility.FormatUtils;
import com.titankingdoms.dev.titanchat.utility.Messaging;

public abstract class Command {
	
	protected final TitanChat plugin;
	
	private final String label;
	
	private String[] aliases = new String[0];
	private String description = "";
	private String syntax = "";
	
	private int maxArgs = 0;
	private int minArgs = 0;
	
	private final Map<String, Command> commands;
	
	private boolean registered = false;
	
	public Command(String label) {
		Validate.isTrue(StringUtils.isNotBlank(label), "Label cannot be blank");
		Validate.isTrue(StringUtils.isAlphanumeric(label), "Label cannot be non-alphanumerical");
		
		this.plugin = TitanChat.getInstance();
		this.label = label;
		this.commands = new TreeMap<String, Command>();
	}
	
	public String buildSyntax(CommandSender sender, String[] args) {
		return syntax;
	}
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof Command) ? toString().equals(object.toString()) : false;
	}
	
	public abstract boolean execute(CommandSender sender, String[] args);
	
	public Command get(String name) {
		return (has(name)) ? commands.get(name) : null;
	}
	
	public String[] getAliases() {
		return aliases.clone();
	}
	
	public List<Command> getAll() {
		return new ArrayList<Command>(commands.values());
	}
	
	public String getDescription() {
		return description;
	}
	
	public final String getLabel() {
		return label;
	}
	
	public int getMaxArguments() {
		return maxArgs;
	}
	
	public int getMinArguments() {
		return minArgs;
	}
	
	public String getSyntax() {
		return syntax;
	}
	
	public boolean has(String name) {
		return (name != null && !name.isEmpty()) ? commands.containsKey(name.toLowerCase()) : false;
	}
	
	public boolean has(Command command) {
		return (command != null && has(command.getLabel())) ? get(command.getLabel()).equals(command) : false;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	protected final boolean invokeLayeredExecution(CommandSender sender, String[] args) {
		if (args.length < 1)
			return false;
		
		if (!has(args[0]))
			return false;
		
		Command next = get(args[0]);
		String[] arguments = Arrays.copyOfRange(args, 1, args.length);
		
		if (arguments.length < next.getMinArguments() || arguments.length > next.getMaxArguments()) {
			Messaging.message(sender, FormatUtils.RED + "Invalid argument length");
			return false;
		}
		
		next.execute(sender, arguments);
		return true;
	}
	
	protected final String invokeLayeredSyntaxBuilder(CommandSender sender, String[] args) {
		String syntax = label + " ";
		
		if (args.length > 0 && has(args[0]))
			syntax += args[0] + " " + get(args[0]).buildSyntax(sender, Arrays.copyOfRange(args, 1, args.length));
		else
			syntax += this.syntax;
		
		return syntax.toLowerCase();
	}
	
	protected final List<String> invokeLayeredTabCompletion(CommandSender sender, String[] args) {
		switch (args.length) {
		
		case 0:
			return new ArrayList<String>();
		
		case 1:
			return match(args[0]);
			
		default:
			if (!has(args[0]))
				return new ArrayList<String>();
			
			return get(args[0]).tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
		}
	}
	
	protected List<String> match(String name) {
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
	
	protected void register(Command command) {
		Validate.notNull(command, "Command cannot be null");
		
		if (has(command) || command.registered)
			return;
		
		this.commands.put(command.getLabel().toLowerCase(), command);
		
		for (String alias : command.getAliases()) {
			if (has(alias))
				continue;
			
			this.commands.put(alias.toLowerCase(), command);
		}
		
		command.registered = true;
	}
	
	protected void setAliases(String... aliases) {
		this.aliases = (aliases != null) ? aliases : new String[0];
	}
	
	protected void setArgumentRange(int minArgs, int maxArgs) {
		this.minArgs = (minArgs >= 0) ? minArgs : 0;
		this.maxArgs = (maxArgs >= minArgs) ? maxArgs : this.minArgs;
	}
	
	protected void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
	
	protected void setSyntax(String syntax) {
		this.syntax = (syntax != null) ? syntax : "";
	}
	
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return new ArrayList<String>();
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
				"}, " +
				"\"syntax\": \"" + getSyntax() + "\"" +
				"}";
	}
	
	protected void unregister(Command command) {
		Validate.notNull(command, "Command cannot be null");
		
		if (!has(command) || !command.registered)
			return;
		
		this.commands.remove(command.getLabel().toLowerCase());
		
		for (String alias : command.getAliases()) {
			if (has(alias) && !get(alias).equals(command))
				continue;
			
			this.commands.remove(alias.toLowerCase());
		}
		
		command.registered = false;
	}
	
	public boolean validatePermissions(CommandSender sender, String[] args) {
		return true;
	}
}