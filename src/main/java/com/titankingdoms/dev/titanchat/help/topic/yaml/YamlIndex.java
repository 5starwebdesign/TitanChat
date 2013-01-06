package com.titankingdoms.dev.titanchat.help.topic.yaml;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.help.Help;
import com.titankingdoms.dev.titanchat.help.Index;
import com.titankingdoms.dev.titanchat.vault.Vault;

public final class YamlIndex extends Index {
	
	private final Help help;
	private final String description;
	private final String permission;
	private final List<String> topics;
	
	public YamlIndex(Help help, ConfigurationSection section) {
		super(section.getName());
		this.help = help;
		this.description = section.getString("description", "");
		this.permission = section.getString("permission", "");
		this.topics = (section.get("topics") != null) ? section.getStringList("topics") : new ArrayList<String>();
	}
	
	public boolean canView(CommandSender sender) {
		return (!permission.isEmpty()) ? Vault.hasPermission(sender, permission) : true;
	}
	
	public String getBriefDescription() {
		return description;
	}
	
	public String getFullDescription() {
		if (!topics.isEmpty()) {
			for (String name : topics) {
				if (!help.hasHelpTopic(name))
					continue;
				
				addTopic(help.getHelpTopic(name));
			}
			
			topics.clear();
		}
		
		return super.getFullDescription();
	}
	
	public String getParagraph(CommandSender sender) {
		if (!topics.isEmpty()) {
			for (String name : topics) {
				if (!help.hasHelpTopic(name))
					continue;
				
				addTopic(help.getHelpTopic(name));
			}
			
			topics.clear();
		}
		
		return super.getParagraph(sender);
	}
}