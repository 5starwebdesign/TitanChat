package com.titankingdoms.dev.titanchat.help.topic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.help.Help;

public abstract class Index implements Topic {
	
	protected final Help help;
	
	private final String name;
	
	private final List<String> topics;
	
	public Index(String name) {
		this.help = TitanChat.getInstance().getHelp();
		this.name = name;
		this.topics = new ArrayList<String>();
	}
	
	public void addTopic(String topic) {
		this.topics.add(topic);
	}
	
	public String getName() {
		return name;
	}
	
	public String getParagraph(CommandSender sender) {
		StringBuilder str = new StringBuilder();
		
		for (String name : getTopics()) {
			if (!help.hasHelpTopic(name))
				continue;
			
			Topic topic = help.getHelpTopic(name);
			
			if (!topic.canView(sender))
				continue;
			
			String line = topic.getName() + " - " + topic.getBriefDescription();
			
			if (line.length() >= 119)
				line = line.substring(0, 116) + "...";
			
			str.append(line);
		}
		
		return str.toString();
	}
	
	public List<String> getTopics() {
		return new LinkedList<String>(topics);
	}
	
	public final boolean isIndex() {
		return true;
	}
	
	public void removeTopic(String topic) {
		this.topics.remove(topic);
	}
}