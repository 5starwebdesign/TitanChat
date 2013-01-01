package com.titankingdoms.dev.titanchat.help.topic.index;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.help.HelpMap;
import com.titankingdoms.dev.titanchat.help.topic.HelpTopic;

public final class YamlIndexTopic extends IndexTopic {
	
	private final String description;
	private final String permission;
	
	private final Map<String, HelpTopic> topics;
	
	public YamlIndexTopic(ConfigurationSection section) {
		super(section.getName());
		this.description = section.getString("description", "");
		this.permission = section.getString("permission", "");
		this.topics = new TreeMap<String, HelpTopic>();
		
		HelpMap helpMap = TitanChat.getInstance().getHelpMap();
		
		for (String topic : section.getStringList("topics"))
			if (helpMap.hasHelpTopic(topic))
				topics.put(helpMap.getHelpTopic(topic).getName(), helpMap.getHelpTopic(topic));
	}
	
	public boolean canView(CommandSender sender) {
		return (!permission.isEmpty()) ? sender.hasPermission(permission) : true;
	}
	
	public String getBriefDescription() {
		return description;
	}
	
	public String getFullDescription() {
		StringBuilder str = new StringBuilder();
		
		if (!topics.isEmpty())
			str.append("Topics:\n");
		
		for (HelpTopic topic : topics.values()) {
			String line = topic.getName() + " - " + topic.getBriefDescription();
			
			if (line.length() == 119)
				line = line.substring(0, 119);
			else if (line.length() > 119)
				line = line.substring(0, 116) + "...";
			
			str.append(line + "\n");
		}
		
		return str.toString();
	}
}