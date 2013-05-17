package com.titankingdoms.dev.titanchat.core.channel.irc;

import com.titankingdoms.dev.titanchat.core.participant.Participant;

public abstract class IRCParticipant extends Participant {
	
	protected final IRCChannel channel;
	
	public IRCParticipant(String name, IRCChannel channel) {
		super("IRC:" + name);
		this.channel = channel;
	}
	
	@Override
	public abstract void sendMessage(String... messages);
}