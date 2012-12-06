package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public final class DirectCommand extends Command {
	
	public DirectCommand(String name) {
		super("Direct");
		setArgumentRange(1, 1);
		setDescription("Sets your current channel to the specified channel");
		setUsage("[channel]");
		registerHelpTopic(new DirectTopic());
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().existingChannelAlias(args[0])) {
			msg(sender, C.RED + "Channel does not exist");
			return;
		}
		
		channel = plugin.getChannel(args[0]);
		
		if (!channel.isParticipating(sender.getName())) {
			msg(sender, C.RED + "You have not joined the channel");
			msg(sender, C.GOLD + "Attempting to join...");
			plugin.getServer().dispatchCommand(sender, "titanchat join " + channel.getName());
			return;
		}
		
		Participant participant = plugin.getParticipant(sender.getName());
		
		if (!participant.isDirectedAt(channel)) {
			participant.direct(channel);
			msg(sender, C.GOLD + "You have changed your current channel to " + channel.getName());
			
		} else { msg(sender, C.RED + "Your current channel is already " + channel.getName()); }
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
	
	public final class DirectTopic implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Sets your current channel";
		}
		
		public String[][] getFullDescription() {
			return new String[][] {
					{
						"Description: Sets your current channel to the specified channel",
						"Usage: /titanchat <@[channel]> direct [channel]"
					}
			};
		}
		
		public String getName() {
			return "Direct";
		}
	}
}