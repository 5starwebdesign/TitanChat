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

package com.titankingdoms.dev.titanchat.api.format.var;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.api.event.ConverseEvent;

public abstract class Var {
	
	private final String name;
	
	public Var(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		
		this.name = name;
	}
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof Var) ? toString().equals(object.toString()) : false;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String getTag() {
		return "%" + name;
	}
	
	public abstract String getValue(ConverseEvent event);
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "\"Var\": {" +
				"\"name\": \"" + getName() + "\", " +
				"\"tag\": \"" + getTag() + "\"" +
				"}";
	}
}