package com.titankingdoms.dev.titanchat.command.commands;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;

public final class ConverseCommand extends Command {
	
	public ConverseCommand(String name) {
		super("Converse");
		setAliases("");
		setArgumentRange(1, 1024);
		setDescription("");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub

	}
}