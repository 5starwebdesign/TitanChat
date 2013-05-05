package com.titankingdoms.dev.titanchat.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class Index extends Topic {
	
	private final Map<String, Topic> topics;
	
	public Index(String name) {
		this(name, "The " + name + " Index", new TreeMap<String, Topic>());
	}
	
	public Index(String name, String description) {
		this(name, description, new TreeMap<String, Topic>());
	}
	
	public Index(String name, Map<String, Topic> topics) {
		this(name, "The " + name + " Index", topics);
	}
	
	public Index(String name, String description, Map<String, Topic> topics) {
		super(name, description);
		this.topics = topics;
	}
	
	public final void addTopic(Topic topic) {
		this.topics.put(topic.getName(), topic);
	}
	
	public final List<Topic> getTopics() {
		return new ArrayList<Topic>(topics.values());
	}
	
	public final void removeTopic(Topic topic) {
		this.topics.remove(topic.getName());
	}
}