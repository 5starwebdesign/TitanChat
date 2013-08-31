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

package com.titankingdoms.dev.titanchat.legacy;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.EndPoint;
import com.titankingdoms.dev.titanchat.user.*;

public final class LegacyChat implements EndPoint {
	
	private final TitanChat plugin;
	
	private final UserManager manager;
	
	private final String DEFAULT_FORMAT = "%prefix%display%suffix: %message";
	
	public LegacyChat() {
		this.plugin = TitanChat.getInstance();
		this.manager = plugin.getManager(UserManager.class);
	}
	
	@Override
	public String getFormat() {
		return plugin.getConfig().getString("format.converse", DEFAULT_FORMAT);
	}
	
	@Override
	public String getName() {
		return "Minecraft";
	}
	
	@Override
	public Set<EndPoint> getRelayPoints(EndPoint sender) {
		return new HashSet<EndPoint>(manager.getAll());
	}
	
	@Override
	public String getType() {
		return "Minecraft";
	}
	
	@Override
	public void linkPoint(EndPoint point) {}
	
	@Override
	public void sendRawLine(String line) {
		for (User user : manager.getAll())
			user.sendRawLine(line);
	}
	
	@Override
	public void unlinkPoint(EndPoint point) {}
}