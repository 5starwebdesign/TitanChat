package com.titankingdoms.dev.titanchat.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.channel.Channel;

public class Permissions {
	
	public static void load() {
		TitanChat plugin = TitanChat.getInstance();
		
		if (plugin == null)
			return;
		
		YamlConfiguration ps = YamlConfiguration.loadConfiguration(plugin.getResource("permissions.yml"));
		
		for (String name : ps.getKeys(false)) {
			String node = "TitanChat." + name.replace("_", ".").replace("<channel>", "*");
			
			if (plugin.getServer().getPluginManager().getPermission(node) != null)
				continue;
			
			ConfigurationSection pSection = ps.getConfigurationSection(name);
			
			String description = pSection.getString("description", "");
			PermissionDefault def = PermissionDefault.getByName(pSection.getString("default", "op"));
			Map<String, Boolean> children = new HashMap<String, Boolean>();
			
			if (pSection.contains("children")) {
				for (String child : pSection.getConfigurationSection("children").getKeys(false)) {
					String childNode = "TitanChat." + child.replace("_", ".").replace("<channel>", "*");
					children.put(childNode, pSection.getBoolean("children." + child, true));
				}
			}
			
			Permission permission = new Permission(node, description, def, children);
			plugin.getServer().getPluginManager().addPermission(permission);
		}
	}
	
	public static void load(Channel channel) {
		TitanChat plugin = TitanChat.getInstance();
		
		if (plugin == null)
			return;
		
		String chName = channel.getName();
		
		YamlConfiguration ps = YamlConfiguration.loadConfiguration(plugin.getResource("permissions.yml"));
		
		for (String name : ps.getKeys(false)) {
			if (!name.contains("<channel>"))
				continue;
			
			String node = "TitanChat." + name.replace("_", ".").replace("<channel>", chName);
			
			if (plugin.getServer().getPluginManager().getPermission(node) != null)
				continue;
			
			ConfigurationSection pSection = ps.getConfigurationSection(name);
			
			String description = pSection.getString("description", "");
			PermissionDefault def = PermissionDefault.getByName(pSection.getString("default", "op"));
			Map<String, Boolean> children = new HashMap<String, Boolean>();
			
			if (pSection.contains("children")) {
				for (String child : pSection.getConfigurationSection("children").getKeys(false)) {
					String childNode = "TitanChat." + child.replace("_", ".").replace("<channel>", chName);
					children.put(childNode, pSection.getBoolean("children." + child, true));
				}
			}
			
			Permission permission = new Permission(node, description, def, children);
			permission.addParent("TitanChat." + name.replace("_", ".").replace("<channel>", "*"), true);
			plugin.getServer().getPluginManager().addPermission(permission);
		}
	}
	
	public static void unload(Channel channel) {
		TitanChat plugin = TitanChat.getInstance();
		
		if (plugin == null)
			return;
		
		String chName = channel.getName();
		
		YamlConfiguration ps = YamlConfiguration.loadConfiguration(plugin.getResource("permissions.yml"));
		
		for (String name : ps.getKeys(false)) {
			if (!name.contains("<channel>"))
				continue;
			
			String node = "TitanChat." + name.replace("_", ".").replace("<channel>", chName);
			
			if (plugin.getServer().getPluginManager().getPermission(node) == null)
				continue;
			
			plugin.getServer().getPluginManager().removePermission(node);
		}
	}
}