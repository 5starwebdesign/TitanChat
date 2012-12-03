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

package com.titankingdoms.nodinchan.titanchat.event.util;

/**
 * Message - Represents a message
 * 
 * @author NodinChan
 *
 */
public final class Message implements Cloneable {
	
	private String format;
	private String message;
	
	public Message(String format, String message) {
		this.format = format;
		this.message = message;
	}
	
	@Override
	public Message clone() {
		return new Message(format, message);
	}
	
	/**
	 * Gets the format
	 * 
	 * @return The format
	 */
	public String getFormat() {
		return format;
	}
	
	/**
	 * Gets the message
	 * 
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the format
	 * 
	 * @param format The new format
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
	/**
	 * Sets the message
	 * 
	 * @param message The new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}