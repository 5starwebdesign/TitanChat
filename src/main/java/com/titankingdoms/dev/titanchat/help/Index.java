package com.titankingdoms.dev.titanchat.help;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.util.C;

public abstract class Index implements Topic {
	
	private final String name;
	
	private final List<Topic> topics;
	
	public Index(String name) {
		this(name, new ArrayList<Topic>());
	}
	
	public Index(String name, ArrayList<Topic> topics) {
		this.name = name;
		this.topics = topics;
	}
	
	public void addTopic(Topic topic) {
		this.topics.add(topic);
	}
	
	public String getFullDescription() {
		StringBuilder str = new StringBuilder();
		
		for (Topic topic : getTopics()) {
			if (str.length() > 0)
				str.append("\n");
			
			String line = topic.getName() + " - " + topic.getBriefDescription();
			
			if (line.length() >= 119)
				line = line.substring(0, 116) + "...";
			
			str.append(line);
		}
		
		return str.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public String getParagraph(CommandSender sender) {
		StringBuilder str = new StringBuilder();
		
		for (Topic topic : getTopics()) {
			if (!topic.canView(sender))
				continue;
			
			String line = C.GOLD + topic.getName() + C.WHITE + " - " + C.GREY + topic.getBriefDescription();
			
			if (line.length() >= 119)
				line = line.substring(0, 116) + "...";
			
			str.append(line);
		}
		
		return str.toString();
	}
	
	public List<Topic> getTopics() {
		return new LinkedList<Topic>(topics);
	}
	
	public final boolean isIndex() {
		return true;
	}
	
	public void removeTopic(Topic topic) {
		this.topics.remove(topic);
	}
}