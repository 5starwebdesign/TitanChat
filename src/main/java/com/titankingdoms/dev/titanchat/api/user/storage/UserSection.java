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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

public class UserSection {
	
	private final String name;
	
	private NodeCache current;
	
	private final List<NodeCache> nodes;
	
	private final Map<String, String> metadata;
	
	public UserSection(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		
		this.name = name;
		this.nodes = new ArrayList<NodeCache>();
		this.metadata = new HashMap<String, String>();
	}
	
	public void addNode(NodeCache node) {
		if (nodes.contains(node))
			return;
		
		nodes.add(node);
	}
	
	public NodeCache getCurrentNode() {
		return current;
	}
	
	public final String getName() {
		return name;
	}
	
	public List<NodeCache> getNodes() {
		return new ArrayList<NodeCache>(nodes);
	}
	
	public Map<String, String> getMetadata() {
		return new HashMap<String, String>(metadata);
	}
	
	public void removeNode(NodeCache node) {
		if (!nodes.contains(node))
			return;
		
		nodes.remove(node);
	}
	
	public void setCurrentNode(NodeCache current) {
		this.current = current;
	}
	
	public void setMetadata(String key, String value) {
		Validate.notEmpty(key, "Key cannot be empty");
		
		if (value != null)
			metadata.put(key, value);
		else
			metadata.remove(key);
	}
}