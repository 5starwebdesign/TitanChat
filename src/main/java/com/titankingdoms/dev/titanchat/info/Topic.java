package com.titankingdoms.dev.titanchat.info;

import com.titankingdoms.dev.titanchat.TitanChat;

public abstract class Topic {
	
	protected final TitanChat plugin;
	
	private final String name;
	private final String description;
	
	private String information;
	
	public Topic(String name, String description) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.description = description;
		this.information = "";
	}
	
	public final String getDescription() {
		return description;
	}
	
	public String getInformation() {
		return (information.isEmpty()) ? description : information;
	}
	
	public final String getName() {
		return name;
	}
	
	public void setInformation(String information) {
		this.information = (information != null) ? information : "";
	}
}