package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public final class PlaceCommand extends Command {
	
	public PlaceCommand() {
		super("Place");
		setAliases("p");
		setArgumentRange(1, 1024);
		setDescription("Places the player(s) in the channel");
		setUsage("[player]...");
		registerHelpTopic(new PlaceHelp());
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (channel == null) {
			msg(sender, C.RED + "Please specify or join a channel to use the command");
			return;
		}
		
		for (String playerName : args) {
			OfflinePlayer player = getOfflinePlayer(playerName);
			
			if (player.isOnline()) {
				if (!channel.isParticipating(player)) {
					channel.join(plugin.getParticipant(player.getName()));
					msg(player, C.GOLD + "You have been placed into " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						msg(sender, C.GOLD + getDisplay(player) + " has been placed into the channel");
					
					broadcast(channel, C.GOLD + getDisplay(player) + " has been placed into the channel");
					
				} else { msg(sender, C.RED + getDisplay(player) + " is already in the channel"); }
				
			} else { msg(sender, C.RED + "Player is not online"); }
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.isAdmin(sender.getName()))
			return true;
		
		if (hasPermission(sender, "TitanChat.place." + channel.getName()))
			return true;
		
		return false;
	}
	
	public final class PlaceHelp implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Places the player(s) in the channel";
		}
		
		public String[] getFullDescription() {
			return new String[] {
					"Description: Places the player(s) in the channel",
					"Aliases: 'p'",
					"Usage: /titanchat <@[channel]> place [player]..."
			};
		}
		
		public String getName() {
			return "Place";
		}
	}
}