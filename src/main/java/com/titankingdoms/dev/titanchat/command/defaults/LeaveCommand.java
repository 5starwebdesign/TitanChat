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
import com.titankingdoms.dev.titanchat.vault.Vault;

/**
 * {@link LeaveCommand} - Command for leaving channels
 * 
 * @author NodinChan
 *
 */
public final class LeaveCommand extends Command {
	
	public LeaveCommand() {
		super("Leave");
		setAliases("l");
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
		
		if (!channel.getOperators().contains(sender.getName())) {
			if (!Vault.hasPermission(sender, "TitanChat.leave." + channel.getName())) {
				sendMessage(sender, "&4You do not have permission");
				return;
			}
		}
		
		if (!channel.isParticipating(sender.getName())) {
			sendMessage(sender, "&4You have not joined the channel");
			return;
		}
		
		channel.leave(plugin.getParticipantManager().getParticipant(sender));
		sendMessage(sender, "&6You have left " + channel.getName());
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}