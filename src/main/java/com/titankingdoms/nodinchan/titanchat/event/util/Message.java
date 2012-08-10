package com.titankingdoms.nodinchan.titanchat.event.util;

public final class Message implements Cloneable {
	
	private String format;
	private String message;
	
	public Message(String format, String message) {
		this.format = format;
		this.message = message;
	}
	
	@Override
	public Message clone() {
		return new Message(format, message);
	}
	
	public String getFormat() {
		return format;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}