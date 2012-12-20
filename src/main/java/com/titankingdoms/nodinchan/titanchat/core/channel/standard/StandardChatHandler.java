package com.titankingdoms.nodinchan.titanchat.core.channel.standard;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChatHandler;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;

public final class StandardChatHandler extends ChatHandler {
	
	private final Channel channel;
	
	public StandardChatHandler(StandardChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public String getFormat() {
		return channel.getFormat();
	}
	
	@Override
	public Set<Participant> getRecipients() {
		Set<Participant> recipients = new HashSet<Participant>();
		
		for (String name : channel.getParticipants())
			recipients.add(plugin.getParticipantManager().getParticipant(name));
		
		return recipients;
	}
}