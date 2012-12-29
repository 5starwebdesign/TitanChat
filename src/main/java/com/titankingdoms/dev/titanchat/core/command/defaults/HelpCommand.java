package com.titankingdoms.dev.titanchat.core.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.command.Command;

public final class HelpCommand extends Command {
	
	public HelpCommand() {
		super("Help");
		setAliases("?");
		setArgumentRange(0, 1);
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return false;
	}
}