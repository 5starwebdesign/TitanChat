package com.titankingdoms.nodinchan.titanchat.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.command.info.*;

public final class Executor {
	
	private final CommandBase command;
	
	private final Method method;
	
	private final String name;
	private String[] aliases = new String[0];
	private String description = "";
	private String usage = "";
	
	private boolean channel = false;
	private boolean console = true;
	
	public Executor(CommandBase command, Method method) {
		this.method = method;
		this.command = command;
		this.name = method.getName();
		
		if (method.isAnnotationPresent(CommandOption.class)) {
			channel = method.getAnnotation(CommandOption.class).requireChannel();
			console = method.getAnnotation(CommandOption.class).allowConsoleUsage();
		}
		
		if (method.isAnnotationPresent(Aliases.class))
			this.aliases = method.getAnnotation(Aliases.class).value();
		
		if (method.isAnnotationPresent(Description.class))
			this.description = method.getAnnotation(Description.class).value();
		
		if (method.isAnnotationPresent(Usage.class))
			this.usage = method.getAnnotation(Usage.class).value();
	}
	
	public boolean allowConsoleUsage() {
		return console;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Executor)
			return ((Executor) object).method.equals(method) && ((Executor) object).command.equals(command);
		
		return false;
	}
	
	public void execute(CommandSender sender, com.titankingdoms.nodinchan.titanchat.channel.Channel channel, String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		method.invoke(command, sender, channel, args);
	}
	
	public void execute(Player player, com.titankingdoms.nodinchan.titanchat.channel.Channel channel, String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		method.invoke(command, player, channel, args);
	}
	
	public String[] getAliases() {
		return aliases;
	}
	
	public CommandBase getCommand() {
		return command;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public boolean requireChannel() {
		return channel;
	}
	
	@Override
	public String toString() {
		return "Command:" + name;
	}
}