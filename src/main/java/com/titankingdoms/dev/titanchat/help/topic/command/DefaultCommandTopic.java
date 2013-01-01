package com.titankingdoms.dev.titanchat.help.topic.command;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.command.Command;

public final class DefaultCommandTopic extends CommandTopic {
	
	private final String briefDesc;
	private final String fullDesc;
	
	public DefaultCommandTopic(Command command) {
		super(command.getName());
		this.briefDesc = command.getBriefDescription();
		this.fullDesc = command.getFullDescription();
	}
	
	public boolean canView(CommandSender sender) {
		return true;
	}
	
	public String getBriefDescription() {
		return briefDesc;
	}
	
	public String getFullDescription() {
		return fullDesc;
	}
}