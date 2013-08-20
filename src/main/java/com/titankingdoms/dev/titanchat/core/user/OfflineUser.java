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

package com.titankingdoms.dev.titanchat.core.user;

import org.bukkit.command.CommandSender;

public final class OfflineUser extends User {
	
	public OfflineUser(String name) {
		super(name);
	}
	
	@Override
	public CommandSender getCommandSender() {
		return null;
	}
	
	public User getOnlineUser() {
		UserManager manager = plugin.getManager(UserManager.class);
		
		if (!manager.has(getName()))
			return null;
		
		return manager.get(getName());
	}
	
	@Override
	public boolean isOnline() {
		return false;
	}
}