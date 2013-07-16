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
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link EmoteCommand} - Command for emoting in {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public final class EmoteCommand extends ChannelCommand {
	
	public EmoteCommand() {
		super("Emote");
		setAliases("em", "me");
		setArgumentRange(1, 1024);
		setDescription("Emote in the channel");
		setUsage("[emote]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		String format = "* " + sender.getName() + " %message";
		String message = StringUtils.join(args, " ");
		
		channel.processConversation(plugin.getParticipantManager().getParticipant(sender), format, message);
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