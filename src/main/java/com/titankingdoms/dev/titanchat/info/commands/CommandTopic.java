package com.titankingdoms.dev.titanchat.info.commands;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.info.Topic;

public final class CommandTopic extends Topic {
	
	public CommandTopic(Command command) {
		super(command.getName(), command.getDescription());
	}
}