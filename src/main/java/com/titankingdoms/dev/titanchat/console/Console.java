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

package com.titankingdoms.dev.titanchat.console;

import java.util.Collection;
import java.util.Set;

import org.bukkit.command.ConsoleCommandSender;

import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.conversation.NodeManager;

public final class Console implements Node, NodeManager<Console> {
	
	protected final TitanChat plugin;
	
	private static final Console instance = new Console();
	
	private static final String NAME = "Console";
	private static final String TYPE = "Console";
	
	private final ConsoleConnection connection;
	
	private volatile Node exploring;
	
	private static final Set<Console> console = ImmutableSet.<Console>builder().add(instance).build();
	private static final Set<Node> terminus = ImmutableSet.<Node>builder().add(instance).build();
	
	private Console() {
		this.plugin = TitanChat.instance();
		this.connection = new ConsoleConnection(this);
	}
	
	@Override
	public Console get(String name) {
		return instance;
	}
	
	@Override
	public Collection<Console> getAll() {
		return console;
	}
	
	@Override
	public ConsoleConnection getConnection() {
		return connection;
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
	
	public Node getViewing() {
		return exploring;
	}
	
	@Override
	public boolean has(String name) {
		return name != null && !name.isEmpty();
	}
	
	public static Console instance() {
		return instance;
	}
	
	public boolean isViewing(Node node) {
		return (node == null && exploring == null) || exploring.equals(node);
	}
	
	@Override
	public void register(Console console) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void sendLine(String line) {
		getConsoleSender().sendMessage(line);
	}
	
	public void setViewing(Node viewing) {
		this.exploring = viewing;
	}
	
	@Override
	public void unregister(Console console) {
		throw new UnsupportedOperationException();
	}
}