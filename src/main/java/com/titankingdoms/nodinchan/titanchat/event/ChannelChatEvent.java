package com.titankingdoms.nodinchan.titanchat.event;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;
import com.titankingdoms.nodinchan.titanchat.format.FormatHandler;

public final class ChannelChatEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Participant sender;
	private final Set<Participant> recipients;
	
	private final Channel channel;
	
	private String format;
	private String message;
	
	public ChannelChatEvent(Participant sender, Channel channel, String message) {
		this(sender, new HashSet<Participant>(), channel, FormatHandler.DEFAULT_FORMAT, message);
	}
	
	public ChannelChatEvent(Participant sender, Channel channel, String format, String message) {
		this(sender, new HashSet<Participant>(), channel, format, message);
	}
	
	public ChannelChatEvent(Participant sender, Set<Participant> recipients, Channel channel, String message) {
		this(sender, recipients, channel, FormatHandler.DEFAULT_FORMAT, message);
	}
	
	public ChannelChatEvent(Participant sender, Set<Participant> recipients, Channel channel, String format, String message) {
		this.sender = sender;
		this.recipients = recipients;
		this.channel = channel;
		this.format = format;
		this.message = message;
	}
	
	public boolean addRecipient(Participant recipient) {
		if (recipient == null)
			return false;
		
		return (!this.recipients.contains(recipient)) ? this.recipients.add(recipient) : false;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public String getFormat() {
		return format;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Set<Participant> getRecipients() {
		return new HashSet<Participant>(recipients);
	}
	
	public Participant getSender() {
		return sender;
	}
	
	public boolean removeRecipient(Participant recipient) {
		if (recipient == null)
			return false;
		
		return (this.recipients.contains(recipient)) ? this.recipients.remove(recipient) : false;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}