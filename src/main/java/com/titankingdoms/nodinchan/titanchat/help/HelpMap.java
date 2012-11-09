package com.titankingdoms.nodinchan.titanchat.help;

import java.util.Map;
import java.util.TreeMap;

public class HelpMap {
	
	private final Map<String, HelpTopic> topics;
	
	public HelpMap() {
		this.topics = new TreeMap<String, HelpTopic>();
	}
	
	public HelpTopic getHelpTopic(String topic) {
		return topics.get(topic.toLowerCase());
	}
	
	public void registerHelpTopics(HelpTopic... topics) {
		for (HelpTopic topic : topics) {
			if (this.topics.containsKey(topic.getName()))
				continue;
			
			this.topics.put(topic.getName().toLowerCase(), topic);
		}
	}
}