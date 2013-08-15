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

package com.titankingdoms.dev.titanchat.core;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.user.User;
import com.titankingdoms.dev.titanchat.core.user.UserManager;
import com.titankingdoms.dev.titanchat.event.ConverseEvent;

public final class MinecraftChat implements EndPoint {
	
	private final TitanChat plugin;
	
	public MinecraftChat() {
		this.plugin = TitanChat.getInstance();
	}
	
	@Override
	public String getName() {
		return "MinecraftChat";
	}
	
	@Override
	public Set<EndPoint> getRelayPoints() {
		return new HashSet<EndPoint>(plugin.getManager(UserManager.class).getAll());
	}
	
	@Override
	public String getType() {
		return "MinecraftChat";
	}
	
	@Override
	public boolean onMessageReceive(ConverseEvent event) {
		return true;
	}
	
	@Override
	public boolean onMessageSend(ConverseEvent event) {
		return true;
	}
	
	@Override
	public void sendNotice(String... messages) {
		for (User user : plugin.getManager(UserManager.class).getAll())
			user.sendNotice(messages);
	}
	
	@Override
	public void sendRawLine(String line) {
		for (User user : plugin.getManager(UserManager.class).getAll())
			user.sendRawLine(line);
	}
}