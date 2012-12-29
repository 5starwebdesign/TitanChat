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

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.Type;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.help.HelpTopic;
import com.titankingdoms.dev.titanchat.util.C;

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