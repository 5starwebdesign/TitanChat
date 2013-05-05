package com.titankingdoms.dev.titanchat.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.info.commands.CommandsIndex;
import com.titankingdoms.dev.titanchat.info.permissions.PermissionsIndex;
import com.titankingdoms.dev.titanchat.util.Debugger;

public final class InfoManager {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(6, "InfoManager");
	
	private final Map<String, Topic> topics;
	
	public InfoManager() {
		this.plugin = TitanChat.getInstance();
		this.topics = new TreeMap<String, Topic>();
	}
	
	public List<Topic> getTopics() {
		return new ArrayList<Topic>(topics.values());
	}
	
	public boolean hasTopic(String name) {
		return topics.containsKey(name);
	}
	
	public boolean hasTopic(Topic topic) {
		return hasTopic(topic.getName());
	}
	
	public void load() {
		registerTopics(
				new CommandsIndex().index(),
				new PermissionsIndex().index()
		);
	}
	
	public void reload() {
		for (Topic topic : getTopics())
			unregisterTopic(topic);
		
		registerTopics(
				new CommandsIndex().index(),
				new PermissionsIndex().index()
		);
	}
	
	public void registerTopics(Topic... topics) {
		if (topics == null)
			return;
		
		for (Topic topic : topics) {
			if (topic == null)
				continue;
			
			if (hasTopic(topic)) {
				plugin.log(Level.WARNING, "Duplicate topic: " + topic.getName());
				continue;
			}
			
			this.topics.put(topic.getName(), topic);
			db.debug(Level.INFO, "Registered topic: " + topic.getName());
		}
	}
	
	public void unload() {
		for (Topic topic : getTopics())
			unregisterTopic(topic);
	}
	
	public void unregisterTopic(Topic topic) {
		if (topic == null || !hasTopic(topic))
			return;
		
		this.topics.remove(topic.getName());
		db.debug(Level.INFO, "Unregistered topic: " + topic.getName());
	}
}