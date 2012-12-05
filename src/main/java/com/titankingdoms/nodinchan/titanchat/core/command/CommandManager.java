package com.titankingdoms.nodinchan.titanchat.core.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.defaults.*;
import com.titankingdoms.nodinchan.titanchat.loading.Loader;
import com.titankingdoms.nodinchan.titanchat.util.C;
import com.titankingdoms.nodinchan.titanchat.util.Messaging;

public final class CommandManager {
	
	private final TitanChat plugin;
	
	private final Map<String, Command> commands;
	private final Map<String, Command> labels;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getCommandDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating command directory...");
		
		this.commands = new TreeMap<String, Command>();
		this.labels = new TreeMap<String, Command>();
	}
	
	public void dispatch(CommandSender sender, Channel channel, String label, String[] args) {
		if (existingCommandLabel(label)) {
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
	
	public boolean existingCommandLabel(String label) {
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
		
		if (commands.size() > 0) {
			StringBuilder str = new StringBuilder();
			
			for (Command command : getCommands()) {
				if (str.length() > 0)
					str.append(", ");
				
				str.append(command.getName());
			}
			
			plugin.log(Level.INFO, "Commands loaded: " + str.toString());
		}
	}
	
	public void register(Command... commands) {
		for (Command command : commands) {
			if (existingCommand(command))
				continue;
			
			this.commands.put(command.getName().toLowerCase(), command);
			this.labels.put(command.getName().toLowerCase(), command);
			
			for (String alias : command.getAliases())
				if (!existingCommandLabel(alias))
					this.labels.put(alias.toLowerCase(), command);
		}
	}
	
	public void unload() {
		
	}
}