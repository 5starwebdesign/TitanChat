package com.titankingdoms.nodinchan.titanchat.channel.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;

public abstract class SettingChanger {
	
	protected final TitanChat plugin;
	
	private final String setting;
	
	private String usage;
	
	public SettingChanger(String setting) {
		this.plugin = TitanChat.getInstance();
		this.setting = setting;
		this.usage = "";
	}
	
	public String getSetting() {
		return setting;
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
	
	public abstract void onSet(CommandSender sender, String[] args);
	
	protected void setUsage(String usage) {
		this.usage = usage;
	}
	
	public final void usage(CommandSender sender) {
		if (usage.isEmpty())
			return;
		
		plugin.send(MessageLevel.INFO, sender, "Usage: /titanchat <@><channel> " + getUsage());
	}
}