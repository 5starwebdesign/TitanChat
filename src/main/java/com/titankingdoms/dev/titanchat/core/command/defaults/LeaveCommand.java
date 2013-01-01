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
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.util.C;

public final class LeaveCommand extends Command {
	
	public LeaveCommand() {
		super("Leave");
		setAliases("l", "part");
		setArgumentRange(1, 1);
		setBriefDescription("Leaves the channel");
		setFullDescription(
				"Description: Leaves the specified channel\n" +
				"Aliases: 'leave', 'l', 'part'\n" +
				"Usage: /titanchat leave [channel]");
		setUsage("[channel]");
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
}