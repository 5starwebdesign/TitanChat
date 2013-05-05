package com.titankingdoms.dev.titanchat.info.permissions;

import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.info.Index;

public final class PermissionsIndex extends Index {
	
	public PermissionsIndex() {
		super("Permissions");
	}
	
	public PermissionsIndex index() {
		YamlConfiguration ps = YamlConfiguration.loadConfiguration(plugin.getResource("permissions.yml"));
		
		for (String name : ps.getKeys(false)) {
			String node = "TitanChat." + name.replace("_", ".");
			String description = ps.getString(name + ".description");
			addTopic(new PermissionTopic(node, description));
		}
		
		return this;
	}
}