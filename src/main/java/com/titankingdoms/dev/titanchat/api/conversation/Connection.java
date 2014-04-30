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

package com.titankingdoms.dev.titanchat.api.conversation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet;

public class Connection {
	
	protected final Node node;
	
	private final Map<String, Node> connections;
	
	public Connection(Node node) {
		Validate.notNull(node, "Node cannot be null");
		
		this.node = node;
		this.connections = new HashMap<>();
	}
	
	public boolean connect(Node node) {
		if (node == null)
			return false;
		
		if (!isConnected(node))
			connections.put(node.getName() + "::" + node.getType(), node);
		
		if (node.getConnection() != null) {
			if (node.getConnection().isConnected(this.node) || node.getConnection().connect(this.node))
				return true;
		}
		
		connections.remove(node.getName() + "::" + node.getType());
		return false;
	}
	
	public boolean disconnect(Node node) {
		if (node == null)
			return false;
		
		if (isConnected(node))
			connections.remove(node.getName() + "::" + node.getType());
		
		if (node.getConnection() != null) {
			if (!node.getConnection().isConnected(this.node) || node.getConnection().disconnect(this.node))
				return true;
		}
		
		connections.put(node.getName() + "::" + node.getType(), node);
		return false;
	}
	
	public final Set<Node> getConnections() {
		return ImmutableSet.copyOf(connections.values());
	}
	
	public final Node getNode() {
		return node;
	}
	
	public final boolean isConnected(Node node) {
		return node != null && connections.containsValue(node);
	}
}