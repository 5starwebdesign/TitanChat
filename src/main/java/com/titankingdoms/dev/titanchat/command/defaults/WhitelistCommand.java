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
import com.titankingdoms.dev.titanchat.vault.Vault;

/**
 * {@link WhitelistCommand} - Command for editting whitelist in channels
 * 
 * @author NodinChan
 *
 */
public final class WhitelistCommand extends Command {
	
	public WhitelistCommand() {
		super("Whitelist");
		setArgumentRange(2, 2);
		setUsage("[add/remove] [player]");
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
		
		Participant participant = plugin.getParticipantManager().getParticipant(args[1]);
		
		if (args[0].equalsIgnoreCase("add")) {
			if (channel.getWhitelist().contains(participant.getName())) {
				sendMessage(sender, "&4" + participant.getDisplayName() + " is already whitelisted");
				return;
			}
			
			channel.getWhitelist().add(participant.getName());
			participant.sendMessage("&6You have been added to the whitelist of " + channel.getName());
			
			if (!channel.isParticipating(sender.getName()))
				sendMessage(sender, "&6" + participant.getDisplayName() + " has been added to whitelist");
			
			broadcast(channel, "&6" + participant.getDisplayName() + " has been added to whitelist");
			
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (!channel.getWhitelist().contains(participant.getName())) {
				sendMessage(sender, "&4" + participant.getDisplayName() + " has not been whitelisted");
				return;
			}
			
			channel.getWhitelist().remove(participant.getName());
			participant.sendMessage("&6You have been removed from the whitelisted of " + channel.getName());
			
			if (!channel.isParticipating(sender.getName()))
				sendMessage(sender, "&6" + participant.getDisplayName() + " has been removed from whitelist");
			
			broadcast(channel, "&6" + participant.getDisplayName() + " has been removed from whitelist");
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.getOperators().contains(sender.getName()))
			return true;
		
		return Vault.hasPermission(sender, "TitanChat.whitelist." + channel.getName());
	}
}