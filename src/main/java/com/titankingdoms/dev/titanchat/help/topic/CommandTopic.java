package com.titankingdoms.dev.titanchat.help.topic;

import org.bukkit.command.CommandSender;



public abstract class CommandTopic implements Topic {
	
	private final String name;
	
	public CommandTopic(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
	
	public String getParagraph(CommandSender sender) {
		return getFullDescription();
	}
	
	public final boolean isCommandHelp() {
		return true;
	}
	
	public final boolean isIndex() {
		return false;
	}
}