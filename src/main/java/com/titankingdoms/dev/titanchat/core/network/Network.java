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

package com.titankingdoms.dev.titanchat.core.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.titankingdoms.dev.titanchat.core.EndPoint;
import com.titankingdoms.dev.titanchat.core.Message;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public final class Network implements EndPoint {
	
	private final String name;
	
	private final Map<String, EndPoint> endpoints;
	
	public Network(String name) {
		this.name = name;
		this.endpoints = new HashMap<String, EndPoint>();
	}
	
	public Set<EndPoint> getLinkedPoints() {
		return new HashSet<EndPoint>(endpoints.values());
	}
	
	public String getName() {
		return name;
	}
	
	public String getPointType() {
		return "Network";
	}
	
	public Message handleMessage(EndPoint sender, String message) {
		return new Message(sender, new HashSet<EndPoint>(), "", message);
	}
	
	public boolean isLinked(EndPoint endpoint) {
		return false;
	}
	
	public void link(EndPoint endpoint) {
		
	}
	
	public void messageIn(EndPoint sender, String format, String message) {
		if (sender == null || format == null || format.isEmpty() || message == null || message.isEmpty())
			return;
	}
	
	public void messageOut(EndPoint recipient, String message) {
		
	}
	
	public void notice(String... messages) {
		for (EndPoint endpoint : getLinkedPoints())
			if (endpoint instanceof Participant)
				endpoint.notice(messages);
	}
	
	public void unlink(EndPoint endpoint) {
		
	}
}