/*
 *     Copyright (C) 2014  Nodin Chan
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

package com.titankingdoms.dev.titanchat.conversation.user.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.metadata.AdapterHandler;
import com.titankingdoms.dev.titanchat.api.metadata.Metadata.Meta;
import com.titankingdoms.dev.titanchat.conversation.user.User;

public final class UserData {
	
	private final TitanChat plugin;
	
	private final UUID id;
	
	private String viewing = "";
	
	private Set<String> connected = new HashSet<>();
	
	private Map<String, String> metadata = new HashMap<>();
	
	public UserData(User user) {
		Validate.notNull(user, "User cannot be null");
		
		this.plugin = TitanChat.instance();
		this.id = user.getUniqueId();
		
		if (!user.isViewing(null))
			this.viewing = user.getViewing().getName() + "::" + user.getViewing().getType();
		
		for (Node node : user.getConnection().getConnections())
			this.connected.add(node.getName() + "::" + node.getType());
		
		for (Entry<String, Meta> entry : user.getMetadata().getData().entrySet()) {
			Meta meta = entry.getValue();
			
			String key = entry.getKey();
			String value;
			
			if (plugin.getSystem().isLoaded(AdapterHandler.class))
				value = plugin.getSystem().getManager(AdapterHandler.class).get(key).toString(meta);
			else
				value = meta.getValue();
			
			this.metadata.put(key, value);
		}
	}
	
	public Set<String> getConnected() {
		return ImmutableSet.copyOf(connected);
	}
	
	public Map<String, String> getMetadata() {
		return ImmutableMap.copyOf(metadata);
	}
	
	public UUID getUniqueId() {
		return id;
	}
	
	public String getViewing() {
		return viewing;
	}
	
	public void setConnected(Collection<String> connected) {
		this.connected = toValid(connected);
	}
	
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = (metadata != null) ? new HashMap<>(metadata) : new HashMap<String, String>();
	}
	
	public void setViewing(String viewing) {
		this.viewing = toValid(viewing);
	}
	
	private String toValid(String node) {
		if (node == null || node.isEmpty())
			return "";
		
		switch (node.split("::").length) {
		
		case 0:
		case 1:
			return "";
			
		case 2:
			return node;
			
		default:
			return node.substring(0, node.indexOf("::", node.indexOf("::") + 1));
		}
	}
	
	private Set<String> toValid(Collection<String> nodes) {
		Set<String> valid = new HashSet<>();
		
		if (nodes != null) {
			for (String node : nodes) {
				if (node == null || node.isEmpty())
					continue;
				
				valid.add(toValid(node));
			}
		}
		
		return valid;
	}
}