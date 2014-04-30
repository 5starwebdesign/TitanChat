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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList.Builder;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent.Status;

public final class Network implements Manager<NodeManager<? extends Node>> {
	
	private static final String NAME = "Network";
	
	private static final Set<Class<? extends Manager<?>>> DEPENDENCIES;
	
	private final Map<String, NodeManager<? extends Node>> managers;
	
	public Network() {
		this.managers = new HashMap<>();
	}
	
	static {
		DEPENDENCIES = ImmutableSet.of();
	}
	
	@Override
	public NodeManager<? extends Node> get(String type) {
		return managers.get(type.toLowerCase());
	}
	
	@Override
	public Collection<NodeManager<? extends Node>> getAll() {
		return ImmutableSet.<NodeManager<? extends Node>>builder().addAll(managers.values()).build();
	}
	
	public Node getNode(String type, String name) {
		return (has(type)) ? get(type).get(name) : null;
	}
	
	@Override
	public Set<Class<? extends Manager<?>>> getDependencies() {
		return DEPENDENCIES;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean has(String type) {
		return type != null && !type.isEmpty() && managers.containsKey(type.toLowerCase());
	}
	
	@Override
	public boolean has(NodeManager<? extends Node> manager) {
		return manager != null && has(manager.getType()) && get(manager.getType()).equals(manager);
	}
	
	public boolean hasNode(String type, String name) {
		return has(type) && get(type).has(name);
	}
	
	@Override
	public void load() {}
	
	@Override
	public List<String> match(String type) {
		if (type == null || type.isEmpty())
			return ImmutableList.copyOf(managers.keySet());
		
		Builder<String> matches = ImmutableList.builder();
		
		for (String manager : managers.keySet()) {
			if (!manager.startsWith(type.toLowerCase()))
				continue;
			
			matches.add(manager);
		}
		
		return matches.build();
	}
	
	public static Status post(Conversation conversation) {
		if (conversation == null)
			return Status.CANCELLED;
		
		ConverseEvent event = new ConverseEvent(conversation);
		
		Set<Node> recipients = event.getRecipients();
		
		if (!recipients.contains(conversation.getSender()))
			recipients.add(conversation.getSender());
		
		TitanChat.instance().getServer().getPluginManager().callEvent(event);
		
		if (event.inStatus(Status.PENDING)) {
			if (!recipients.contains(conversation.getSender()))
				recipients.add(conversation.getSender());
			
			String line = conversation.getFormat().replace("%message", conversation.getMessage());
			
			for (Node node : recipients) {
				if (!node.isConversable(event.getSender(), event.getIntermediate(), event.getMessage()))
					continue;
				
				node.sendLine(line);
			}
			
			event.setStatus(Status.SENT);
		}
		
		return event.getStatus();
	}
	
	public static Status post(Node sender, Node recipient, String type, String format, String message) {
		return post(new Conversation(sender, recipient, type).setFormat(format).setMessage(message));
	}
	
	@Override
	public void register(NodeManager<? extends Node> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		Validate.isTrue(!has(manager), "Manager already registered");
		
		managers.put(manager.getType().toLowerCase(), manager);
	}
	
	@Override
	public void reload() {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(NodeManager<? extends Node> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		Validate.isTrue(has(manager), "Manager not registered");
		
		managers.remove(manager.getType().toLowerCase());
	}
}