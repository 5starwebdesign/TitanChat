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

package com.titankingdoms.dev.titanchat.api.user.storage;

import org.apache.commons.lang.Validate;

public final class NodeCache {
	
	private final String name;
	private final String type;
	
	public NodeCache(String name, String type) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.notEmpty(type, "Type cannot be empty");
		
		this.name = name;
		this.type = type;
	}
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof NodeCache) ? toString().equals(object.toString()) : false;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "\"NodeCache\": {" +
				"\"name\": \"" + getName() + "\", " +
				"\"type\": \"" + getType() + "\"" +
				"}";
	}
}