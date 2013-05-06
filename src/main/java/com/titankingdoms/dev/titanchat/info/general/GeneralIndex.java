package com.titankingdoms.dev.titanchat.info.general;

import java.util.List;

import com.titankingdoms.dev.titanchat.info.Index;
import com.titankingdoms.dev.titanchat.info.Topic;

/**
 * {@link GeneralIndex} - The general index for all topics
 * 
 * @author NodinChan
 *
 */
public final class GeneralIndex extends Index {
	
	public GeneralIndex() {
		super("General", "The General Index");
	}
	
	@Override
	public void addTopic(Topic topic) {
		plugin.getTopicManager().registerTopics(topic);
	}
	
	@Override
	public Topic getTopic(String name) {
		return plugin.getTopicManager().getTopic(name);
	}
	
	@Override
	public List<Topic> getTopics() {
		return plugin.getTopicManager().getTopics();
	}
	
	@Override
	public void removeTopic(Topic topic) {
		plugin.getTopicManager().unregisterTopic(topic);
	}
}