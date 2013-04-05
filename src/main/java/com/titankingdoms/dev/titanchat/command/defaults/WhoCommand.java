package com.titankingdoms.dev.titanchat.command.defaults;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link WhoCommand} - Command for getting info about a player
 * 
 * @author NodinChan
 *
 */
public final class WhoCommand extends Command {
	
	public WhoCommand() {
		super("Who");
		setArgumentRange(0, 1);
		setUsage("<player>");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		Participant participant = plugin.getParticipantManager().getParticipant(sender);
		
		if (args.length > 0)
			participant = plugin.getParticipantManager().getParticipant(args[0]);
		
		sendMessage(sender, "&6Name: &5" + participant.getName());
		sendMessage(sender, "&6Status: &5" + ((participant.isOnline()) ? "Online" : "Offline"));
		sendMessage(sender, "&6Channels: &5" + StringUtils.join(participant.getChannelList(), ", "));
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}