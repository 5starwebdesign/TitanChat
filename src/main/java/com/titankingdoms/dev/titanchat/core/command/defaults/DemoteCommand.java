package com.titankingdoms.dev.titanchat.core.command.defaults;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.util.C;

public final class DemoteCommand extends Command {
	
	public DemoteCommand() {
		super("Demote");
		setArgumentRange(1, 1024);
		setBriefDescription("Demotes the player(s)");
		setFullDescription(
				"Description: Demotes the player(s) in the specified channel\n" +
				"Usage: /titanchat <@[channel]> demote [player]...");
		setUsage("[player]...");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (channel == null) {
			msg(sender, C.RED + "Please specify or join a channel to use the command");
			return;
		}
		
		for (String playerName : args) {
			OfflinePlayer player = getOfflinePlayer(playerName);
			
			if (channel.isAdmin(player)) {
				channel.getAdmins().remove(player.getName());
				
				msg(player, C.RED + "You have been demoted in " + channel.getName());
				
				if (!channel.isParticipating(sender.getName()))
					msg(sender, C.RED + getDisplayName(player.getName()) + " has been demoted in the channel");
				
				broadcast(channel, C.RED + getDisplayName(player.getName()) + " has been demoted in the channel");
				
			} else { msg(sender, C.RED + getDisplayName(player.getName()) + " is not an admin"); }
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.isAdmin(sender.getName()))
			return true;
		
		return hasPermission(sender, "TitanChat.rank." + channel.getName());
	}
}