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

import com.titankingdoms.dev.titanchat.command.ChannelCommand;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link WhitelistCommand} - Command for editting and view the whitelist in {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public final class WhitelistCommand extends ChannelCommand {
	
	public WhitelistCommand() {
		super("Whitelist");
		setAliases("w");
		setArgumentRange(1, 1024);
		setDescription("Edit or view the whitelist of the channel");
		setUsage("<add|list|remove> [player] [reason]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (channel == null) {
			sendMessage(sender, "&4Channel not defined");
			return;
		}
		
		if (!channel.getConfig().getBoolean("whitelist", false)) {
			sendMessage(sender, "&4Whitelist is not enabled for the channel");
			return;
		}
		
		if (args[0].equalsIgnoreCase("list")) {
			String list = StringUtils.join(channel.getWhitelist(), ", ");
			sendMessage(sender, "&6" + channel.getName() + " Whitelist: " + list);
			return;
			
		} else {
			if (args.length < 2) {
				sendMessage(sender, "&4Invalid argument length");
				
				String usage = "/titanchat [@<channel>] whitelist " + getUsage();
				sendMessage(sender, "&6" + usage);
				return;
			}
		}
		
		Participant participant = plugin.getParticipantManager().getParticipant(args[1]);
		
		if (args[0].equalsIgnoreCase("add")) {
			if (channel.getWhitelist().contains(participant.getName())) {
				sendMessage(sender, participant.getDisplayName() + " &4is already on the whitelist");
				return;
			}
			
			channel.getWhitelist().add(participant.getName());
			participant.notice("&6You have been added to the whitelist of " + channel.getName());
			
			if (args.length > 2) {
				String reason = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ").trim();
				participant.notice("&6Reason: " + reason);
			}
			
			if (!channel.isLinked(plugin.getParticipantManager().getParticipant(sender)))
				sendMessage(sender, participant.getDisplayName() + " &6has been added to the whitelist");
			
			channel.notice(participant.getDisplayName() + " &6has been added to the whitelist");
			
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (!channel.getWhitelist().contains(participant.getName())) {
				sendMessage(sender, participant.getDisplayName() + " &4is not on the whitelist");
				return;
			}
			
			channel.getWhitelist().remove(participant.getName());
			participant.notice("&4You have been removed from the whitelisted of " + channel.getName());
			
			if (args.length > 2) {
				String reason = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ").trim();
				participant.notice("&4Reason: " + reason);
			}
			
			if (!channel.isLinked(plugin.getParticipantManager().getParticipant(sender)))
				sendMessage(sender, participant.getDisplayName() + " &6has been removed from the whitelist");
			
			channel.notice(participant.getDisplayName() + " &6has been removed from the whitelist");
			
		} else {
			sendMessage(sender, "&4Incorrect usage: /titanchat [@<channel>] whitelist " + getUsage());
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel == null)
			return false;
		
		if (channel.getOperators().contains(sender.getName()))
			return true;
		
		return Vault.hasPermission(sender, "TitanChat.whitelist." + channel.getName());
	}
}