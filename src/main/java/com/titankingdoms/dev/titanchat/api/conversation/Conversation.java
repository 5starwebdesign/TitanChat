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

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent;

public class Conversation implements Cloneable {
	
	protected final TitanChat plugin;
	
	private final Node sender;
	private final Node recipient;
	
	private String format;
	private String message;
	
	public Conversation(Node sender, Node recipient, String format, String message) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(recipient, "Recipient cannot be null");
		Validate.notEmpty(format, "Format cannot be empty");
		Validate.notEmpty(message, "Message cannot be empty");
		
		this.plugin = TitanChat.getInstance();
		this.sender = sender;
		this.recipient = recipient;
		this.format = format;
		this.message = message;
	}
	
	@Override
	public Conversation clone() {
		return new Conversation(sender, recipient, format, message);
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
	
	public void post() {
		ConverseEvent event = new ConverseEvent(this);
		
		if (!event.getTerminusRecipients().contains(sender))
			event.getTerminusRecipients().add(sender);
		
		plugin.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		if (!event.getTerminusRecipients().contains(sender))
			event.getTerminusRecipients().add(sender);
		
		int recipient = 0;
		
		for (Node node : event.getTerminusRecipients())
			if (node.sendConversation(clone()))
				recipient++;
		
		if (recipient < 2)
			sender.sendRawLine("&7Nobody heard you...");
	}
	
	public void setFormat(String format) {
		Validate.notEmpty(format, "Format cannot be empty");
		this.format = format;
	}
	
	public void setMessage(String message) {
		Validate.notEmpty(message, "Message cannot be empty");
		this.message = message;
	}
}