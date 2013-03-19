package com.titankingdoms.dev.titanchat.event;

import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public final class ChannelJoinEvent extends ChannelEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Participant participant;
	
	public ChannelJoinEvent(Participant participant, Channel channel) {
		super(channel);
		this.participant = participant;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Participant getParticipant() {
		return participant;
	}
}