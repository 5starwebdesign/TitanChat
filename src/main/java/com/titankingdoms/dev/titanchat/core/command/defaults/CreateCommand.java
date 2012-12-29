/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
 *     
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.titankingdoms.dev.titanchat.core.command.defaults;

import java.io.File;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.help.HelpTopic;
import com.titankingdoms.dev.titanchat.util.C;

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