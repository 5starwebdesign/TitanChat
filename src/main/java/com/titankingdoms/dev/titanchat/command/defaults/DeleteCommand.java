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
import com.titankingdoms.dev.titanchat.event.ChannelDeletionEvent;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link DeleteCommand} - Command for {@link Channel} deletion
 * 
 * @author NodinChan
 *
 */
public final class DeleteCommand extends Command {
	
	public DeleteCommand() {
		super("Delete");
		setAliases("d");
		setArgumentRange(1, 1);
		setDescription("Delete a channel");
		setUsage("<channel>");
	}

	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (!plugin.getChannelManager().hasChannel(args[0])) {
			sendMessage(sender, "&4Channel does not exist");
			return;
		}
		
		channel = plugin.getChannelManager().getChannel(args[0]);
		plugin.getChannelManager().unregisterChannel(channel);
		
		ChannelDeletionEvent event = new ChannelDeletionEvent(channel);
		plugin.getServer().getPluginManager().callEvent(event);
		
		if (!channel.isParticipating(sender.getName()))
			sendMessage(sender, "&6" + channel.getName() + " has been deleted");
		
		channel.sendMessage("&6" + channel.getName() + " has been deleted");
		
		for (Participant participant : channel.getParticipants())
			channel.leave(participant);
		
		channel.getConfigFile().delete();
	}

	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return Vault.hasPermission(sender, "TitanChat.delete");
	}
}