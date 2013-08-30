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

package com.titankingdoms.dev.titanchat.channel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.channel.Channel;

public abstract class Factory<T extends Channel> {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	public Factory(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(StringUtils.isAlphanumeric(name), "Name cannot contain non-alphanumeric chars");
		
		this.plugin = TitanChat.getInstance();
		this.name = name;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Factory)
			return toString().equals(object.toString());
		
		return false;
	}
	
	public final String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public abstract T loadChannel(String name, FileConfiguration config);
	
	public abstract void saveChannel(T channel);
	
	@Override
	public String toString() {
		return "ChannelFactory: {" +
				"name: " + getName() +
				"}";
	}
}