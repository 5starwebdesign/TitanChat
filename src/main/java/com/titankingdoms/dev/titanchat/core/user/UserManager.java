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

import java.util.List;

import com.titankingdoms.dev.titanchat.Manager;

public final class UserManager implements Manager<User> {
	
	public UserManager() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public User get(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean has(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean has(User item) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void load() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void registerAll(User... items) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void unregister(User item) {
		// TODO Auto-generated method stub

	}
}