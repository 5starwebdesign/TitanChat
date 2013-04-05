package com.titankingdoms.dev.titanchat.event;

import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.core.channel.Channel;

public final class ChannelCreationEvent extends ChannelEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	public ChannelCreationEvent(Channel channel) {
		super(channel);
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}