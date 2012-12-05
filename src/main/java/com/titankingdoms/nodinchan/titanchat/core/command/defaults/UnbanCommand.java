package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;
import com.titankingdoms.nodinchan.titanchat.util.Messaging;

public final class UnbanCommand extends Command {
	
	public UnbanCommand() {
		super("Unban");
		setAliases("ub", "pardon");
		setArgumentRange(1, 1024);
		setDescription("Unbans the player(s) from the channel");
		setUsage("[player]...");
		registerHelpTopic(new UnbanTopic());
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (channel == null) {
			msg(sender, C.RED + "Please specify or join a channel to use the command");
			return;
		}
		
		switch (channel.getType()) {
		
		case CUSTOM:
		case NONE:
			for (String playerName : args) {
				OfflinePlayer player = getOfflinePlayer(playerName);
				
				if (channel.isBlacklisted(player)) {
					channel.getBlacklist().remove(player.getName());
					
					msg(player, C.RED + "You have been unbanned from " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						msg(sender, C.GOLD + getDisplay(player) + " has been unbanned");
					
					Messaging.broadcast(channel, getDisplay(player) + " has been unbanned");
					
				} else { msg(sender, C.RED + getDisplay(player) + " was not banned"); }
			}
			break;
			
		default:
			msg(sender, C.RED + "Command not available for " + channel.getType().getName() + " channels");
			break;
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.isAdmin(sender.getName()))
			return true;
		
		if (hasPermission(sender, "TitanChat.ban." + channel.getName()))
			return true;
		
		return false;
	}
	
	public final class UnbanTopic implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Unbans the player(s) from the channel";
		}
		
		public String[][] getFullDescription() {
			return new String[][] {
					{
						"Description: Unbans the player(s) from the channel",
						"Aliases: 'ub', 'pardon'",
						"Usage: /titanchat <@[channel]> unban [player]..."
					}
			};
		}
		
		public String getName() {
			return "Unban";
		}
	}
}