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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.nodinchan.titanchat.event.util.Message;

public final class MessageReceiveEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Player sender;
	
	private final Message message;
	
	private final Map<Player, Message> messages;
	private final Map<Player, Boolean> cancelled;
	
	public MessageReceiveEvent(Player sender, List<Player> recipants, Message message) {
		this.sender = sender;
		this.message = message;
		this.messages = new HashMap<Player, Message>();
		this.cancelled = new HashMap<Player, Boolean>();
		
		for (Player recipant : recipants) {
			messages.put(recipant, message.clone());
			cancelled.put(recipant, false);
		}
	}
	
	public MessageReceiveEvent(Player sender, Player[] recipants, Message message) {
		this(sender, Arrays.asList(recipants), message);
	}
	
	public String getFormat(Player recipant) {
		if (messages.containsKey(recipant))
			return messages.get(recipant).getFormat();
		
		return message.getFormat();
	}
	
	public String getFormattedMessage(Player recipant) {
		String format = getFormat(recipant);
		String message = getMessage(recipant);
		
		return format.replace("%message", message);
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public String getMessage(Player recipant) {
		if (messages.containsKey(recipant))
			return messages.get(recipant).getMessage();
		
		return message.getMessage();
	}
	
	public List<Player> getRecipants() {
		List<Player> recipants = new ArrayList<Player>(messages.keySet());
		
		for (Player recipant : messages.keySet())
			if (cancelled.containsKey(recipant) && cancelled.get(recipant))
				recipants.add(recipant);
		
		return recipants;
	}
	
	public Player getSender() {
		return sender;
	}
	
	public boolean isCancelled(Player recipant) {
		if (cancelled.containsKey(recipant))
			return cancelled.get(recipant);
		
		return true;
	}
	
	public void setCancelled(Player recipant, boolean cancelled) {
		this.cancelled.put(recipant, cancelled);
	}
	
	public void setFormat(Player recipant, String format) {
		if (messages.containsKey(recipant))
			messages.get(recipant).setFormat(format);
	}
	
	public void setMessage(Player recipant, String message) {
		if (messages.containsKey(recipant))
			messages.get(recipant).setMessage(message);
	}
}