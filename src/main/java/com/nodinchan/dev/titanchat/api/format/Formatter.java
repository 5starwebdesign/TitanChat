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

package com.nodinchan.dev.titanchat.api.format;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.nodinchan.dev.module.AbstractModule;

public final class Formatter extends AbstractModule {
	
	private final Map<String, Variable> variables;
	
	public Formatter() {
		super("Formatter");
		this.variables = new HashMap<>();
	}
	
	public Variable get(String tag) {
		return (tag == null || tag.isEmpty()) ? null : variables.get("%" + tag.toLowerCase());
	}
	
	public boolean isHandled(String tag) {
		return tag != null && !tag.isEmpty() && variables.containsKey("%" + tag.toLowerCase());
	}
	
	@Override
	public void load() {}
	
	public void register(Variable variable) {
		Validate.notNull(variable, "Variable cannot be null");
		Validate.isTrue(!isHandled(variable.getTag()), "Variable already registered");
		
		variables.put("%" + variable.getTag().toLowerCase(), variable);
	}
	
	@Override
	public void reload() {}
	
	@Override
	public void unload() {}
	
	public void unregister(String tag) {
		Validate.notEmpty(tag, "Tag cannot be empty");
		Validate.isTrue(isHandled(tag), "Variable not registered");
		
		variables.remove("%" + tag.toLowerCase());
	}
}