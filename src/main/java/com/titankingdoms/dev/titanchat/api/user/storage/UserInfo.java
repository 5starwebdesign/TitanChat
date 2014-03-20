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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
	
	public UserInfo(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.viewing = "";
		this.connected = Collections.unmodifiableList(new ArrayList<String>());
		this.metadata = Collections.unmodifiableMap(new HashMap<String, String>());
	}
	
	public UserInfo(User user) {
		Validate.notNull(user, "User cannot be null");
		
		this.plugin = TitanChat.getInstance();
		this.name = user.getName();
		
		if (user.getViewing() != null)
			this.viewing = user.getViewing().getName() + "::" + user.getViewing().getType();
		else
			this.viewing = "";
		
		List<String> connected = new ArrayList<String>();
		
		for (Node node : user.getConnected())
			connected.add(node.getName() + "::" + node.getType());
		
		this.connected = Collections.unmodifiableList(connected);
		
		Map<String, String> metadata = new HashMap<String, String>();
		
		for (Entry<String, Meta> meta : user.getMetadata().getData().entrySet()) {
			if (plugin.getSystem().hasManager(AdapterHandler.class)) {
				MetaAdapter adapter = plugin.getManager(AdapterHandler.class).get(meta.getKey());
				metadata.put(meta.getKey(), adapter.toString(meta.getValue()));
				continue;
			}
			
			metadata.put(meta.getKey(), meta.getValue().getValue());
		}
		
		this.metadata = Collections.unmodifiableMap(metadata);
	}
	
	public List<String> getConnected() {
		return connected;
	}
	
	public Map<String, String> getMetadata() {
		return metadata;
	}
	
	public final String getName() {
		return name;
	}
	
	public String getViewing() {
		return viewing;
	}
}