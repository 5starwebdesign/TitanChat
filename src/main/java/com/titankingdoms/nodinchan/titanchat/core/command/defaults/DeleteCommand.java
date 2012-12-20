package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public final class DeleteCommand extends Command {
	
	public DeleteCommand() {
		super("Delete");
		setAliases("d");
		setArgumentRange(1, 1);
		setDescription("Deletes the channel");
		setUsage("[channel]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().existingChannelAlias(args[0])) {
			msg(sender, C.RED + "Channel does not exist");
			return;
		}
		
		channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (!channel.getType().equals(Type.NONE)) {
			msg(sender, C.RED + "You cannot delete this type of channels");
			return;
		}
		
		plugin.getChannelManager().deleteChannel(sender, channel.getName());
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return hasPermission(sender, "TitanChat.delete");
	}
	
	public final class DeleteTopic implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Deletes the channel";
		}
		
		public String[][] getFullDescription() {
			return new String[][] {
					{
						"Description: Deletes the channel",
						"Aliases: 'd'",
						"Usage: /titanchat <@[channel]> delete [channel]"
					}
			};
		}
		
		public String getName() {
			return "Delete";
		}
	}
}