package com.titankingdoms.dev.titanchat.help;

import org.bukkit.command.CommandSender;

public abstract class CommandTopic implements Topic {
	
	private final String name;
	
	private Index index;
	
	public CommandTopic(String name) {
		this.name = name;
	}
	
	public Index getParent() {
		return index;
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
	
	public void setParent(Index index) {
		this.index = index;
	}
}