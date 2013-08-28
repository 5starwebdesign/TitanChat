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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.api.EndPoint;

public final class ConverseEvent extends Event implements Cloneable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final EndPoint sender;
	private final Set<EndPoint> recipients;
	
	private String format;
	private String message;
	
	public ConverseEvent(EndPoint sender, Set<EndPoint> recipients, String format, String message) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notEmpty(format, "Format cannot be empty");
		Validate.notEmpty(message, "Message cannot be empty");
		
		this.sender = sender;
		this.recipients = (recipients != null) ? recipients : new HashSet<EndPoint>();
		this.format = format;
		this.message = message;
	}
	
	public ConverseEvent(EndPoint sender, String format, String message) {
		this(sender, new HashSet<EndPoint>(), format, message);
	}
	
	@Override
	public ConverseEvent clone() {
		return new ConverseEvent(sender, new HashSet<EndPoint>(recipients), format, message);
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
	
	public Set<EndPoint> getRecipients() {
		return recipients;
	}
	
	public EndPoint getSender() {
		return sender;
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