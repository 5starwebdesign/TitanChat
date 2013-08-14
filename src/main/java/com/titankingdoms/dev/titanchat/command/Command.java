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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;

public abstract class Command {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private String[] aliases;
	private String description;
	private int maxArgs;
	private int minArgs;
	
	public Command(String name, String[] aliases, String description) {
		this.plugin = TitanChat.getInstance();
		this.name = (name != null) ? name : "";
		this.aliases = (aliases != null) ? aliases : new String[0];
		this.description = (description != null) ? description : "";
		this.maxArgs = 0;
		this.minArgs = 0;
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
}