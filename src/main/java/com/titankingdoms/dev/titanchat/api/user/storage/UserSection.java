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

import java.util.*;

import org.apache.commons.lang.Validate;

public final class UserSection {
	
	private final String name;
	
	private String node;
	
	private final Set<String> nodes;
	
	private final Map<String, String> metadata;
	
	public UserSection(String name) {
		this.name = name;
		this.node = "";
		this.nodes = new HashSet<String>();
		this.metadata = new HashMap<String, String>();
	}
	
	public void addNode(String name, String type) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.notEmpty(type, "Type cannot be empty");
		
		nodes.add(name + "::" + type);
	}
	
	public String getCurrentNode() {
		return node;
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, String> getMetadata() {
		return new HashMap<String, String>(metadata);
	}
	
	public Set<String> getNodes() {
		return nodes;
	}
	
	public void removeNode(String name, String type) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.notEmpty(type, "Type cannot be empty");
		
		nodes.remove(name + "::" + type);
	}
	
	public void setCurrentNode(String name, String type) {
		if (name == null || name.isEmpty() || type == null || type.isEmpty())
			this.node = "";
		else
			this.node = name + "::" + type;
	}
	
	public void setMetadata(String key, String value) {
		Validate.notEmpty(key, "Key cannot be empty");
		
		if (value != null)
			this.metadata.put(key, value);
		else
			this.metadata.remove(key);
	}
	
	public void setMetadata(Map<String, String> metadata) {
		this.metadata.clear();
		this.metadata.putAll(metadata);
	}
}