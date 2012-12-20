package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;

public final class PromoteCommand extends Command {
	
	public PromoteCommand(String name) {
		super("Promote");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.isAdmin(sender.getName()))
			return true;
		
		return hasPermission(sender, "TitanChat.rank." + channel.getName());
	}
}