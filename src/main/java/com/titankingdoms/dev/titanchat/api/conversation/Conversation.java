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

import java.util.Set;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent.Status;

public final class Conversation {
	
	private final TitanChat plugin;
	
	private final Node sender;
	private final Node recipient;
	
	private final String type;
	
	private String format;
	private String message;
	
	public Conversation(Node sender, Node recipient, String type) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(recipient, "Recipient cannot be null");
		Validate.notEmpty(type, "Type cannot be empty");
		
		this.plugin = TitanChat.instance();
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
	
	public Status post() {
		if (!recipient.onConversation(sender, message, false))
			return Status.CANCELLED;
		
		ConverseEvent event = new ConverseEvent(this);
		
		Set<Node> recipients = event.getRecipients();
		
		if (!recipients.contains(sender))
			recipients.add(sender);
		
		plugin.getServer().getPluginManager().callEvent(event);
		
		if (!event.inStatus(Status.PENDING))
			return event.getStatus();
		
		if (!recipients.contains(sender))
			recipients.add(sender);
		
		String line = format.replace("%message", message);
		
		for (Node node : recipients) {
			if (!node.onConversation(sender, message, false))
				continue;
			
			node.sendLine(line);
		}
		
		event.setStatus(Status.SENT);
		
		return Status.SENT;
	}
	
	public Conversation setFormat(String format) {
		Validate.isTrue(format != null && format.contains("%message"), "Format cannot forgo '%meesage'");
		
		this.format = format;
		return this;
	}
	
	public Conversation setMessage(String message) {
		this.message = (message != null) ? message : "";
		return this;
	}
}