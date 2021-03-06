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

import com.titankingdoms.dev.titanchat.command.ChannelCommand;
import com.titankingdoms.dev.titanchat.core.EndPoint;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.event.ChatProcessEvent;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link ChatCommand} - Command for sending messages to {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public final class ChatCommand extends ChannelCommand {
	
	public ChatCommand() {
		super("Chat");
		setAliases("ch");
		setArgumentRange(1, 1024);
		setDescription("Send the message to the channel");
		setUsage("<message>");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		EndPoint msgSender = plugin.getParticipantManager().getParticipant(sender);
		EndPoint msgRecipient = channel;
		
		String format = msgRecipient.getConversationFormat();
		String message = StringUtils.join(args, " ");
		
		ChatProcessEvent event = msgRecipient.processConversation(msgSender, format, message);
		plugin.getServer().getPluginManager().callEvent(event);
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel == null)
			return false;
		
		if (channel.getOperators().contains(sender.getName()))
			return true;
		
		return Vault.hasPermission(sender, "TitanChat.speak." + channel.getName());
	}
}