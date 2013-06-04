package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.ConsoleParticipant;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link NickCommand} - Command for changing the display name of {@link Player}s
 * 
 * @author NodinChan
 *
 */
public final class NickCommand extends Command {
	
	public NickCommand() {
		super("Nick");
		setArgumentRange(1, 3);
		setDescription("Change the display name of the player");
		setUsage("<set|reset> [player] <nickname>");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (args[0].equalsIgnoreCase("set")) {
			if (args.length < 2) {
				sendMessage(sender, "&4Invalid argument length");
				sendMessage(sender, "&6" + "/titanchat nick " + getUsage());
				return;
			}
			
			Participant participant = plugin.getParticipantManager().getParticipant(sender);
			String nickname = "";
			
			if (args.length > 2) {
				participant = plugin.getParticipantManager().getParticipant(args[1]);
				nickname = args[2];
				
			} else {
				nickname = args[1];
			}
			
			if (nickname.isEmpty()) {
				sendMessage(sender, "&4Nicknames cannot be empty");
				return;
			}
			
			if (nickname.length() > 16 && !(participant instanceof ConsoleParticipant)) {
				sendMessage(sender, "&4Nicknames cannot be longer than 16 characters");
				return;
			}
			
			participant.setDisplayName(nickname);
			participant.notice("&6You are now known as " + participant.getDisplayName());
			
		} else if (args[0].equalsIgnoreCase("reset")) {
			if (args.length > 2) {
				sendMessage(sender, "&4Invalid argument length");
				sendMessage(sender, "&6" + "/titanchat nick " + getUsage());
				return;
			}
			
			Participant participant = plugin.getParticipantManager().getParticipant(sender);
			
			if (args.length > 1)
				participant = plugin.getParticipantManager().getParticipant(args[1]);
			
			participant.setDisplayName(participant.getName());
			participant.notice("&6You have reset your display name");
			
		} else {
			sendMessage(sender, "&4Incorrect usage: /titanchat nick " + getUsage());
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}