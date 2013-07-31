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

package com.titankingdoms.dev.titanchat.core.channel;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.EndPoint;

public abstract class Channel implements EndPoint {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	public Channel(String name) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Channel)
			return getName().equals(((Channel) object).getName());
		
		return false;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final String getType() {
		return "Channel";
	}
	
	@Override
	public void onMessageIn(EndPoint sender, String format, String message) {}
	
	@Override
	public void onMessageOut(EndPoint recipient, String format, String message) {}
	
	@Override
	public void sendNotice(String... messages) {}
	
	@Override
	public String toString() {
		return "Channel: {name: " + getName() + "}";
	}
}