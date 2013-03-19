package com.titankingdoms.dev.titanchat.command;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.command.defaults.*;

public final class CommandManager {
	
	private final TitanChat plugin;
	
	private final Map<String, Command> commands;
	private final Map<String, Command> labels;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getCommandDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating commands directory...");
		
		this.commands = new HashMap<String, Command>();
		this.labels = new HashMap<String, Command>();
	}
	
	public Command getCommand(String label) {
		return labels.get(label.toLowerCase());
	}
	
	public File getCommandDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "commands");
	}
	
	public boolean hasCommand(String name) {
		return commands.containsKey(name.toLowerCase());
	}
	
	public boolean hasCommand(Command command) {
		return hasCommand(command.getName());
	}
	
	public boolean hasLabel(String label) {
		return labels.containsKey(label.toLowerCase());
	}
	
	public void load() {
		registerCommands(
				new InviteCommand(),
				new JoinCommand(),
				new LeaveCommand()
		);
	}
	
	public void registerCommands(Command... commands) {
		if (commands == null)
			return;
		
		for (Command command : commands) {
			if (command == null)
				continue;
			
			if (hasCommand(command)) {
				plugin.log(Level.WARNING, "Duplicate command: " + command.getName());
				continue;
			}
			
			this.commands.put(command.getName().toLowerCase(), command);
			this.labels.put(command.getName().toLowerCase(), command);
			
			for (String alias : command.getAliases())
				if (!hasLabel(alias))
					labels.put(alias.toLowerCase(), command);
		}
	}
	
	public void reload() {
		this.commands.clear();
		
		registerCommands(
				new InviteCommand(),
				new JoinCommand(),
				new LeaveCommand()
		);
	}
	
	public void unload() {
		this.commands.clear();
	}
}