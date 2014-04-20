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

package com.titankingdoms.dev.titanchat.legacy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.api.conversation.Conversation;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.conversation.Provider;

public final class LegacyChat implements Node, Provider<LegacyChat> {
	
	private final Map<String, Node> connected = new HashMap<String, Node>();
	
	@Override
	public void attach(Node node) {
		Validate.notNull(node, "Node cannot be null");
		
		String tag = node.getName() + "::" + node.getType();
		
		if (connected.containsKey(tag))
			return;
		
		connected.put(tag, node);
		
		if (!node.isConnected(this))
			node.attach(this);
	}
	
	@Override
	public void detach(Node node) {
		Validate.notNull(node, "Node cannot be null");
		
		String tag = node.getName() + "::" + node.getType();
		
		if (!connected.containsKey(tag))
			return;
		
		connected.remove(tag);
		
		if (node.isConnected(this))
			node.detach(this);
	}
	
	@Override
	public LegacyChat get(String name) {
		return this;
	}
	
	@Override
	public Set<Node> getConnected() {
		return ImmutableSet.<Node>builder().addAll(connected.values()).build();
	}
	
	@Override
	public String getName() {
		return "Legacy";
	}
	
	@Override
	public Set<Node> getTerminusNodes() {
		Builder<Node> terminus = ImmutableSet.builder();
		
		for (Node node : connected.values())
			terminus.addAll(node.getTerminusNodes());
		
		return terminus.build();
	}
	
	@Override
	public String getType() {
		return "Minecraft";
	}
	
	@Override
	public boolean has(String name) {
		return true;
	}
	
	@Override
	public boolean has(LegacyChat item) {
		return true;
	}
	
	@Override
	public boolean isConnected(Node node) {
		return node.getType().equals("User") && connected.containsKey(node.getName());
	}
	
	@Override
	public boolean isConversable(Node sender, String message, String type) {
		return true;
	}
	
	@Override
	public Conversation onConversation(Node sender, String message) {
		return new Conversation(sender, this, "%message", message, "Normal");
	}
	
	@Override
	public void sendRawLine(String line) {
		for (Node node : getTerminusNodes())
			node.sendRawLine(line);
	}
}