/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.event.chat;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.event.util.Message;

/**
 * MessageSendEvent - Called when messages are to be sent
 * 
 * @author NodinChan
 *
 */
public final class MessageSendEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Player sender;
	
	private final Channel sentFrom;
	
	private final List<Player> recipants;
	
	private final Message message;
	
	private boolean cancelled = false;
	
	public MessageSendEvent(Player sender, Channel sentFrom, List<Player> recipants, Message message) {
		this.sender = sender;
		this.sentFrom = sentFrom;
		this.recipants = recipants;
		this.message = message;
	}
	
	public MessageSendEvent(Player sender, Channel sentFrom, Player[] recipants, Message message) {
		this(sender, sentFrom, Arrays.asList(recipants), message);
	}
	
	/**
	 * Gets the format
	 * 
	 * @return The format
	 */
	public String getFormat() {
		return message.getFormat();
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	/**
	 * Gets the message
	 * 
	 * @return The message
	 */
	public String getMessage() {
		return message.getMessage();
	}
	
	/**
	 * Gets the recipants
	 * 
	 * @return The recipants
	 */
	public List<Player> getRecipants() {
		return recipants;
	}
	
	/**
	 * Gets the sender
	 * 
	 * @return The sender
	 */
	public Player getSender() {
		return sender;
	}
	
	/**
	 * Gets the channel sent from
	 * 
	 * @return The channel the message is sent from
	 */
	public Channel getSentFrom() {
		return sentFrom;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Sets the format
	 * 
	 * @param format The new format
	 */
	public void setFormat(String format) {
		this.message.setFormat(format);
	}
	
	/**
	 * Sets the message
	 * 
	 * @param message The new message
	 */
	public void setMessage(String message) {
		this.message.setMessage(message);
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}