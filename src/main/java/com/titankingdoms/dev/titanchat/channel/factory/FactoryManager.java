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

package com.titankingdoms.dev.titanchat.channel.factory;

import java.util.*;
import java.util.logging.Level;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;

public final class FactoryManager implements Manager<Factory> {
	
	private final TitanChat plugin;
	
	private final Map<String, Factory> factories;
	
	public FactoryManager() {
		this.plugin = TitanChat.getInstance();
		this.factories = new HashMap<String, Factory>();
	}
	
	@Override
	public Factory get(String name) {
		return (has(name)) ? factories.get(name.toLowerCase()) : null;
	}
	
	@Override
	public List<Factory> getAll() {
		return new ArrayList<Factory>(factories.values());
	}
	
	@Override
	public String getName() {
		return "FactoryManager";
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? factories.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(Factory factory) {
		if (factory == null || !has(factory.getName()))
			return false;
		
		return get(factory.getName()).equals(factory);
	}
	
	@Override
	public void load() {}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return new ArrayList<String>(factories.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String factory : factories.keySet()) {
			if (!factory.startsWith(name))
				continue;
			
			matches.add(factory);
		}
		
		Collections.sort(matches);
		
		return matches;
	}
	
	@Override
	public void registerAll(Factory... factories) {
		if (factories == null)
			return;
		
		for (Factory factory : factories) {
			if (factory == null)
				continue;
			
			if (has(factory)) {
				plugin.log(Level.WARNING, "Duplicate: " + factory);
				continue;
			}
			
			this.factories.put(factory.getName().toLowerCase(), factory);
		}
	}
	
	@Override
	public void reload() {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Factory factory) {
		if (factory == null || !has(factory))
			return;
		
		this.factories.remove(factory.getName().toLowerCase());
	}
}