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

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.vault.Vault;

/**
 * {@link BlacklistCommand} - Command for blacklisting in channels
 * 
 * @author NodinChan
 *
 */
public final class BlacklistCommand extends Command {
	
	public BlacklistCommand() {
		super("Blaclist");
		setAliases("ban", "b");
		setArgumentRange(1, 1024);
		setUsage("[player] <reason>");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (channel == null) {
			sendMessage(sender, "&4Channel not defined");
			return;
		}
		
		Participant participant = plugin.getParticipantManager().getParticipant(args[0]);
		
		if (channel.getBlacklist().contains(participant.getName())) {
			sendMessage(sender, "&4" + participant.getDisplayName() + " is already banned from the channel");
			return;
		}
		
		String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length));
		
		channel.leave(participant);
		channel.getBlacklist().add(participant.getName());
		participant.sendMessage("&4You have been banned from " + channel.getName() + ": " + reason);
		
		if (!channel.isParticipating(sender.getName()))
			sendMessage(sender, "&6" + participant.getDisplayName() + " has been banned");
		
		broadcast(channel, "&6" + participant.getDisplayName() + " has been banned");
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.getOperators().contains(sender.getName()))
			return true;
		
		return Vault.hasPermission(sender, "TitanChat.blacklist." + channel.getName());
	}
}