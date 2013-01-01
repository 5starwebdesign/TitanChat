package com.titankingdoms.dev.titanchat.help.topic.command;

import com.titankingdoms.dev.titanchat.help.topic.HelpTopic;


public abstract class CommandTopic implements HelpTopic {
	
	private final String name;
	
	public CommandTopic(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
	
	public final boolean isCommandHelp() {
		return true;
	}
	
	public final boolean isIndex() {
		return false;
	}
}