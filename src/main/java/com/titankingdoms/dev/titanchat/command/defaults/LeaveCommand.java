package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.vault.Vault;

public final class LeaveCommand extends Command {
	
	public LeaveCommand() {
		super("Leave");
		setAliases("l");
		setArgumentRange(1, 1);
		setUsage("[channel]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().hasAlias(args[0])) {
			sendMessage(sender, "§4Channel does not exist");
			return;
		}
		
		channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (!channel.isParticipating(sender.getName())) {
			sendMessage(sender, "§4You have not joined the channel");
			return;
		}
		
		channel.leave(plugin.getParticipantManager().getParticipant(sender));
		sendMessage(sender, "§6You have left " + channel.getName());
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return Vault.hasPermission(sender, "TitanChat.participate." + channel.getName());
	}
}