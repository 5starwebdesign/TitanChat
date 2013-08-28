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

package com.titankingdoms.dev.titanchat;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.dev.titanchat.api.EndPoint;
import com.titankingdoms.dev.titanchat.user.User;
import com.titankingdoms.dev.titanchat.user.UserManager;

public final class Minecraft implements EndPoint {
	
	private final UserManager manager;
	
	public Minecraft() {
		this.manager = TitanChat.getInstance().getManager(UserManager.class);
	}
	
	@Override
	public String getName() {
		return "Minecraft";
	}
	
	@Override
	public Set<EndPoint> getRelayPoints() {
		return new HashSet<EndPoint>(manager.getAll());
	}
	
	@Override
	public String getType() {
		return "Minecraft";
	}
	
	@Override
	public void sendRawLine(String line) {
		for (User user : manager.getAll())
			user.sendRawLine(line);
	}
}