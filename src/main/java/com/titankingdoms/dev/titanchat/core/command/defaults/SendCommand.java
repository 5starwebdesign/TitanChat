package com.titankingdoms.dev.titanchat.core.command.defaults;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.command.Command;

public final class SendCommand extends Command {
	
	public SendCommand() {
		super("Send");
		setAliases("s");
		setArgumentRange(1, 1024);
		setDescription("Chats in the specified channel");
		setUsage("[message]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		plugin.getParticipantManager().getParticipant(sender).chat(channel, StringUtils.join(args, " "));
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.isBlacklisted(sender.getName()))
			return false;
		
		return sender.hasPermission("TitanChat.speak." + channel.getName());
	}
}