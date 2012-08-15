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

package com.titankingdoms.nodinchan.titanchat.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.titankingdoms.nodinchan.titanchat.event.util.Message;

public final class WhisperEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final CommandSender sender;
	private final CommandSender recipant;
	
	private final Message message;
	
	public WhisperEvent(CommandSender sender, CommandSender recipant, Message message) {
		this.sender = sender;
		this.recipant = recipant;
		this.message = message;
	}
	
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
	
	public String getMessage() {
		return message.getMessage();
	}
	
	public CommandSender getRecipant() {
		return recipant;
	}
	
	public CommandSender getSender() {
		return sender;
	}
	
	public void setFormat(String format) {
		this.message.setFormat(format);
	}
	
	public void setMessage(String message) {
		this.message.setMessage(message);
	}
}