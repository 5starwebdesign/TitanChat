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

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.command.*;
import com.titankingdoms.dev.titanchat.tools.Messaging;

public final class CommandManager extends Command {
	
	private final TitanChat plugin;
	
	public CommandManager() {
		super("TitanChat");
		this.plugin = TitanChat.getInstance();
	}
	
	@Override
	public void execute(CommandSender sender, String[] args, CommandData data) {
		if (args.length < 1) {
			Messaging.sendNotice("&6You are running v" + plugin.getDescription().getVersion());
			Messaging.sendNotice("&6Type \"/titanchat help\" for the non-existant help");
			return;
		}
		
		executeLayer(sender, args, data);
	}
	
	public Command getCommand(String name) {
		return get(name);
	}
	
	public Collection<Command> getCommands() {
		return getAll();
	}
	
	@Override
	public String getName() {
		return "CommandManager";
	}
	
	public boolean hasCommand(String name) {
		return has(name);
	}
	
	public boolean hasCommand(Command command) {
		return has(command);
	}
	
	@Override
	public void load() {
		registerAll(new ReloadCommand(),
					new ClearCommand(),
					new BlacklistCommand(), new WhitelistCommand()
		);
	}
	
	@Override
	public void registerAll(Command... commands) {
		if (commands == null)
			return;
		
		for (Command command : commands) {
			if (command == null)
				continue;
			
			String name = command.getLabel();
			
			if (has(name) && get(name).getLabel().equals(name)) {
				plugin.log(Level.INFO, "Duplicate: " + command);
				continue;
			}
			
			super.registerAll(command);
		}
	}
	
	@Override
	public void reload() {
		unload();
		load();
	}
	
	@Override
	public List<String> tab(CommandSender sender, String[] args, CommandData data) {
		return tabLayer(sender, args, data);
	}
	
	@Override
	public void unload() {
		for (Command command : getAll())
			unregister(command);
	}
}