package com.titankingdoms.dev.titanchat.help;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.command.Command;

public final class CommandTopic implements HelpTopic {
	
	private final String name;
	private final String briefDesc;
	private final String fullDesc;
	
	public CommandTopic(Command command) {
		this.name = command.getName();
		this.briefDesc = "About the " + name + " command";
		this.fullDesc = command.getDescription();
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
	
	public String getName() {
		return name;
	}
}