package com.titankingdoms.dev.titanchat.help.topic.yaml;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.help.Topic;
import com.titankingdoms.dev.titanchat.vault.Vault;

public final class YamlGeneralTopic implements Topic {
	
	private final String name;
	private final String briefDesc;
	private final String fullDesc;
	private final String permission;
	
	public YamlGeneralTopic(ConfigurationSection section) {
		this.name = section.getName();
		this.briefDesc = section.getString("brief-desc", "");
		this.fullDesc = section.getString("full-desc", "");
		this.permission = section.getString("permission", "");
	}
	
	public boolean canView(CommandSender sender) {
		return (!permission.isEmpty()) ? Vault.hasPermission(sender, permission) : true;
	}
	
	public String getBriefDescription() {
		return briefDesc;
	}
	
	public String getFullDescription() {
		return fullDesc;
	}
	
	public String getName() {
		return name;
	}
	
	public String getParagraph(CommandSender sender) {
		return getFullDescription();
	}
	
	public boolean isIndex() {
		return false;
	}
}