package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public final class LeaveCommand extends Command {
	
	public LeaveCommand() {
		super("Leave");
		setAliases("l");
		setArgumentRange(1, 1);
		setDescription("Leaves the channel");
		setUsage("[channel]");
		registerHelpTopic(new LeaveTopic());
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().existingChannelAlias(args[0])) {
			msg(sender, C.RED + "Channel does not exist");
			return;
		}
		
		channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (!hasPermission(sender, "TitanChat.leave." + channel.getName())) {
			msg(sender, C.RED + "You do not have permission");
			return;
		}
		
		if (channel.isParticipating(sender.getName())) {
			channel.leave(plugin.getParticipantManager().getParticipant(sender.getName()));
			msg(sender, C.GOLD + "You have left " + channel.getName());
			
		} else { msg(sender, C.RED + "You are not in the channel"); }
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
	
	public final class LeaveTopic implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Leaves the channel";
		}
		
		public String[][] getFullDescription() {
			return new String[][] {
					{
						"Description: Leaves the channel",
						"Aliases: 'l'",
						"Usage: /titanchat <@[channel]> leave [channel]"
					}
			};
		}
		
		public String getName() {
			return "Leave";
		}
	}
}