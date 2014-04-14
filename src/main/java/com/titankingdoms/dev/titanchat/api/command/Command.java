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
import com.titankingdoms.dev.titanchat.api.command.AssistanceCommand.Assistance;
import com.titankingdoms.dev.titanchat.api.help.HelpIndex;
import com.titankingdoms.dev.titanchat.utility.Messaging;
import com.titankingdoms.dev.titanchat.utility.FormatUtils.Format;

public abstract class Command {
	
	protected final TitanChat plugin;
	
	private final String label;
	
	private String[] aliases = new String[0];
	private String description = "";
	
	private int maxArgs = 0;
	private int minArgs = 0;
	
	private String canonSyntax;
	private String syntax;
	
	private final Map<String, Command> commands;
	
	private boolean registration = true;
	
	private HelpIndex assistance;
	
	private Command parent;
	
	public Command(String label) {
		Validate.notEmpty(label.trim(), "Label cannot be empty");
		
		this.plugin = TitanChat.instance();
		this.label = label.trim();
		this.syntax = this.label;
		this.commands = new TreeMap<String, Command>();
	}
	
	private final String assembleCanonicalSyntax() {
		if (parent != null) {
			Command command = this;
			
			StringBuilder absolute = new StringBuilder().append("/");
			
			while (command != null && absolute.length() <= 1024) {
				String syntax = command.getSyntax();
				
				if (syntax.contains(" "))
					absolute.insert(1, syntax.substring(0, syntax.lastIndexOf(' ')).trim() + " ");
				else
					absolute.insert(1, syntax.trim() + " ");
				
				command = command.parent;
			}
			
			this.canonSyntax = absolute.toString().trim().toLowerCase();
			
		} else {
			this.canonSyntax = "/" + getSyntax();
		}
		
		return canonSyntax;
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof Command && toString().equals(object.toString());
	}
	
	protected void execute(CommandSender sender, String[] args) {
		if (!registration)
			return;
		
		Messaging.message(sender, Format.RED + "Invalid command");
		Messaging.message(sender, "Syntax: " + getCanonicalSyntax());
	}
	
	public Command get(String name) {
		return (has(name)) ? commands.get(name.toLowerCase()) : null;
	}
	
	public String[] getAliases() {
		return aliases.clone();
	}
	
	public List<Command> getAll() {
		return new ArrayList<Command>(commands.values());
	}
	
	public HelpIndex getAssitance() {
		return assistance;
	}
	
	public final String getCanonicalSyntax() {
		if (canonSyntax == null || canonSyntax.isEmpty())
			return assembleCanonicalSyntax();
		
		return canonSyntax;
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
		return name != null && !name.isEmpty() && commands.containsKey(name.toLowerCase());
	}
	
	public boolean has(Command command) {
		return command != null && has(command.getLabel()) && get(command.getLabel()).equals(command);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public final void invokeExecution(CommandSender sender, String[] args) {
		if (args.length > 0 && invokeExecution(sender, args[0], Arrays.copyOfRange(args, 1, args.length)))
			return;
		
		execute(sender, args);
	}
	
	private final boolean invokeExecution(CommandSender sender, String label, String[] args) {
		if (!registration || !has(label))
			return false;
		
		Command command = get(label);
		
		if (!command.isPermitted(sender, args)) {
			Messaging.message(sender, Format.RED + "You do not have permission");
			return true;
		}
		
		if (args.length < command.getMinArguments() || args.length > command.getMaxArguments()) {
			Messaging.message(sender, Format.RED + "Invalid argument length");
			Messaging.message(sender, "Syntax: " + getCanonicalSyntax());
			return true;
		}
		
		command.invokeExecution(sender, args);
		return true;
	}
	
	public final List<String> invokeTabCompletion(CommandSender sender, String[] args) {
		switch (args.length) {
		
		case 0:
			return tabComplete(sender, args);
		
		case 1:
			return (!commands.isEmpty()) ? match(args[0]) : tabComplete(sender, args);
			
		default:
			if (!has(args[0]))
				return tabComplete(sender, args);
			
			return get(args[0]).invokeTabCompletion(sender, Arrays.copyOfRange(args, 1, args.length));
		}
	}
	
	public boolean isPermitted(CommandSender sender, String[] args) {
		return true;
	}
	
	protected final boolean isRegistered() {
		if (!plugin.getSystem().hasManager(CommandManager.class))
			return false;
		
		return parent != null || plugin.getSystem().getManager(CommandManager.class).has(this);
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
	
	public void register(Command command) {
		if (!registration)
			return;
		
		Validate.notNull(command, "Command cannot be null");
		
		if (command.isRegistered())
			return;
		
		commands.put(command.getLabel().toLowerCase(), command);
		
		for (String alias : command.getAliases()) {
			if (has(alias))
				continue;
			
			commands.put(alias.toLowerCase(), command);
		}
		
		command.parent = this;
		command.assembleCanonicalSyntax();
	}
	
	protected void setAliases(String... aliases) {
		this.aliases = (aliases != null) ? aliases : new String[0];
	}
	
	protected void setArgumentRange(int minArgs, int maxArgs) {
		this.minArgs = (minArgs >= 0) ? minArgs : 0;
		this.maxArgs = (maxArgs >= minArgs) ? maxArgs : this.minArgs;
	}
	
	public void setAssistance(HelpIndex assistance) {
		this.assistance = (assistance != null) ? assistance : new Assistance(this);
	}
	
	protected void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
	
	protected void setSupportRegistration(boolean registration) {
		this.registration = registration;
	}
	
	protected void setSyntax(String syntax) {
		this.syntax = (syntax != null) ? label + " " + syntax : label;
		
		assembleCanonicalSyntax();
	}
	
	protected List<String> tabComplete(CommandSender sender, String[] args) {
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
	
	public void unregister(Command command) {
		if (!registration)
			return;
		
		Validate.notNull(command, "Command cannot be null");
		
		if (!has(command))
			return;
		
		commands.remove(command.getLabel().toLowerCase());
		
		for (String alias : command.getAliases()) {
			if (has(alias) && !get(alias).equals(command))
				continue;
			
			commands.remove(alias.toLowerCase());
		}
		
		command.parent = null;
		command.assembleCanonicalSyntax();
	}
}