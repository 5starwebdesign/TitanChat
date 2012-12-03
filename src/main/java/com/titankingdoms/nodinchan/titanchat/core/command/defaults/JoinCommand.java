package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public final class JoinCommand extends Command {
	
	public JoinCommand() {
		super("Join");
		setAliases("j");
		setArgumentRange(1, 2);
		setDescription("Joins the channel");
		setUsage("[channel] <password>");
		registerHelpTopic(new JoinTopic());
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().existingChannelAlias(args[0])) {
			msg(sender, C.RED + "Channel does not exist");
			return;
		}
		
		channel = plugin.getChannel(args[0]);
		
		if (!hasPermission(sender, "TitanChat.join." + channel.getName())) {
			msg(sender, C.RED + "You do not have permission");
			return;
		}
		
		if (channel.getType().equals(Type.STAFF) && !plugin.isStaff(sender)) {
			msg(sender, C.RED + "You do not have permission to join a staff channel");
			return;
		}
		
		if (channel.getInfo().getPassword() != null && !channel.getInfo().getPassword().isEmpty()) {
			if (args.length < 2) {
				msg(sender, C.RED + "Please enter a password");
				return;
			}
			
			if (!channel.getInfo().getPassword().equals(args[1])) {
				msg(sender, C.RED + "Incorrect Password");
				return;
			}
		}
		
		if (channel.isBlacklisted(sender.getName())) {
			msg(sender, C.RED + "You are banned from the channel");
			return;
		}
		
		if (channel.getInfo().getSetting("whitelist", false)) {
			if (!channel.isWhitelisted(sender.getName())) {
				msg(sender, C.RED + "You are not whitelisted for the channel");
				return;
			}
		}
		
		if (!channel.isParticipating(sender.getName())) {
			channel.join(plugin.getParticipant(sender.getName()));
			msg(sender, C.GOLD + "You have joined " + channel.getName());
			
		} else {
			msg(sender, C.RED + "You have already joined the channel");
			msg(sender, C.GOLD + "Attempting to set as current...");
			plugin.getServer().dispatchCommand(sender, "titanchat direct " + channel.getName());
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
	
	public final class JoinTopic implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Joins the channel";
		}
		
		public String[][] getFullDescription() {
			return new String[][] {
					{
						"Description: Joins the channel",
						"Aliases: 'j'",
						"Usage: /titanchat <@[channel]> join [channel] <password>"
					}
			};
		}
		
		public String getName() {
			return "Join";
		}
	}
}