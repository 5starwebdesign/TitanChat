package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public final class KickCommand extends Command {
	
	public KickCommand() {
		super("Kick");
		setAliases("k");
		setArgumentRange(1, 1024);
		setDescription("Kicks the player(s) from the channel");
		setUsage("[player]...");
		registerHelpTopic(new KickHelp());
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		for (String playerName : args) {
			OfflinePlayer player = getOfflinePlayer(playerName);
			
			if (player.isOnline()) {
				if (!channel.isParticipating(player)) {
					channel.leave(plugin.getParticipant(player.getName()));
					msg(player, C.RED + "You have been kicked from " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						msg(sender, C.RED + getDisplay(player) + " has been kicked from the channel");
					
					broadcast(channel, C.RED + getDisplay(player) + " has been kicked from the channel");
					
				} else { msg(sender, C.RED + getDisplay(player) + " is already in the channel"); }
				
			} else { msg(sender, C.RED + "Player is not online"); }
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.isAdmin(sender.getName()))
			return true;
		
		if (hasPermission(sender, "TitanChat.kick." + channel.getName()))
			return true;
		
		return false;
	}
	
	public final class KickHelp implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Kicks the player(s) from the channel";
		}
		
		public String[] getFullDescription() {
			return new String[] {
					"Description: Kicks the player(s) from the channel",
					"Aliases: 'k'",
					"Usage: /titanchat <@[channel]> kick [player]..."
			};
		}
		
		public String getName() {
			return "Kick";
		}
	}
}