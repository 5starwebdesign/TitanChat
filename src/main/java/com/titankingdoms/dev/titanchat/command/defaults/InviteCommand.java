package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public final class InviteCommand extends Command {
	
	public InviteCommand() {
		super("Invite");
		setAliases("inv");
		setArgumentRange(1, 1);
		setUsage("[player]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
	
	public final class Invitation {
		
		private final Channel channel;
		private final Participant participant;
		
		public Invitation(Channel channel, Participant participant) {
			this.channel = channel;
			this.participant = participant;
		}
		
		public Channel getChannel() {
			return channel;
		}
		
		public Participant getParticipant() {
			return participant;
		}
	}
}