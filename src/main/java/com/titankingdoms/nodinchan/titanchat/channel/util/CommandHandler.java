package com.titankingdoms.nodinchan.titanchat.channel.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.command.Executor;

public abstract class CommandHandler {
	
	protected final TitanChat plugin;
	
	private final String command;
	
	private String usage;
	
	public CommandHandler(String command) {
		this.plugin = TitanChat.getInstance();
		this.command = command;
		
		Executor executor = plugin.getManager().getCommandManager().getCommandExecutor(command);
		
		if (executor != null)
			this.usage = executor.getUsage();
		else
			this.usage = "";
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public final boolean hasPermission(CommandSender sender, String permission) {
		if (!(sender instanceof Player))
			return true;
		
		return sender.hasPermission(permission);
	}
	
	public final void invalidArgLength(CommandSender sender) {
		plugin.send(MessageLevel.WARNING, sender, "Invalid Argument Length");
		usage(sender);
	}
	
	public abstract void onCommand(CommandSender sender, String[] args);
	
	protected void setUsage(String usage) {
		this.usage = usage;
	}
	
	public final void usage(CommandSender sender) {
		if (usage.isEmpty())
			return;
		
		plugin.send(MessageLevel.INFO, sender, "Usage: /titanchat <@><channel> " + getUsage());
	}
}