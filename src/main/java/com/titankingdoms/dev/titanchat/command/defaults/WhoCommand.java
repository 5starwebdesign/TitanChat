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

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link WhoCommand} - Command for getting info about a player
 * 
 * @author NodinChan
 *
 */
public final class WhoCommand extends Command {
	
	public WhoCommand() {
		super("Who");
		setArgumentRange(0, 1);
		setUsage("<player>");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		Participant participant = plugin.getParticipantManager().getParticipant(sender);
		
		if (args.length > 0)
			participant = plugin.getParticipantManager().getParticipant(args[0]);
		
		sendMessage(sender, "&6Name: &5" + participant.getName());
		sendMessage(sender, "&6Status: &5" + ((participant.isOnline()) ? "Online" : "Offline"));
		sendMessage(sender, "&6Channels: &5" + StringUtils.join(participant.getChannelList(), ", "));
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}