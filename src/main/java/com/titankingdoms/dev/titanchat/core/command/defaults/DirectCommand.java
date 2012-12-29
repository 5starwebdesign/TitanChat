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
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.help.HelpTopic;
import com.titankingdoms.dev.titanchat.util.C;

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
		
		channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (!channel.isParticipating(sender.getName())) {
			msg(sender, C.RED + "You have not joined the channel");
			msg(sender, C.GOLD + "Attempting to join...");
			plugin.getServer().dispatchCommand(sender, "titanchat join " + channel.getName());
			return;
		}
		
		Participant participant = plugin.getParticipantManager().getParticipant(sender.getName());
		
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