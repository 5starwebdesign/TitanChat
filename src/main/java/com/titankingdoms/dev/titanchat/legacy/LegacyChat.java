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

package com.titankingdoms.dev.titanchat.legacy;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.api.conversation.Connection;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.conversation.NodeManager;

public final class LegacyChat implements Node, NodeManager<LegacyChat> {
	
	private static final String NAME = "Legacy";
	private static final String TYPE = "Minecraft";
	
	private final Connection connection = new Connection(this);
	
	private final Set<LegacyChat> chat = ImmutableSet.<LegacyChat>builder().add(this).build();
	
	@Override
	public LegacyChat get(String name) {
		return this;
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