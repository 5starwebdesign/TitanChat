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

package com.titankingdoms.dev.titanchat.api.user.storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.meta.AdapterHandler;
import com.titankingdoms.dev.titanchat.api.meta.MetaAdapter;
import com.titankingdoms.dev.titanchat.api.meta.Metadata.Meta;
import com.titankingdoms.dev.titanchat.api.user.User;

public class UserInfo {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	protected String viewing = "";
	
	protected final List<String> connected;
	
	protected final Map<String, String> metadata;
	
	public UserInfo(User user) {
		this((user != null) ? user.getName() : "");
		
		this.viewing = user.getViewing().getName() + "::" + user.getViewing().getType();
		
		for (Node node : user.getConnected())
			this.connected.add(node.getName() + "::" + node.getType());
		
		for (Entry<String, Meta> metadata : user.getMetadata().getData().entrySet()) {
			if (plugin.getSystem().hasManager(AdapterHandler.class)) {
				MetaAdapter adapter = plugin.getManager(AdapterHandler.class).get(metadata.getKey());
				this.metadata.put(metadata.getKey(), adapter.toString(metadata.getValue()));
				continue;
			}
			
			this.metadata.put(metadata.getKey(), metadata.getValue().getValue());
		}
	}
	
	public UserInfo(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.connected = new LinkedList<String>();
		this.metadata = new HashMap<String, String>();
	}
	
	public List<String> getConnected() {
		return Collections.unmodifiableList(connected);
	}
	
	public Map<String, String> getMetadata() {
		return Collections.unmodifiableMap(metadata);
	}
	
	public final String getName() {
		return name;
	}
	
	public String getViewing() {
		return viewing;
	}
}