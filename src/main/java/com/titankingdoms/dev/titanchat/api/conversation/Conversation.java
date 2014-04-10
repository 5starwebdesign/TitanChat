/*
 *     Copyright (C) 2013  Nodin Chan
 *     
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.api.conversation;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.api.conversation.Node;

public final class Conversation implements Cloneable {
	
	private final Node sender;
	private final Node recipient;
	
	private String format;
	private String message;
	
	private final String type;
	
	private Status status;
	
	private Conversation(Node sender, Node recipient, String format, String message, String type, Status status) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(recipient, "Recipient cannot be null");
		Validate.notEmpty(format, "Format cannot be empty");
		Validate.notEmpty(type, "Type cannot be empty");
		Validate.notNull(status, "Status cannot be null");
		
		this.sender = sender;
		this.recipient = recipient;
		this.format = format;
		this.message = (message != null) ? message : "";
		this.type = type;
		this.status = status;
	}
	
	public Conversation(Node sender, Node recipient, String format, String message, String type) {
		this(sender, recipient, format, message, type, Status.PENDING);
	}
	
	@Override
	public Conversation clone() {
		return new Conversation(sender, recipient, format, message, type, status);
	}
	
	public String getFormat() {
		return format;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Node getRecipient() {
		return recipient;
	}
	
	public Node getSender() {
		return sender;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean inStatus(Status status) {
		return this.status.equals(status);
	}
	
	public Conversation setFormat(String format) {
		Validate.isTrue(format != null && format.contains("%message"), "Format cannot forgo %message");
		this.format = format;
		return this;
	}
	
	public Conversation setMessage(String message) {
		this.message = (message != null) ? message : "";
		return this;
	}
	
	public Conversation setStatus(Status status) {
		Validate.notNull(status, "Status cannot be null");
		Validate.isTrue(!status.equals(Status.PENDING), "Status cannot be set to Pending");
		this.status = status;
		return this;
	}
	
	public enum Status { CANCELLED, PENDING, SENT }
}