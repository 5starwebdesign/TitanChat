package com.titankingdoms.dev.titanchat.core;

import java.util.Set;

public final class Message {
	
	private final EndPoint sender;
	private final Set<EndPoint> recipients;
	
	private final String format;
	private final String message;
	
	public Message(EndPoint sender, Set<EndPoint> recipients, String format, String message) {
		this.sender = sender;
		this.recipients = recipients;
		this.format = format;
		this.message = message;
	}
	
	public String getFormat() {
		return format;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Set<EndPoint> getRecipients() {
		return recipients;
	}
	
	public EndPoint getSender() {
		return sender;
	}
}