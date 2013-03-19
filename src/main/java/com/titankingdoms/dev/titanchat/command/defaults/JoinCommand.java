package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.vault.Vault;

public final class JoinCommand extends Command {
	
	public JoinCommand() {
		super("Join");
		setAliases("j");
		setArgumentRange(1, 2);
		setUsage("[channel] <password>");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().hasAlias(args[0])) {
			sendMessage(sender, "§4Channel does not exist");
			return;
		}
		
		channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (channel.isParticipating(sender.getName())) {
			sendMessage(sender, "§4You have already joined the channel");
			return;
		}
		
		if (channel.getBlacklist().contains(sender.getName())) {
			sendMessage(sender, "§4You are blacklisted from the channel");
			return;
		}
		
		if (channel.getData("whitelist", "false").asBoolean()) {
			if (!channel.getWhitelist().contains(sender.getName())) {
				sendMessage(sender, "§4You are not whitelisted for the channel");
				return;
			}
		}
		
		if (channel.getPassword() != null && !channel.getPassword().isEmpty()) {
			if (args.length < 2) {
				sendMessage(sender, "§4Please enter a password");
				return;
			}
			
			if (!channel.getPassword().equals(args[1])) {
				sendMessage(sender, "§4Incorrect password");
				return;
			}
		}
		
		channel.join(plugin.getParticipantManager().getParticipant(sender));
		sendMessage(sender, "§6You have joined " + channel.getName());
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return Vault.hasPermission(sender, "TitanChat.participate." + channel.getName());
	}
}