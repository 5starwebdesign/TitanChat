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

package com.titankingdoms.dev.titanchat.api.conversation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.api.Manager;

public final class ProvisionManager implements Manager<Provider<Node>> {
	
	private final Map<String, Provider<Node>> providers;
	
	private final Set<String> dependencies = Collections.unmodifiableSet(new HashSet<String>());
	
	public ProvisionManager() {
		this.providers = new HashMap<String, Provider<Node>>();
	}
	
	@Override
	public Provider<Node> get(String name) {
		return providers.get(name);
	}
	
	@Override
	public Set<Provider<Node>> getAll() {
		return new HashSet<Provider<Node>>(providers.values());
	}
	
	@Override
	public Set<String> getDependencies() {
		return dependencies;
	}
	
	@Override
	public String getName() {
		return "ProvisionManager";
	}
	
	public <T extends Node> Provider<T> getProvider(Class<Provider<T>> type, String name) {
		return (has(name)) ? type.cast(get(name)) : null;
	}
	
	@Override
	public boolean has(String name) {
		return name != null && !name.isEmpty() && providers.containsKey(name.toLowerCase());
	}
	
	@Override
	public boolean has(Provider<Node> provider) {
		return provider != null && has(provider.getName()) && get(provider.getName()).equals(provider);
	}
	
	@Override
	public void load() {}
	
	@Override
	public Collection<String> match(String name) {
		return null;
	}
	
	@Override
	public void register(Provider<Node> provider) {
		Validate.notNull(provider, "Provider cannot be null");
		Validate.isTrue(!has(provider.getName()), "Provider already registered");
		
		providers.put(provider.getName().toLowerCase(), provider);
	}
	
	@Override
	public void reload() {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Provider<Node> provider) {
		Validate.notNull(provider, "Provider cannot be null");
		Validate.isTrue(has(provider.getName()), "Provider not registered");
		
		providers.remove(provider.getName().toLowerCase());
	}
}