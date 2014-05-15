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

package com.nodinchan.dev.titanchat.conversation.legacy;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.ImmutableSet;
import com.nodinchan.dev.conversation.Connection;
import com.nodinchan.dev.conversation.Conversation;
import com.nodinchan.dev.conversation.Node;
import com.nodinchan.dev.conversation.NodeManager;

public final class LegacyChat implements Node, NodeManager<LegacyChat> {
	
	private static final String NAME = "Legacy";
	private static final String TYPE = "Minecraft";
	
	private final Connection connection;
	
	private final Set<LegacyChat> chat;
	
	public LegacyChat() {
		this.connection = new Connection(this);
		this.chat = ImmutableSet.of(this);
	}
	
	@Override
	public LegacyChat get(String name) {
		return (name == null || name.isEmpty()) ? null : this;
	}
	
	@Override
	public Collection<LegacyChat> getAll() {
		return chat;
	}
	
	@Override
	public Connection getConnection() {
		return connection;
	}
	
	@Override
	public Conversation getConversation(Node sender, String message) {
		return new Conversation(sender, this, "Normal").setMessage(message);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public Set<Node> getTerminusNodes() {
		Builder<Node> terminus = ImmutableSet.builder();
		
		for (Node node : connection.getConnections())
			terminus.addAll(node.getTerminusNodes());
		
		return terminus.build();
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public boolean has(String name) {
		return name != null && !name.isEmpty();
	}
	
	@Override
	public boolean isConversable(Node sender, String message, String type) {
		return true;
	}
	
	@Override
	public void register(LegacyChat chat) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void sendLine(String line) {
		for (Node node : getTerminusNodes())
			node.sendLine(line);
	}
	
	@Override
	public void unregister(LegacyChat chat) {
		throw new UnsupportedOperationException();
	}
}