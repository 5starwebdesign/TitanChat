/*
 *     Copyright (C) 2014  Nodin Chan
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

public final class Conversation {
	
	private final Node sender;
	private final Node recipient;
	
	private final String type;
	
	private String format;
	private String message;
	
	public Conversation(Node sender, Node recipient, String type) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(recipient, "Recipient cannot be null");
		Validate.notEmpty(type, "Type cannot be empty");
		
		this.sender = sender;
		this.recipient = recipient;
		this.type = type;
		this.format = "%message";
		this.message = "";
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
	
	public String getType() {
		return type;
	}
	
	public Conversation setFormat(String format) {
		Validate.isTrue(format != null && format.contains("%message"), "");
		
		this.format = format;
		return this;
	}
	
	public Conversation setMessage(String message) {
		this.message = (message != null) ? message : "";
		return this;
	}
}