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

import com.nodinchan.dev.titanchat.api.conversation.NodeFactory;

public final class UserFactory implements NodeFactory<User> {
	
	protected static final String TYPE = "User";
	
	@Override
	public User getNode(String name) {
		return null;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public boolean hasNode(String name) {
		return false;
	}
	
	@Override
	public void registerNode(User node) {}
	
	@Override
	public void unregisterNode(String name) {}
}