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

import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link ChannelChatEvent} - Called when {@link Participant}s chat in {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public final class ChannelChatEvent extends ChannelEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Participant sender;
	private final Set<Participant> recipients;
	
	private String format;
	private String message;
	
	public ChannelChatEvent(Participant sender, Channel channel, String message) {
		this(sender, new HashSet<Participant>(), channel, "", message);
	}
	
	public ChannelChatEvent(Participant sender, Channel channel, String format, String message) {
		this(sender, new HashSet<Participant>(), channel, format, message);
	}
	
	public ChannelChatEvent(Participant sender, Set<Participant> recipients, Channel channel, String message) {
		this(sender, recipients, channel, "", message);
	}
	
	public ChannelChatEvent(Participant sender, Set<Participant> recipients, Channel channel, String format, String message) {
		super(channel);
		this.sender = sender;
		this.recipients = recipients;
		this.format = format;
		this.message = message;
	}
	
	/**
	 * Adds the {@link Participant} to the recipients
	 * 
	 * @param recipient The {@link Participant} to add
	 * 
	 * @return True if successfully added
	 */
	public boolean addRecipient(Participant recipient) {
		if (recipient == null)
			return false;
		
		return (!this.recipients.contains(recipient)) ? this.recipients.add(recipient) : false;
	}
	
	/**
	 * Gets the format to be used
	 * 
	 * @return The format
	 */
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
	
	/**
	 * Gets the message to be sent
	 * 
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the recipients of the message
	 * 
	 * @return The recipients
	 */
	public Set<Participant> getRecipients() {
		return new HashSet<Participant>(recipients);
	}
	
	/**
	 * Gets the sender of the message
	 * 
	 * @return The sender
	 */
	public Participant getSender() {
		return sender;
	}
	
	/**
	 * Removes the {@link Participant} from the recipients
	 * 
	 * @param recipient The {@link Participant} to remove
	 * 
	 * @return True if successfully removed
	 */
	public boolean removeRecipient(Participant recipient) {
		if (recipient == null)
			return false;
		
		return (this.recipients.contains(recipient)) ? this.recipients.remove(recipient) : false;
	}
	
	/**
	 * Sets the format to be used
	 * 
	 * @param format The new format
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
	/**
	 * Sets the message to be sent
	 * 
	 * @param message The new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}