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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.nodinchan.dev.titanchat.TitanChat;
import com.nodinchan.dev.titanchat.api.command.guide.Assistance;
import com.nodinchan.dev.titanchat.api.command.guide.GenericAssistance;
import com.nodinchan.dev.titanchat.tools.Format;

public abstract class Command {
	
	protected final TitanChat plugin;
	
	private final String label;
	
	private String[] aliases;
	
	private String description;
	
	private int maxArgs;
	private int minArgs;
	
	private String canonSyntax;
	private String syntax;
	
	private boolean progressive;
	
	private final Map<String, Command> commands;
	
	private Assistance assistance;
	
	private Command parent;
	
	public Command(String label) {
		Validate.notEmpty(label, "Label cannot be empty");
		Validate.isTrue(label.matches(".*\\W+.*"), "Label cannot contain non-word characters");
		
		this.plugin = TitanChat.instance();
		this.label = label;
		this.aliases = new String[0];
		this.description = "";
		this.maxArgs = 0;
		this.minArgs = 0;
		this.syntax = label;
		this.progressive = true;
		this.commands = new TreeMap<>();
		this.assistance = new GenericAssistance(this);
	}
	
	private final String assembleCanonicalSyntax() {
		if (parent != null) {
			Command command = this;
			
			StringBuilder absolute = new StringBuilder().append('/');
			
			while (command != null) {
				String syntax = command.getSyntax();
				
				if (syntax.indexOf(' ', syntax.indexOf(' ')) > -1)
					absolute.insert(1, syntax.substring(0, syntax.lastIndexOf(' ')).trim() + " ");
				else
					absolute.insert(1, syntax.trim() + " ");
				
				command = command.getParent();
			}
			
			this.canonSyntax = absolute.toString().trim().toLowerCase();
			
		} else {
			this.canonSyntax = "/" + getSyntax().toLowerCase();
		}
		
		return canonSyntax;
	}
	
	protected final void callProgressiveExecution(CommandSender sender, String label, String[] args) {
		if (!isRegistered(label)) {
			message(sender, Format.RED + "Invalid command", "Syntax: " + getCanonicalSyntax());
			return;
		}
		
		Command command = getCommand(label);
		
		if (!command.isPermittedExecution(sender, args)) {
			message(sender, Format.RED + "You do not have permission");
			return;
		}
		
		if (args.length > 0 && args[0].equals("?") && !command.isRegistered(args[0])) {
			command.invokeAssistanceDisplay(sender, Arrays.copyOfRange(args, 1, args.length));
			return;
		}
		
		if (args.length < command.getMinArguments() || args.length > command.getMaxArguments()) {
			message(sender, Format.RED + "Invalid argument length", "Syntax: " + getCanonicalSyntax());
			return;
		}
		
		command.invokeExecution(sender, args);
	}
	
	protected final List<String> callProgressiveTabCompletion(CommandSender sender, String label, String[] args) {
		switch (args.length) {
			
		case 0:
			if (label == null || label.isEmpty())
				return ImmutableList.copyOf(commands.keySet());
			
			Builder<String> matches = ImmutableList.builder();
			
			for (String match : commands.keySet()) {
				if (!match.startsWith(label))
					continue;
				
				matches.add(match);
			}
			
			return matches.build();
			
		default:
			if (!isRegistered(label))
				return ImmutableList.of();
			
			return getCommand(label).invokeTabCompletion(sender, args);
		}
	}
	
	public String[] getAliases() {
		return aliases.clone();
	}
	
	public final Assistance getAssistance() {
		return assistance;
	}
	
	public final String getCanonicalSyntax() {
		if (canonSyntax == null || canonSyntax.isEmpty())
			return assembleCanonicalSyntax();
		
		return canonSyntax;
	}
	
	private final Command getCommand(String label) {
		return (label == null) ? null : commands.get(label.toLowerCase());
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
	
	private final Command getParent() {
		return parent;
	}
	
	public String getSyntax() {
		return syntax;
	}
	
	public boolean hasAliases() {
		return aliases.length > 0;
	}
	
	public boolean hasDescription() {
		return !description.isEmpty();
	}
	
	public final void invokeAssistanceDisplay(CommandSender sender, String[] args) {
		boolean list = args.length > 0 && args[0].equalsIgnoreCase("list");
		
		String title = assistance.getTitle();
		
		int max = (list) ? assistance.getListCount() : assistance.getPageCount();
		int page = 1;
		
		if (max > 1) {
			switch (args.length) {
			
			case 0:
				break;
				
			case 1:
				page = (!list) ? NumberUtils.toInt(args[0], 1) : 1;
				break;
				
			default:
				page = NumberUtils.toInt((!list) ? args[0] : args[1], 1);
				break;
			}
			
			title += " (" + page + "/" + max + ")";
		}
		
		if (page < 1)
			page = 1;
		
		if (page > max)
			page = max;
		
		String content = (list) ? assistance.getCommands(page) : assistance.getContent(page);
		
		message(sender, Format.AZURE + StringUtils.center(" " + title + " ", 50, '='));
		
		for (String line : Format.wrap(Format.AZURE + content, 50))
			message(sender, line);
	}
	
	public void invokeExecution(CommandSender sender, String[] args) {
		if (!progressive || args.length < 1)
			return;
		
		callProgressiveExecution(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
	}
	
	public List<String> invokeTabCompletion(CommandSender sender, String[] args) {
		if (!progressive || args.length < 1)
			return ImmutableList.of();
		
		return callProgressiveTabCompletion(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
	}
	
	public boolean isPermittedExecution(CommandSender sender, String[] args) {
		return true;
	}
	
	public final boolean isProgressive() {
		return progressive;
	}
	
	public final boolean isRegistered() {
		if (!plugin.getSystem().isLoaded(CommandManager.class))
			return false;
		
		return parent != null || plugin.getSystem().getModule(CommandManager.class).has(this);
	}
	
	public final boolean isRegistered(String label) {
		return progressive && label != null && commands.containsKey(label.toLowerCase());
	}
	
	protected void message(CommandSender sender, String... messages) {
		Validate.notNull(sender, "Sender cannot be null");
		sender.sendMessage(StringUtils.join(messages, '\n'));
	}
	
	public final Command registerCommand(Command command) {
		Validate.notNull(command, "Command cannot be null");
		Validate.isTrue(!command.isRegistered(), "Command already registered");
		
		commands.put(command.getLabel().toLowerCase(), command);
		
		assistance.addChapter(command.getAssistance());
		
		if (!command.hasAliases())
			return this;
		
		for (String alias : command.getAliases()) {
			if (alias.matches(".*\\W+.*"))
				continue;
			
			if (isRegistered(alias) && getCommand(alias).getLabel().equalsIgnoreCase(alias))
				continue;
			
			commands.put(alias.toLowerCase(), command);
		}
		
		return this;
	}
	
	public final Command registerCommands(Command... commands) {
		Validate.notNull(commands, "Commands cannot be null");
		
		for (Command command : commands)
			registerCommand(command);
		
		return this;
	}
	
	protected void setAliases(String... aliases) {
		this.aliases = (aliases != null) ? aliases : new String[0];
	}
	
	protected void setArgumentRange(int minArgs, int maxArgs) {
		this.minArgs = (minArgs >= 0) ? minArgs : 0;
		this.maxArgs = (maxArgs >= minArgs) ? maxArgs : this.minArgs;
	}
	
	public final void setAssistance(Assistance assistance) {
		this.assistance = (assistance != null) ? assistance : new GenericAssistance(this);
	}
	
	protected void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
	
	protected final void setProgressive(boolean progressive) {
		this.progressive = progressive;
	}
	
	protected void setSyntax(String syntax) {
		this.syntax = (syntax != null) ? label + " " + syntax : label;
		
		assembleCanonicalSyntax();
	}
	
	public final Command unregisterCommand(String label) {
		Validate.notEmpty(label, "Label cannot be empty");
		Validate.isTrue(isRegistered(label), "Command not registered");
		
		Command command = commands.remove(label.toLowerCase());
		
		assistance.removeChapter(label);
		
		if (!command.hasAliases())
			return this;
		
		for (String alias : command.getAliases()) {
			if (alias.matches(".*\\W+.*"))
				continue;
			
			if (!isRegistered(alias) || !getCommand(alias).getLabel().equalsIgnoreCase(alias))
				continue;
			
			commands.remove(alias.toLowerCase());
		}
		
		return this;
	}
	
	public final Command unregisterCommands(String... labels) {
		Validate.notNull(labels, "Labels cannot be null");
		
		for (String label : labels)
			unregisterCommand(label);
		
		return this;
	}
}