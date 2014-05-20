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

package com.nodinchan.dev.titanchat.api.conversation.user;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableSet;
import com.nodinchan.dev.conversation.Conversation;
import com.nodinchan.dev.conversation.Node;
import com.nodinchan.dev.metadata.Metadata;
import com.nodinchan.dev.titanchat.TitanChat;

public final class User implements Node {
	
	private final TitanChat plugin;
	
	private static final String TYPE = "User";
	
	private final UUID id;
	
	private final String name;
	
	private Metadata metadata;
	
	private final UserConnection connection;
	
	private volatile Node exploring;
	
	private final Set<Node> terminus;
	
	public User(UUID id) {
		Validate.notNull(id, "ID cannot be null");
		
		this.plugin = TitanChat.instance();
		this.id = id;
		this.name = getOfflinePlayer().getName();
		this.connection = new UserConnection(this);
		this.terminus = ImmutableSet.<Node>of(this);
	}
	
	@Override
	public UserConnection getConnection() {
		return connection;
	}
	
	@Override
	public Conversation getConversation(Node sender, String message) {
		return new Conversation(sender, this, "PrivMsg").setMessage(message);
	}
	
	public Metadata getMetadata() {
		if (metadata == null)
			this.metadata = new Metadata();
		
		return metadata;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return plugin.getServer().getOfflinePlayer(id);
	}
	
	public Player getPlayer() {
		return plugin.getServer().getPlayer(id);
	}
	
	@Override
	public Collection<Node> getTerminusNodes() {
		return terminus;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	public Node getViewing() {
		return exploring;
	}
	
	public UUID getUniqueId() {
		return id;
	}
	
	@Override
	public boolean isConversable(Node sender, String message, String type) {
		return true;
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	public boolean isViewing(Node node) {
		return (node == null && exploring == null) || exploring.equals(node);
	}
	
	@Override
	public void sendLine(String line) {
		if (!isOnline())
			return;
		
		getPlayer().sendMessage(line);
	}
	
	public void setViewing(Node viewing) {
		this.exploring = viewing;
	}
}