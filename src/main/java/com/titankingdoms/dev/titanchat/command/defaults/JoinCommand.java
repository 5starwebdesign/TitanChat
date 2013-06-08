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
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link JoinCommand} - Command for joining {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public final class JoinCommand extends Command {
	
	public JoinCommand() {
		super("Join");
		setAliases("j");
		setArgumentRange(1, 2);
		setDescription("Join the channel");
		setUsage("<channel> [password]");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!plugin.getChannelManager().hasAlias(args[0])) {
			sendMessage(sender, "&4Channel does not exist");
			return;
		}
		
		Channel channel = plugin.getChannelManager().getChannel(args[0]);
		
		if (!channel.getOperators().contains(sender.getName())) {
			if (!Vault.hasPermission(sender, "TitanChat.join." + channel.getName())) {
				sendMessage(sender, "&4You do not have permission");
				return;
			}
		}
		
		if (channel.isParticipating(sender.getName())) {
			sendMessage(sender, "&4You have already joined the channel");
			return;
		}
		
		if (channel.getStatus().equals(Status.STAFF) && !Vault.hasPermission(sender, "TitanChat.staff")) {
			sendMessage(sender, "&4You do not have permission to join staff channels");
			return;
		}
		
		if (channel.getBlacklist().contains(sender.getName())) {
			sendMessage(sender, "&4You are blacklisted from the channel");
			return;
		}
		
		if (channel.getConfig().getBoolean("whitelist", false)) {
			if (!channel.getWhitelist().contains(sender.getName())) {
				sendMessage(sender, "&4You are not whitelisted for the channel");
				return;
			}
		}
		
		if (!channel.getConfig().getString("password", "").isEmpty()) {
			if (args.length < 2) {
				sendMessage(sender, "&4Please enter a password");
				return;
			}
			
			if (!channel.getConfig().getString("password", "").equals(args[1])) {
				sendMessage(sender, "&4Incorrect password");
				return;
			}
		}
		
		channel.join(plugin.getParticipantManager().getParticipant(sender));
		sendMessage(sender, "&6You have joined " + channel.getName());
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender) {
		return true;
	}
}