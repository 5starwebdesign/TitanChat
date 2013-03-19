package com.titankingdoms.dev.titanchat.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.loading.Loader;

public final class AddonManager {
	
	private final TitanChat plugin;
	
	private final Map<String, ChatAddon> addons;
	
	public AddonManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getAddonDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new TreeMap<String, ChatAddon>();
	}
	
	public ChatAddon getAddon(String name) {
		return addons.get(name);
	}
	
	public File getAddonDirectory() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	public List<ChatAddon> getAddons() {
		return new ArrayList<ChatAddon>(addons.values());
	}
	
	public boolean hasAddon(String name) {
		return addons.containsKey(name.toLowerCase());
	}
	
	public boolean hasAddon(ChatAddon addon) {
		return hasAddon(addon.getName());
	}
	
	public void load() {
		registerAddons(Loader.load(ChatAddon.class, getAddonDirectory()).toArray(new ChatAddon[0]));
		
		if (!addons.isEmpty())
			plugin.log(Level.INFO, "Addons loaded: " + StringUtils.join(addons.keySet(), ", "));
	}
	
	public void registerAddons(ChatAddon... addons) {
		if (addons == null)
			return;
		
		for (ChatAddon addon : addons) {
			if (addon == null)
				continue;
			
			if (hasAddon(addon)) {
				plugin.log(Level.WARNING, "Duplicate addon: " + addon.getName());
				continue;
			}
			
			this.addons.put(addon.getName().toLowerCase(), addon);
		}
	}
	
	public void reload() {
		addons.clear();
		
		registerAddons(Loader.load(ChatAddon.class, getAddonDirectory()).toArray(new ChatAddon[0]));
		
		if (!addons.isEmpty())
			plugin.log(Level.INFO, "Addons loaded: " + StringUtils.join(addons.keySet(), ", "));
	}
	
	public void unload() {
		addons.clear();
	}
}