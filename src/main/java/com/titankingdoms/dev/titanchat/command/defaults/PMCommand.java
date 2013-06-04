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
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link PMCommand} - Command for private messaging
 * 
 * @author NodinChan
 *
 */
public final class PMCommand extends Command {

	public PMCommand() {
		super("PM");
		setAliases("msg", "privmsg");
		setArgumentRange(1, 1024);
		setDescription("Private messaging");
		setUsage("<player> [message]");
	}

	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		Participant target = plugin.getParticipantManager().getParticipant(args[0]);
		
		if (!target.isOnline()) {
			sendMessage(sender, target.getDisplayName() + " &4is currently offline");
			return;
		}
		
		Participant participant = plugin.getParticipantManager().getParticipant(sender);
		participant.join(target.getPM());
		sendMessage(sender, "&6You have started a private conversation with " + target.getDisplayName());
		target.notice(participant.getDisplayName() + " &6has started a private conversation with you");
		
		if (args.length > 1)
			participant.chatOut(target.getPM(), StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
	}

	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return Vault.hasPermission(sender, "TitanChat.privmsg");
	}
}