package com.titankingdoms.dev.titanchat.core.conversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.titankingdoms.dev.titanchat.api.Manager;

public final class ProvisionManager implements Manager<Provider> {
	
	private final Map<String, Provider> providers;
	
	public ProvisionManager() {
		this.providers = new HashMap<String, Provider>();
	}
	
	@Override
	public Provider get(String name) {
		return (has(name)) ? providers.get(name.toLowerCase()) : null;
	}
	
	@Override
	public Collection<Provider> getAll() {
		return new HashSet<Provider>(providers.values());
	}
	
	@Override
	public String getName() {
		return "ProvisionManager";
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? providers.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(Provider provider) {
		return (provider != null && has(provider.getType())) ? get(provider.getType()).equals(provider) : false;
	}
	
	@Override
	public void init() {}
	
	@Override
	public void load() {}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return new ArrayList<String>(providers.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String provider : providers.keySet()) {
			if (!provider.startsWith(name.toLowerCase()))
				continue;
			
			matches.add(provider);
		}
		
		Collections.sort(matches);
		
		return matches;
	}
	
	@Override
	public void registerAll(Provider... providers) {
		if (providers == null)
			return;
		
		for (Provider provider : providers) {
			if (provider == null || has(provider))
				continue;
			
			this.providers.put(provider.getType().toLowerCase(), provider);
		}
	}
	
	@Override
	public void reload() {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Provider provider) {
		if (provider == null || !has(provider))
			return;
		
		this.providers.remove(provider.getType().toLowerCase());
	}
}