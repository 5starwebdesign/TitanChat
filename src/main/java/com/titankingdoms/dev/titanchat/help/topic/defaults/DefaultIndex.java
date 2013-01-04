package com.titankingdoms.dev.titanchat.help.topic.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.help.topic.Index;
import com.titankingdoms.dev.titanchat.help.topic.Topic;

public final class DefaultIndex extends Index {
	
	public DefaultIndex() {
		super("TitanChat Help Index");
	}
	
	public boolean canView(CommandSender sender) {
		return true;
	}
	
	public String getBriefDescription() {
		return "Help Index of TitanChat";
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