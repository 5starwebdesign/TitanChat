package com.titankingdoms.nodinchan.titanchat.core.channel.setting;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;

public abstract class Setting extends Command {
	
	public Setting(String name) {
		super(name);
	}
	
	public abstract void execute(CommandSender sender, String[] args);
	
	@Override
	public final void execute(CommandSender sender, Channel channel, String[] args) {
		execute(sender, args);
	}
	
	@Override
	public final boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}