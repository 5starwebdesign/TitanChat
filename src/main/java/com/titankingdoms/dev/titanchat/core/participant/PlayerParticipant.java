package com.titankingdoms.dev.titanchat.core.participant;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PlayerParticipant extends Participant {
	
	public PlayerParticipant(Player player) {
		super(player.getName());
	}
	
	@Override
	public CommandSender asCommandSender() {
		return plugin.getServer().getPlayer(getName());
	}
	
	@Override
	public Participant toParticipant() {
		return this;
	}
}