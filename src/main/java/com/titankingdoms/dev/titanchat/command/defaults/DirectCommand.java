/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link DirectCommand} - Command for directing attention to another channel
 * 
 * @author NodinChan
 *
 */
public final class DirectCommand extends Command {
	
	public DirectCommand() {
		super("Direct");
		setArgumentRange(1, 1);
		setUsage("[channel]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().hasAlias(args[0])) {
			sendMessage(sender, "&4Channel does not exist");
			return;
		}
		
		channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (!channel.isParticipating(sender.getName())) {
			plugin.getServer().dispatchCommand(sender, "titanchat join " + channel.getName());
			return;
		}
		
		Participant participant = plugin.getParticipantManager().getParticipant(sender);
		
		if (participant.getCurrentChannel().equals(channel)) {
			sendMessage(sender, "&4You are already speaking in the channel");
			return;
		}
		
		participant.direct(channel);
		sendMessage(sender, "&6You are now speaking in " + channel.getName());
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}