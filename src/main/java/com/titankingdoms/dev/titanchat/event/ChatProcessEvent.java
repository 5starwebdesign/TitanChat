/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.titankingdoms.dev.titanchat.event;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.core.EndPoint;

public final class ChatProcessEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final EndPoint sender;
	private final Set<EndPoint> recipients;
	
	private String format;
	private String message;
	
	public ChatProcessEvent(EndPoint sender, Set<EndPoint> recipients, String format, String message) {
		this.sender = sender;
		this.recipients = (recipients != null) ? recipients : new HashSet<EndPoint>();
		this.format = (format != null) ? format : "";
		this.message = (message != null) ? message : "";
	}
	
	public ChatProcessEvent(EndPoint sender, String format, String message) {
		this(sender, new HashSet<EndPoint>(), format, message);
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
		this.format = (format != null) ? format : "";
	}
	
	public void setMessage(String message) {
		this.message = (message != null) ? message : "";
	}
}