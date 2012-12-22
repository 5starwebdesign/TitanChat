/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.event;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;
import com.titankingdoms.nodinchan.titanchat.format.FormatHandler;

public final class ChannelChatEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Participant sender;
	private final Set<Participant> recipients;
	
	private final Channel channel;
	
	private String format;
	private String message;
	
	public ChannelChatEvent(Participant sender, Channel channel, String message) {
		this(sender, new HashSet<Participant>(), channel, FormatHandler.DEFAULT_FORMAT, message);
	}
	
	public ChannelChatEvent(Participant sender, Channel channel, String format, String message) {
		this(sender, new HashSet<Participant>(), channel, format, message);
	}
	
	public ChannelChatEvent(Participant sender, Set<Participant> recipients, Channel channel, String message) {
		this(sender, recipients, channel, FormatHandler.DEFAULT_FORMAT, message);
	}
	
	public ChannelChatEvent(Participant sender, Set<Participant> recipients, Channel channel, String format, String message) {
		this.sender = sender;
		this.recipients = recipients;
		this.channel = channel;
		this.format = format;
		this.message = message;
	}
	
	public boolean addRecipient(Participant recipient) {
		if (recipient == null)
			return false;
		
		return (!this.recipients.contains(recipient)) ? this.recipients.add(recipient) : false;
	}
	
	public Channel getChannel() {
		return channel;
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
	
	public Set<Participant> getRecipients() {
		return new HashSet<Participant>(recipients);
	}
	
	public Participant getSender() {
		return sender;
	}
	
	public boolean removeRecipient(Participant recipient) {
		if (recipient == null)
			return false;
		
		return (this.recipients.contains(recipient)) ? this.recipients.remove(recipient) : false;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}