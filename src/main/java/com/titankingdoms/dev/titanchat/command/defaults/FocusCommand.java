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
 * {@link FocusCommand} - Command for directing focus to another {@link Channel}
 * 
 * @author NodinChan
 *
 */
public final class FocusCommand extends Command {
	
	public FocusCommand() {
		super("Focus");
		setAliases("f", "change", "swap");
		setArgumentRange(1, 1);
		setDescription("Direct focus to the channel");
		setUsage("<channel>");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!plugin.getChannelManager().hasAlias(args[0])) {
			sendMessage(sender, "&4Channel does not exist");
			return;
		}
		
		Channel channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (!channel.isLinked(plugin.getParticipantManager().getParticipant(sender))) {
			plugin.getServer().dispatchCommand(sender, "titanchat join " + channel.getName());
			return;
		}
		
		if (!channel.isLinked(plugin.getParticipantManager().getParticipant(sender)))
			return;
		
		Participant participant = plugin.getParticipantManager().getParticipant(sender);
		
		if (channel.equals(participant.getCurrentEndPoint())) {
			sendMessage(sender, "&4You are already speaking in the channel");
			return;
		}
		
		participant.focus(channel);
		sendMessage(sender, "&6You are now speaking in " + channel.getName());
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender) {
		return true;
	}
}