package com.titankingdoms.dev.titanchat.info.commands;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.info.Index;

public final class CommandsIndex extends Index {
	
	public CommandsIndex() {
		super("Commands");
	}
	
	public CommandsIndex index() {
		for (Command command : plugin.getCommandManager().getCommands())
			addTopic(new CommandTopic(command));
		
		return this;
	}
}