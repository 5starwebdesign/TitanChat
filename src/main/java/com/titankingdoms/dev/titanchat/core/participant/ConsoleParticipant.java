package com.titankingdoms.dev.titanchat.core.participant;

import org.bukkit.command.CommandSender;

public final class ConsoleParticipant extends Participant {
	
	public ConsoleParticipant() {
		super("CONSOLE");
	}
	
	@Override
	public CommandSender asCommandSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	@Override
	public Participant toParticipant() {
		return this;
	}
}