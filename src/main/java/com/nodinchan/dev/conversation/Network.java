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

package com.nodinchan.dev.conversation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableSet;
import com.nodinchan.dev.conversation.event.ConverseEvent;

public class Network {
	
	private final Map<String, NodeManager<? extends Node>> managers;
	
	public Network() {
		this.managers = new HashMap<>();
	}
	
	public Node getNode(String type, String name) {
		return (isManaged(type)) ? getNodeManager(type).get(name) : null;
	}
	
	public NodeManager<? extends Node> getNodeManager(String type) {
		return (type == null || type.isEmpty()) ? null : managers.get(type.toLowerCase());
	}
	
	public int getTypeCount() {
		return managers.size();
	}
	
	public boolean isManaged(String type) {
		return type != null && !type.isEmpty() && managers.containsKey(type.toLowerCase());
	}
	
	public boolean hasNode(String type, String name) {
		return isManaged(type) && getNodeManager(type).has(name);
	}
	
	public static boolean post(Plugin plugin, Conversation conversation) {
		if (conversation == null)
			return false;
		
		Node sender = conversation.getSender();
		String type = conversation.getType();
		
		if (!conversation.getRecipient().isConversable(sender, conversation.getMessage(), type))
			return false;
		
		ConverseEvent event = new ConverseEvent(conversation);
		plugin.getServer().getPluginManager().callEvent(event);
		
		Set<Node> recipients = ImmutableSet.<Node>builder().add(sender).addAll(event.getRecipients()).build();
		
		String line = event.getFormat().replace("%message", event.getMessage());
		
		for (Node recipient : recipients) {
			if (!recipient.isConversable(sender, event.getMessage(), type))
				continue;
			
			recipient.sendLine(line);
		}
		
		return true;
	}
	
	public void register(NodeManager<? extends Node> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		Validate.isTrue(!isManaged(manager.getType()), "Manager already registered");
		
		managers.put(manager.getType().toLowerCase(), manager);
	}
	
	public void unregister(String type) {
		Validate.notEmpty(type, "Type cannot be empty");
		Validate.isTrue(isManaged(type), "Manager not registered");
		
		managers.remove(type.toLowerCase());
	}
}