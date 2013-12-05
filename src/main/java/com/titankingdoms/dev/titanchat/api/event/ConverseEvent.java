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

package com.titankingdoms.dev.titanchat.api.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.api.conversation.Conversation;
import com.titankingdoms.dev.titanchat.api.conversation.Node;

public final class ConverseEvent extends Event implements Cancellable, Cloneable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Conversation conversation;
	
	private final Set<Node> recipients;
	
	private boolean cancelled;
	
	private ConverseEvent(Conversation conversation, Collection<Node> recipients) {
		Validate.notNull(conversation, "Conversation cannot be null");
		Validate.notNull(recipients, "Recipients cannot be null");
		
		this.conversation = conversation;
		this.recipients = new HashSet<Node>(recipients);
	}
	
	public ConverseEvent(Conversation conversation) {
		this(conversation, conversation.getRecipient().getTerminusNodes());
	}
	
	@Override
	public ConverseEvent clone() {
		return new ConverseEvent(conversation.clone(), recipients);
	}
	
	public String getFormat() {
		return conversation.getFormat();
	}
	
	public String getMessage() {
		return conversation.getMessage();
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Node getRecipient() {
		return conversation.getRecipient();
	}
	
	public Node getSender() {
		return conversation.getSender();
	}
	
	public Set<Node> getTerminusRecipients() {
		return recipients;
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