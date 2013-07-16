package com.titankingdoms.dev.titanchat.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.core.EndPoint;

public final class ChatEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final EndPoint sender;
	private final EndPoint recipient;
	
	private final String format;
	private final String message;
	
	public ChatEvent(EndPoint sender, EndPoint recipient, String format, String message) {
		this.sender = sender;
		this.recipient = recipient;
		this.format = format;
		this.message = message;
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
	
	public EndPoint getRecipient() {
		return recipient;
	}
	
	public EndPoint getSender() {
		return sender;
	}
}