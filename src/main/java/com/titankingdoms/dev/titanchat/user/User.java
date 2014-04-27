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

package com.titankingdoms.dev.titanchat.user;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.metadata.Metadata;

public final class User implements Node {
	
	private final TitanChat plugin;
	
	private static final String TYPE = "User";
	
	private final UUID id;
	
	private final String name;
	
	private Metadata metadata;
	
	private final UserConnection connection = new UserConnection(this);
	
	private volatile Node exploring;
	
	private final Set<Node> terminus = ImmutableSet.<Node>builder().add(this).build();
	
	public User(OfflinePlayer player) {
		Validate.notNull(player, "Player cannot be null");
		
		this.plugin = TitanChat.instance();
		this.id = player.getUniqueId();
		this.name = (player.hasPlayedBefore()) ? player.getName() : "Unknown";
	}
	
	@Override
	public UserConnection getConnection() {
		return connection;
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