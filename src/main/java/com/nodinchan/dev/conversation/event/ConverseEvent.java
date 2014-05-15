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

package com.nodinchan.dev.conversation.event;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.nodinchan.dev.conversation.Conversation;
import com.nodinchan.dev.conversation.Node;

public final class ConverseEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Conversation conversation;
	
	private final Set<Node> recipients;
	
	private boolean cancelled;
	
	public ConverseEvent(Conversation conversation) {
		super(!Bukkit.isPrimaryThread());
		Validate.notNull(conversation, "Conversation cannot be null");
		
		this.conversation = conversation;
		this.recipients = new HashSet<Node>(conversation.getRecipient().getTerminusNodes());
		this.cancelled = false;
	}
	
	public String getConversationType() {
		return conversation.getType();
	}
	
	public String getFormat() {
		return conversation.getFormat();
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Node getIntermediate() {
		return conversation.getRecipient();
	}
	
	public String getMessage() {
		return conversation.getMessage();
	}
	
	public Set<Node> getRecipients() {
		return recipients;
	}
	
	public Node getSender() {
		return conversation.getSender();
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public void setFormat(String format) {
		conversation.setFormat(format);
	}
	
	public void setMessage(String message) {
		conversation.setMessage(message);
	}
}