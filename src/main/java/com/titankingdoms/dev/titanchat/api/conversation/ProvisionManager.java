package com.titankingdoms.dev.titanchat.api.conversation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	public Collection<Provider<Node>> getAll() {
		return null;
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
	public void register(Provider<Node> provider) {}
	
	@Override
	public void reload() {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Provider<Node> provider) {}
}