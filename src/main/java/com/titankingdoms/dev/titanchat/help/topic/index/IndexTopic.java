package com.titankingdoms.dev.titanchat.help.topic.index;

import com.titankingdoms.dev.titanchat.help.topic.HelpTopic;

public abstract class IndexTopic implements HelpTopic {
	
	private final String name;
	
	public IndexTopic(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public final boolean isIndex() {
		return true;
	}
}