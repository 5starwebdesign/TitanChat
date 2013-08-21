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

package com.titankingdoms.dev.titanchat.core.channel;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.EndPoint;

public abstract class Channel implements EndPoint {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private final Set<String> blacklist;
	private final Set<String> whitelist;
	
	public Channel(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(StringUtils.isAlphanumeric(name), "Name cannot contain non-alphanumeric characters");
		
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.blacklist = new HashSet<String>();
		this.whitelist = new HashSet<String>();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Channel)
			return toString().equals(object.toString());
		
		return false;
	}
	
	public Set<String> getBlacklist() {
		return blacklist;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final String getType() {
		return "Channel";
	}
	
	public Set<String> getWhitelist() {
		return whitelist;
	}
	
	@Override
	public void sendNotice(String... messages) {}
	
	@Override
	public String toString() {
		return "Channel: {" +
				"name: " + getName() +
				"}";
	}
}