package com.titankingdoms.dev.titanchat.help.topic.yaml;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.help.topic.Index;
import com.titankingdoms.dev.titanchat.help.topic.Topic;
import com.titankingdoms.dev.titanchat.vault.Vault;

public final class YamlIndex extends Index {
	
	private final String description;
	private final String permission;
	
	public YamlIndex(ConfigurationSection section) {
		super(section.getName());
		this.description = section.getString("description", "");
		this.permission = section.getString("permission", "");
		
		if (section.get("topics") != null)
			for (String topic : section.getStringList("topics"))
				addTopic(topic);
	}
	
	public boolean canView(CommandSender sender) {
		return (!permission.isEmpty()) ? Vault.hasPermission(sender, permission) : true;
	}
	
	public String getBriefDescription() {
		return description;
	}
	
	public String getFullDescription() {
		StringBuilder str = new StringBuilder();
		
		for (String name : getTopics()) {
			if (!help.hasHelpTopic(name))
				continue;
			
			Topic topic = help.getHelpTopic(name);
			
			String line = topic.getName() + " - " + topic.getBriefDescription();
			
			if (line.length() >= 119)
				line = line.substring(0, 116) + "...";
			
			str.append(line);
		}
		
		return str.toString();
	}
}