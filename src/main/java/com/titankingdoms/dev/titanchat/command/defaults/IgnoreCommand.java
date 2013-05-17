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
 * {@link IgnoreCommand} - Command for editting or viewing the ignore list
 * 
 * @author NodinChan
 *
 */
public final class IgnoreCommand extends Command {
	
	public IgnoreCommand() {
		super("Ignore");
		setArgumentRange(1, 2);
		setDescription("Edit or view the ignore list");
		setUsage("<add|list|remove> [player]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		Participant participant = plugin.getParticipantManager().getParticipant(sender);
		
		if (args[0].equals("list")) {
			String list = StringUtils.join(participant.getIgnoreList(), ", ");
			sendMessage(sender, "&6Ignore List: " + list);
			return;
			
		} else {
			if (args.length < 2) {
				sendMessage(sender, "&4Invalid argument length");
				
				String usage = "/titanchat ignore " + getUsage();
				sendMessage(sender, "&6" + usage);
				return;
			}
		}
		
		Participant target = plugin.getParticipantManager().getParticipant(args[1]);
		
		if (args[0].equalsIgnoreCase("add")) {
			if (participant.getIgnoreList().contains(target.getName())) {
				sendMessage(sender, target.getDisplayName() + " &4is already on the ignore list");
				return;
			}
			
			participant.getIgnoreList().add(target.getName());
			target.sendMessage("&4You have been added to the ignore list of " + participant.getDisplayName());
			sendMessage(sender, target.getDisplayName() + " &6has been added to the ignore list");
			
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (!participant.getIgnoreList().contains(target.getName())) {
				sendMessage(sender, target.getDisplayName() + " &4is not on the ignore list");
				return;
			}
			
			participant.getIgnoreList().remove(target.getName());
			target.sendMessage("&6You have been removed from the ignore list of " + participant.getDisplayName());
			sendMessage(sender, target.getDisplayName() + " &6has been removed from the ignore list");
			
		} else {
			sendMessage(sender, "&4Incorrect usage: /titanchat ignore " + getUsage());
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}