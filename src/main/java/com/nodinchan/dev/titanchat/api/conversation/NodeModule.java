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

package com.nodinchan.dev.titanchat.api.conversation;

import java.util.HashMap;
import java.util.Map;

import com.nodinchan.dev.module.Module;

public final class NodeModule implements Module {
	
	private static final String NAME = "Node";
	
	private final Map<String, NodeFactory<? extends Node>> factories;
	
	private boolean loaded;
	
	public NodeModule() {
		this.factories = new HashMap<>();
		this.loaded = false;
	}
	
	public NodeFactory<? extends Node> getFactory(String type) {
		return factories.get(type.toLowerCase());
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public Node getNode(String type, String name) {
		return (!hasFactory(type)) ? null : getFactory(type).getNode(name);
	}
	
	public boolean hasFactory(String type) {
		return factories.containsKey(type.toLowerCase());
	}
	
	public boolean hasNode(String type, String name) {
		return hasFactory(type) && getFactory(type).hasNode(name);
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	@Override
	public void load() {}
	
	@Override
	public boolean setLoaded(boolean loaded) {
		if (this.loaded == loaded)
			return false;
		
		this.loaded = loaded;
		
		if (loaded)
			load();
		else
			unload();
		
		return true;
	}
	
	@Override
	public void unload() {}
}