package com.titankingdoms.dev.titanchat.event;

import org.bukkit.event.Event;

import com.titankingdoms.dev.titanchat.core.channel.Channel;

public abstract class ChannelEvent extends Event {
	
	protected final Channel channel;
	
	public ChannelEvent(Channel channel) {
		this.channel = channel;
	}
	
	public final Channel getChannel() {
		return channel;
	}
}