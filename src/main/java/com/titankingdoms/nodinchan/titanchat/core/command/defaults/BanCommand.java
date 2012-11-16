package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public class BanCommand extends Command {
	
	public BanCommand() {
		super("Ban");
		setAliases("b", "blacklist");
		setArgumentRange(1, 1024);
		setDescription("Bans the player(s) from the channel");
		setUsage("[player]...");
		registerHelpTopic(new BanHelp());
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		switch (channel.getType()) {
		
		case CUSTOM:
		case NONE:
			for (String playerName : args) {
				OfflinePlayer player = getOfflinePlayer(playerName);
				
				if (!channel.isBlacklisted(player)) {
					channel.getBlacklist().add(player.getName());
					
					if (channel.isAdmin(player))
						channel.getAdmins().remove(player.getName());
					
					if (channel.isParticipating(player))
						channel.leave(plugin.getParticipant(player.getName()));
					
					msg(player, C.RED + "You have been banned from " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						msg(sender, C.RED + getDisplay(player) + " has been banned from the channel");
					
					broadcast(channel, C.RED + getDisplay(player) + " has been banned from the channel");
					
				} else { msg(sender, C.RED + getDisplay(player) + " was already banned"); }
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
	
	public final class BanHelp implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Bans the player(s) from the channel";
		}
		
		public String[] getFullDescription() {
			return new String[] {
					"Description: Bans the player(s) from the channel",
					"Aliases: 'b', 'blacklist'",
					"Usage: /titanchat <@[channel]> ban [player]..."
			};
		}
		
		public String getName() {
			return "Ban";
		}
	}
}