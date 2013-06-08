package com.titankingdoms.dev.titanchat.command;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public abstract class ChannelCommand extends Command {
	
	public ChannelCommand(String name) {
		super(name);
	}
	
	@Override
	public final void execute(CommandSender sender, String[] args) {
		String chName = "";
		
		if (args[0].startsWith("@")) {
			chName = args[0].substring(1);
			args = Arrays.copyOfRange(args, 1, args.length);
		}
		
		Channel channel = null;
		
		if (!chName.isEmpty()) {
			if (!plugin.getChannelManager().hasChannel(chName)) {
				sendMessage(sender, "&4Channel does not exist");
				return;
			}
			
			channel = plugin.getChannelManager().getChannel(chName);
			
		} else {
			Participant participant = plugin.getParticipantManager().getParticipant(sender);
			
			if (!(participant.getCurrentEndPoint() instanceof Channel)) {
				sendMessage(sender, "&4Channel not defined");
				return;
			}
			
			channel = (Channel) participant.getCurrentEndPoint();
		}
		
		if (!permissionCheck(sender, channel)) {
			sendMessage(sender, "&4You do not have permission");
			return;
		}
		
		execute(sender, channel, args);
	}
	
	public abstract void execute(CommandSender sender, Channel channel, String[] args);
	
	public String getUsage() {
		return "[@<channel>] " + super.getUsage();
	}
	
	public final boolean permissionCheck(CommandSender sender) {
		return true;
	}
	
	public abstract boolean permissionCheck(CommandSender sender, Channel channel);
}