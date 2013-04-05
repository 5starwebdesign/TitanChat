package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.vault.Vault;

/**
 * {@link WhitelistCommand} - Command for whitelisting in channels
 * 
 * @author NodinChan
 *
 */
public final class WhitelistCommand extends Command {
	
	public WhitelistCommand() {
		super("Whitelist");
		setArgumentRange(1, 1);
		setUsage("[player]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (channel == null) {
			sendMessage(sender, "&4Channel not defined");
			return;
		}
		
		if (!channel.getConfig().getBoolean("whitelist", false)) {
			sendMessage(sender, "&4Whitelist is not enabled for the channel");
			return;
		}
		
		Participant participant = plugin.getParticipantManager().getParticipant(args[0]);
		
		if (channel.getWhitelist().contains(participant.getName())) {
			sendMessage(sender, "&4" + participant.getDisplayName() + " is already whitelisted for the channel");
			return;
		}
		
		channel.getWhitelist().add(participant.getName());
		participant.sendMessage("&6You have been whitelisted for " + channel.getName());
		
		if (!channel.isParticipating(sender.getName()))
			sendMessage(sender, "&6" + participant.getDisplayName() + " has been whitelisted");
		
		broadcast(channel, "&6" + participant.getDisplayName() + " has been whitelisted");
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.getOperators().contains(sender.getName()))
			return true;
		
		return Vault.hasPermission(sender, "TitanChat.whitelist." + channel.getName());
	}
}