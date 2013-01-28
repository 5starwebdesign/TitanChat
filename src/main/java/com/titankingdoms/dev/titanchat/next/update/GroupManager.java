package com.titankingdoms.dev.titanchat.next.update;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.next.update.EntityManager.ChatEntitySearcher;

public final class GroupManager {
	
	private final TitanChat plugin;
	
	private File configFile;
	private FileConfiguration config;
	
	private final Map<String, ChatGroup> groups;
	
	public GroupManager() {
		this.plugin = TitanChat.getInstance();
		this.groups = new HashMap<String, ChatGroup>();
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public ChatGroup getGroup(String name) {
		return groups.get(name.toLowerCase());
	}
	
	public Set<ChatGroup> getGroups() {
		return new HashSet<ChatGroup>(groups.values());
	}
	
	public boolean hasGroup(String name) {
		return groups.containsKey(name.toLowerCase());
	}
	
	public void load() {
		ConfigurationSection groupsSection = getConfig().getConfigurationSection("groups");
		
		if (groupsSection != null) {
			for (String groupName : groupsSection.getKeys(false))
				this.groups.put(groupName.toLowerCase(), new ChatGroup(groupName));
			
			for (ChatGroup group : getGroups()) {
				ConfigurationSection groupSection = groupsSection.getConfigurationSection(group.getName());
				
				Permission groupPermission = new Permission("TitanChat.group." + group.getName());
				plugin.getServer().getPluginManager().addPermission(groupPermission);
				group.setPermission(groupPermission, true);
				
				if (groupSection.get("permissions") != null) {
					ConfigurationSection permissions = groupSection.getConfigurationSection("permissions");
					
					for (String node : permissions.getKeys(false)) {
						Permission permission = new Permission(node);
						permission.addParent(groupPermission, permissions.getBoolean(node, true));
						plugin.getServer().getPluginManager().addPermission(permission);
					}
				}
			}
		}
		
		EntityManager.registerSearcher(new ChatEntitySearcher("ChatGroup") {
			
			@Override
			public ChatEntity getEntity(String name) {
				return getGroup(name);
			}
		});
	}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "groups.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("groups.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
}