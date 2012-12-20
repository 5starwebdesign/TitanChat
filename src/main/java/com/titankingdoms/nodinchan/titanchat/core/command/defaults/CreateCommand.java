package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import java.io.File;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public final class CreateCommand extends Command {
	
	public CreateCommand() {
		super("Create");
		setAliases("c");
		setArgumentRange(1, 2);
		setDescription("Creates a channel with specified ChannelLoader or StandardLoader");
		setUsage("[channel] <loader>");
		registerHelpTopic(new CreateTopic());
	}

	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		String channelName = args[0];
		String loaderName = "Standard";
		
		if (plugin.getChannelManager().existingChannelAlias(args[0])) {
			msg(sender, C.RED + "Channel already existed");
			return;
		}
		
		if (channelName.isEmpty()) {
			msg(sender, C.RED + "Channel names cannot be empty");
			return;
		}
		
		try {
			new File(channelName + ".yml").getCanonicalPath();
			
		} catch (Exception e) {
			msg(sender, C.RED + "Channel names cannot contain invalid characters");
			return;
		}
		
		if (args.length > 1 && !args[1].isEmpty())
			loaderName = args[1];
		
		if (!plugin.getChannelManager().existingLoader(loaderName)) {
			msg(sender, C.RED + "ChannelLoader does not exist");
			return;
		}
		
		int limit = plugin.getChannelManager().getLimit();
		
		if (limit >= 0) {
			if (plugin.getChannelManager().getChannels().size() >= limit) {
				msg(sender, C.RED + "Amount of channels has reached the limit");
				return;
			}
		}
		
		ChannelLoader loader = plugin.getChannelManager().getLoader(loaderName);
		plugin.getChannelManager().createChannel(sender, channelName, loader);
	}

	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return hasPermission(sender, "TitanChat.create");
	}
	
	public final class CreateTopic implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Creates a channel";
		}
		
		public String[][] getFullDescription() {
			return new String[][] {
					{
						"Description: Creates a channel with specified ChannelLoader or StandardLoader",
						"Aliases: 'c'",
						"Usage: /titanchat <@[channel]> create [channel] <loader>"
					}
			};
		}
		
		public String getName() {
			return "Create";
		}
	}
}