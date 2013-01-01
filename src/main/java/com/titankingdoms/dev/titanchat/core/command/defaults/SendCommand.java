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
		setBriefDescription("Sends the message to the channel");
		setFullDescription(
				"Description: Sends the message to the specified channel as if chatting in it\n" +
				"Aliases: 'send', 's'\n" +
				"Usage: /titanchat <@[channel]> send [message]");
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