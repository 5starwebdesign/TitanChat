package com.titankingdoms.nodinchan.titanchat.core.command.execution;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;

public abstract class Executor implements Comparable<Executor> {
	
	private final String name;
	
	public Executor(String name) {
		this.name = name;
	}
	
	public final int compareTo(Executor executor) {
		return getName().compareTo(executor.getName());
	}
	
	public abstract boolean execute(CommandSender sender, Channel channel, String[] args);
	
	public abstract String[] getAliases();
	
	public abstract String getDescription();
	
	public String getName() {
		return name;
	}
	
	public abstract String getUsage();
}