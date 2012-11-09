package com.titankingdoms.nodinchan.titanchat.core.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.loading.Loadable;

public abstract class Command extends Loadable {
	
	protected final TitanChat plugin;
	
	private String[] aliases;
	private String description;
	private String usage;
	private String permission;
	
	public Command(String name) {
		super(name);
		this.plugin = TitanChat.getInstance();
		this.aliases = new String[0];
		this.description = "";
		this.usage = "";
		this.permission = "";
	}
	
	public abstract Execution execute(CommandSender sender, Channel channel, String[] args);
	
	public String[] getAliases() {
		return aliases;
	}
	
	protected final ConsoleCommandSender getConsoleSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	public String getDescription() {
		return description;
	}
	
	protected final String getDisplayName(OfflinePlayer player) {
		return "";
	}
	
	protected final OfflinePlayer getOfflinePlayer(String name) {
		return plugin.getServer().getOfflinePlayer(name);
	}
	
	public String getPermission() {
		return permission;
	}
	
	protected final Player getPlayer(String name) {
		return plugin.getServer().getPlayer(name);
	}
	
	public String getUsage() {
		return usage;
	}
	
	protected final boolean hasPermission(CommandSender sender, String permission) {
		return (sender instanceof ConsoleCommandSender) ? true : sender.hasPermission(permission);
	}
	
	protected final boolean isOnline(String name) {
		return plugin.getServer().getOfflinePlayer(name).isOnline();
	}
	
	protected final void msg(CommandSender sender, String... messages) {
		sender.sendMessage(messages);
	}
	
	protected final void registerHelpTopic(HelpTopic topic) {
		
	}
	
	protected void setAliases(String... aliases) {
		this.aliases = aliases;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}
	
	protected void setPermission(String permission) {
		this.permission = permission;
	}
	
	protected void setUsage(String usage) {
		this.usage = usage;
	}
	
	public static final class Execution {
		
		private Execution() {}
		
		public static Execution end() {
			return new Execution();
		}
		
		public static Execution end(CommandSender sender, String... message) {
			return new Execution();
		}
		
		public Execution execution() {
			return this;
		}
	}
}