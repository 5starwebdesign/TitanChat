package com.titankingdoms.dev.titanchat.next.update;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.titankingdoms.dev.titanchat.core.channel.Channel;

public abstract class Participant extends ChatEntity {
	
	private Channel current;
	
	private final Map<String, Channel> channels;
	private final Map<String, ChatGroup> group;
	
	public Participant(String name) {
		super(name);
		this.channels = new HashMap<String, Channel>();
		this.group = new HashMap<String, ChatGroup>();
	}
	
	public final void direct(Channel channel) {
		this.current = channel;
		
		if (!isParticipating(channel))
			join(channel);
		
		recalculatePermissions();
	}
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof Participant) ? ((Participant) object).getName().equals(getName()) : false;
	}
	
	public Set<Channel> getChannels() {
		return new HashSet<Channel>(channels.values());
	}
	
	public ChatGroup getChatGroup(Channel channel) {
		return group.get(channel.getName());
	}
	
	public Channel getCurrentChannel() {
		return current;
	}
	
	@Override
	public String getEntityType() {
		return "Participant";
	}
	
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
		
		if (this.current == null || !this.current.equals(channel))
			direct(channel);
	}
	
	public final void leave(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.remove(channel.getName().toLowerCase());
		
		if (this.current != null && this.current.equals(channel))
			direct(getChannels().iterator().hasNext() ? getChannels().iterator().next() : null);
	}
	
	public void setChatGroup(Channel channel, ChatGroup group) {
		this.group.put(channel.getName(), group);
	}
}