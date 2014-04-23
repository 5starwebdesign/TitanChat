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

package com.titankingdoms.dev.titanchat.console;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.command.ConsoleCommandSender;

import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.conversation.Conversation;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.conversation.Provider;

public final class Console implements Node, Provider<Console> {
	
	protected final TitanChat plugin;
	
	private static final String NAME = "CONSOLE";
	private static final String TYPE = "Console";
	
	private volatile Node exploring;
	
	private final Map<String, Node> connected = new HashMap<String, Node>();
	
	private final Set<Node> terminus = ImmutableSet.<Node>builder().add(this).build();
	
	public Console() {
		this.plugin = TitanChat.instance();
	}
	
	@Override
	public void attach(Node node) {
		Validate.notNull(node, "Node cannot be null");
		
		String tag = node.getName() + "::" + node.getType();
		
		if (connected.containsKey(tag))
			return;
		
		connected.put(tag, node);
		
		if (!node.isConnected(this))
			node.attach(this);
		
		if (exploring == null || !exploring.equals(node))
			exploring = node;
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
		
		if (exploring != null && exploring.equals(node))
			exploring = (!connected.isEmpty()) ? getConnected().toArray(new Node[0])[0] : null;
	}
	
	@Override
	public Console get(String name) {
		return this;
	}
	
	@Override
	public Collection<Node> getConnected() {
		return null;
	}
	
	public ConsoleCommandSender getConsoleSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public Collection<Node> getTerminusNodes() {
		return terminus;
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
	public boolean has(Console console) {
		return console != null && equals(console);
	}
	
	@Override
	public boolean isConnected(Node node) {
		return node != null && connected.containsKey(node.getType() + "::" + node.getName());
	}
	
	@Override
	public boolean isConversable(Node sender, String message, String type) {
		return false;
	}
	
	@Override
	public Conversation onConversation(Node sender, String message) {
		return null;
	}
	
	@Override
	public void sendLine(String line) {
		getConsoleSender().sendMessage(line);
	}
}