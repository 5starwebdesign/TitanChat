package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link DirectCommand} - Command for directing attention to another channel
 * 
 * @author NodinChan
 *
 */
public final class DirectCommand extends Command {
	
	public DirectCommand() {
		super("Direct");
		setArgumentRange(1, 1);
		setUsage("[channel]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().hasAlias(args[0])) {
			sendMessage(sender, "&4Channel does not exist");
			return;
		}
		
		channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (!channel.isParticipating(sender.getName())) {
			plugin.getServer().dispatchCommand(sender, "titanchat join " + channel.getName());
			return;
		}
		
		Participant participant = plugin.getParticipantManager().getParticipant(sender);
		
		if (participant.getCurrent().equals(channel)) {
			sendMessage(sender, "&4You are already speaking in the channel");
			return;
		}
		
		participant.direct(channel);
		sendMessage(sender, "&6You are now speaking in " + channel.getName());
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}