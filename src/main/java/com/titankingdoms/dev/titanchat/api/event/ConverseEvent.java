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

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.api.Point;

public final class ConverseEvent extends Event implements Cancellable, Cloneable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Point sender;
	private final Point recipient;
	private final Collection<Point> relay;
	
	private String format;
	private String message;
	
	private boolean cancelled;
	
	public ConverseEvent(Point sender, Point recipient, Collection<Point> relay, String format, String message) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(recipient, "Recipient cannot be null");
		Validate.notEmpty(format, "Format cannot be empty");
		Validate.notEmpty(message, "Message cannot be empty");
		
		this.sender = sender;
		this.recipient = recipient;
		this.relay = (relay != null) ? relay : new HashSet<Point>();
		this.format = format;
		this.message = message;
		this.cancelled = false;
	}
	
	public ConverseEvent(Point sender, Point recipient, String format, String message) {
		this(sender, recipient, recipient.getRecipients(sender), format, message);
	}
	
	@Override
	public ConverseEvent clone() {
		return new ConverseEvent(sender, recipient, new HashSet<Point>(relay), format, message);
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
	
	public Point getRecipient() {
		return recipient;
	}
	
	public Collection<Point> getRelayPoints() {
		return relay;
	}
	
	public Point getSender() {
		return sender;
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
		Validate.notEmpty((format != null) ? format : "", "A format is needed");
		this.format = format;
	}
	
	public void setMessage(String message) {
		Validate.notEmpty((message != null) ? message : "", "A message is needed");
		this.message = message;
	}
}