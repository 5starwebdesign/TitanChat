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

package com.titankingdoms.dev.titanchat.api.user;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.user.meta.AdapterHandler;
import com.titankingdoms.dev.titanchat.api.user.meta.MetaAdapter;
import com.titankingdoms.dev.titanchat.api.user.meta.Metadata;
import com.titankingdoms.dev.titanchat.api.user.storage.UserInfoStorage;

public abstract class User implements Node {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private Metadata metadata;
	
	private volatile Node exploring;
	
	private final Map<String, Node> connected = new HashMap<String, Node>();
	private final Set<Node> terminus = new HashSet<Node>();
	
	public User(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(!Pattern.compile("\\W").matcher(name).find(), "Name cannot contain non-word characters");
		
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.terminus.add(this);
	}
	
	@Override
	public void attach(Node node) {
		Validate.notNull(node, "Node cannot be null");
		
		String tag = node.getName() + "::" + node.getType();
		
		if (connected.containsKey(node))
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
		
		if (!connected.containsKey(node))
			return;
		
		connected.remove(tag);
		
		if (node.isConnected(this))
			node.detach(this);
		
		if (exploring != null && exploring.equals(node))
			exploring = (!connected.isEmpty()) ? getConnected().toArray(new Node[0])[0] : null;
	}
	
	public Collection<Node> getConnected() {
		return Collections.unmodifiableCollection(connected.values());
	}
	
	public final Metadata getMetadata() {
		if (metadata == null) {
			this.metadata = new Metadata(this);
			
			if (!plugin.getSystem().hasManager(UserManager.class))
				throw new UnsupportedOperationException("UserManager not found");
			
			UserInfoStorage storage = plugin.getManager(UserManager.class).getStorage();
			
			for (Entry<String, String> metadata : storage.get(getName()).getMetadata().entrySet()) {
				if (plugin.getSystem().hasManager(AdapterHandler.class)) {
					MetaAdapter adapter = plugin.getManager(AdapterHandler.class).get(metadata.getKey());
					this.metadata.setData(metadata.getKey(), adapter.fromString(metadata.getValue()));
					continue;
				}
				
				this.metadata.setData(metadata.getKey(), metadata.getValue());
			}
		}
		
		return metadata;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public Set<Node> getTerminusNodes() {
		return Collections.unmodifiableSet(terminus);
	}
	
	@Override
	public final String getType() {
		return "User";
	}
	
	public Node getViewing() {
		return exploring;
	}
	
	@Override
	public boolean isConnected(Node node) {
		return node != null && connected.containsKey(node.getName() + "::" + node.getType());
	}
	
	public boolean isViewing(Node node) {
		return (node == null && exploring == null) || exploring.equals(node);
	}
}