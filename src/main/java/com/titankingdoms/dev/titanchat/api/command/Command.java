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
import com.titankingdoms.dev.titanchat.api.help.HelpIndex;
import com.titankingdoms.dev.titanchat.utility.FormatUtils.Format;
import com.titankingdoms.dev.titanchat.utility.Messaging;

public abstract class Command {
	
	protected final TitanChat plugin;
	
	private final String label;
	
	private String[] aliases = new String[0];
	private String description = "";
	
	private int maxArgs = 0;
	private int minArgs = 0;
	
	private String syntax;
	private String canonicalSyntax;
	
	private boolean registration = true;
	
	private final Map<String, Command> commands;
	
	private HelpIndex help;
	
	private boolean registered = false;
	private Command parent = null;
	
	public Command(String label) {
		Validate.notEmpty(label.trim(), "Label cannot be empty");
		Validate.isTrue(StringUtils.isAlphanumeric(label), "Label cannot be non-alphanumerical");
		
		this.plugin = TitanChat.getInstance();
		this.label = label.trim();
		this.syntax = this.label;
		this.commands = new TreeMap<String, Command>();
		this.help = new CommandSection(this);
	}
	
	private final String assembleCanonicalSyntax() {
		if (parent != null) {
			Command cmd = this;
			
			StringBuilder absolute = new StringBuilder().append("/");
			
			while (cmd != null && absolute.length() <= 1024) {
				String syntax = cmd.getSyntax();
				
				if (syntax.contains(" "))
					absolute.insert(1, syntax.substring(0, syntax.lastIndexOf(' ')).trim() + " ");
				else
					absolute.insert(1, syntax.trim() + " ");
				
				cmd = cmd.parent;
			}
			
			this.canonicalSyntax = absolute.toString().trim().toLowerCase();
			
		} else { this.canonicalSyntax = "/" + getSyntax(); }
		
		return canonicalSyntax;
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof Command && toString().equals(object.toString());
	}
	
	public void execute(CommandSender sender, String[] args) {
		if (!registration)
			return;
		
		Messaging.message(sender, Format.RED + "Invalid command");
		Messaging.message(sender, "Syntax: " + getCanonicalSyntax());
	}
	
	public Command get(String name) {
		return (has(name)) ? commands.get(name) : null;
	}
	
	public String[] getAliases() {
		return aliases.clone();
	}
	
	public List<Command> getAll() {
		return new ArrayList<Command>(commands.values());
	}
	
	public final String getCanonicalSyntax() {
		if (canonicalSyntax == null || canonicalSyntax.isEmpty())
			assembleCanonicalSyntax();
		
		return canonicalSyntax;
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
		if (!registration || args.length < 1 || !has(args[0])) {
			if (args.length > 0 && args[0].equalsIgnoreCase("?")) {
				int max = help.getPageCount();
				
				String title = "";
				String content = "";
				
				if (max > 1) {
					int page = 1;
					
					if (args.length > 1)
						try { page = Integer.parseInt(args[1]); } catch (Exception e) {}
					
					if (page < 1)
						page = 1;
					
					if (page > max)
						page = max;
					
					title = help.getTitle() + " (" + page + "/" + max + ")";
					content = help.getContent(page);
					
				} else {
					title = help.getTitle();
					content = help.getContent();
				}
				
				sender.sendMessage(StringUtils.center(" " + title + " ", 55, '='));
				sender.sendMessage(content);
				sender.sendMessage("=======================================================");
				return;
			}
			
			execute(sender, args);
			return;
		}
		
		Command next = get(args[0]);
		String[] arguments = Arrays.copyOfRange(args, 1, args.length);
		
		if (!next.validateAuthorisation(sender, arguments)) {
			Messaging.message(sender, Format.RED + "You do not have permission");
			return;
		}
		
		if (arguments.length < next.getMinArguments() || arguments.length > next.getMaxArguments()) {
			Messaging.message(sender, Format.RED + "Invalid argument length");
			Messaging.message(sender, "Syntax: " + getCanonicalSyntax());
			return;
		}
		
		next.invokeExecution(sender, arguments);
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
	
	protected boolean isRegistered() {
		return registered;
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
		if (!registration)
			return;
		
		Validate.notNull(command, "Command cannot be null");
		
		if (command.isRegistered() || has(command))
			return;
		
		commands.put(command.getLabel().toLowerCase(), command);
		
		for (String alias : command.getAliases()) {
			if (has(alias))
				continue;
			
			commands.put(alias.toLowerCase(), command);
		}
		
		command.registered = true;
		command.parent = this;
		command.assembleCanonicalSyntax();
		help.addSection(command.help);
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
	
	protected void setHelpSection(HelpIndex help) {
		if (parent != null)
			parent.help.removeSection(this.help);
		
		this.help = (help != null) ? help : new CommandSection(this);
		
		if (parent != null)
			parent.help.addSection(this.help);
		
		for (Command command : getAll())
			this.help.addSection(command.help);
	}
	
	protected void setRegistrationSupport(boolean support) {
		this.registration = support;
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
	
	protected void unregister(Command command) {
		if (!registration)
			return;
		
		Validate.notNull(command, "Command cannot be null");
		
		if (!command.isRegistered() || !has(command))
			return;
		
		commands.remove(command.getLabel().toLowerCase());
		
		for (String alias : command.getAliases()) {
			if (has(alias) && !get(alias).equals(command))
				continue;
			
			commands.remove(alias.toLowerCase());
		}
		
		command.registered = false;
		command.parent = null;
		command.assembleCanonicalSyntax();
		help.removeSection(command.help);
	}
	
	public boolean validateAuthorisation(CommandSender sender, String[] args) {
		return true;
	}
}