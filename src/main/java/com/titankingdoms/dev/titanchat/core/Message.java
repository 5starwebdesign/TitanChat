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

package com.titankingdoms.dev.titanchat.core;

import java.util.Set;

public final class Message {
	
	private final EndPoint sender;
	private final Set<EndPoint> recipients;
	
	private final String format;
	private final String message;
	
	public Message(EndPoint sender, Set<EndPoint> recipients, String format, String message) {
		this.sender = sender;
		this.recipients = recipients;
		this.format = format;
		this.message = message;
	}
	
	public String getFormat() {
		return format;
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
}