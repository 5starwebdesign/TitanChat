/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.titankingdoms.dev.titanchat.core.command;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.command.defaults.*;
import com.titankingdoms.dev.titanchat.loading.Loader;
import com.titankingdoms.dev.titanchat.util.C;
import com.titankingdoms.dev.titanchat.util.Messaging;

public final class CommandManager {
	
	private final TitanChat plugin;
	
	private final Map<String, Command> commands;
	private final Map<String, Command> labels;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getCommandDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating command directory...");
		
		this.commands = new TreeMap<String, Command>();
		this.labels = new HashMap<String, Command>();
	}
	
	public void dispatch(CommandSender sender, Channel channel, String label, String[] args) {
		if (existingLabel(label)) {
			Command command = getCommand(label);
			
			if (args.length < command.getMinArguments() || args.length > command.getMaxArguments()) {
				Messaging.sendMessage(sender, C.RED + "Invalid Argument Length");
				Messaging.sendMessage(sender, C.GOLD + command.getUsage());
				return;
			}
			
			if (!command.permissionCheck(sender, channel)) {
				Messaging.sendMessage(sender, C.RED + "You do not have permission");
				return;
			}
			
			command.execute(sender, channel, args);
			return;
		}
		
		Messaging.sendMessage(sender, C.RED + "Invalid Command");
		Messaging.sendMessage(sender, C.GOLD + "\"/titanchat help [page]\" for help");
	}
	
	public boolean existingCommand(String name) {
		return commands.containsKey(name.toLowerCase());
	}
	
	public boolean existingCommand(Command command) {
		return existingCommand(command.getName());
	}
	
	public boolean existingLabel(String label) {
		return labels.containsKey(label.toLowerCase());
	}
	
	public Command getCommand(String label) {
		return labels.get(label.toLowerCase());
	}
	
	public File getCommandDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "commands");
	}
	
	public List<Command> getCommands() {
		return new ArrayList<Command>(commands.values());
	}
	
	public void load() {
		register(
				new BanCommand(),
				new CreateCommand(),
				new DeleteCommand(),
				new JoinCommand(),
				new KickCommand(),
				new LeaveCommand(),
				new PlaceCommand(),
				new UnbanCommand()
		);
		
		for (Command command : Loader.load(Command.class, getCommandDirectory()))
			register(command);
		
		if (!commands.isEmpty())
			plugin.log(Level.INFO, "Commands loaded: " + StringUtils.join(commands.keySet(), ", "));
	}
	
	public void register(Command... commands) {
		for (Command command : commands) {
			if (existingCommand(command))
				continue;
			
			this.commands.put(command.getName().toLowerCase(), command);
			this.labels.put(command.getName().toLowerCase(), command);
			
			for (String alias : command.getAliases())
				if (!existingLabel(alias))
					this.labels.put(alias.toLowerCase(), command);
		}
	}
	
	public void unload() {
		
	}
}