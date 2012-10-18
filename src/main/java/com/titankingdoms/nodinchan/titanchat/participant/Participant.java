package com.titankingdoms.nodinchan.titanchat.participant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Type;

public abstract class Participant {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private Channel current;
	private final Map<String, Channel> channels;
	
	public Participant(String name) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.channels = new HashMap<String, Channel>();
	}
	
	public abstract void chat(Channel channel, String message);
	
	public final void chat(String message) {
		chat(current, message);
	}
	
	public final void direct(Channel channel) {
		this.current = channel;
		
		if (!isParticipating(channel) && !channel.getType().equals(Type.UTILITY))
			join(channel);
	}
	
	public final Set<Channel> getChannels() {
		return new HashSet<Channel>(channels.values());
	}
	
	public final Channel getCurrentChannel() {
		return current;
	}
	
	public final String getName() {
		return name;
	}
	
	public abstract boolean hasPermission(String permission);
	
	public final boolean isDirectedAt(String channel) {
		return (current != null) ? current.getName().equalsIgnoreCase(channel) : false;
	}
	
	public final boolean isDirectedAt(Channel channel) {
		return (channel != null) ? isDirectedAt(channel.getName()) : current == null;
	}
	
	public abstract boolean isOnline();
	
	public final boolean isParticipating(String channel) {
		return channels.containsKey(channel.toLowerCase());
	}
	
	public final boolean isParticipating(Channel channel) {
		return (channel != null) ? isParticipating(channel.getName()) : false;
	}
	
	public final void join(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.put(channel.getName().toLowerCase(), channel);
		
		if (!this.current.equals(channel))
			direct(channel);
		
		if (!channel.isParticipating(this))
			channel.join(this);
	}
	
	public final void leave(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.remove(channel.getName().toLowerCase());
		
		if (this.current.equals(channel))
			direct(getChannels().iterator().hasNext() ? getChannels().iterator().next() : null);
		
		if (channel.isParticipating(this))
			channel.leave(this);
	}
	
	public abstract void send(MessageLevel level, String message);
}