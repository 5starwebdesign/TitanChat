package com.titankingdoms.dev.titanchat.api.conversation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.api.Manager;

public final class ProvisionManager implements Manager<Provider<Node>> {
	
	private final Map<String, Provider<Node>> providers;
	
	public ProvisionManager() {
		this.providers = new HashMap<String, Provider<Node>>();
	}
	
	@Override
	public Provider<Node> get(String name) {
		return (has(name)) ? providers.get(name) : null;
	}
	
	@Override
	public Set<Provider<Node>> getAll() {
		return new HashSet<Provider<Node>>(providers.values());
	}
	
	public <T extends Node> Provider<T> getProvider(Class<Provider<T>> type, String name) {
		return (has(name)) ? type.cast(get(name)) : null;
	}
	
	@Override
	public String getName() {
		return "ProvisionManager";
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
		
		this.providers.put(provider.getName().toLowerCase(), provider);
	}
	
	@Override
	public void reload() {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Provider<Node> provider) {
		Validate.notNull(provider, "Provider cannot be null");
		Validate.isTrue(has(provider.getName()), "Provider not registered");
		
		this.providers.remove(provider.getName().toLowerCase());
	}
}