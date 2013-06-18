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
import com.titankingdoms.dev.titanchat.core.channel.setting.Status;
import com.titankingdoms.dev.titanchat.format.ChatUtils;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link ListCommand} - Command for listing permitted {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public final class ListCommand extends Command {
	
	public ListCommand() {
		super("List");
		setAliases("channellist", "chlist", "chl");
		setDescription("Get list of permitted channels");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		String header = "&b" + StringUtils.center(" Channel List ", 50, '=');
		
		StringBuilder list = new StringBuilder();
		
		for (Channel ch : plugin.getChannelManager().getChannels()) {
			if (!ch.getOperators().contains(sender.getName()))
				if (!Vault.hasPermission(sender, "TitanChat.join." + ch.getName()))
					continue;
			
			if (ch.getStatus().equals(Status.STAFF) && !Vault.hasPermission(sender, "TitanChat.staff"))
				continue;
			
			if (ch.getBlacklist().contains(sender.getName()))
				continue;
			
			if (ch.getConfig().getBoolean("whitelist", false))
				if (!ch.getWhitelist().contains(sender.getName()))
					continue;
			
			if (list.length() > 0)
				list.append(", ");
			
			list.append(ch.getName() + " (" + ch.getParticipants().size() + ")");
		}
		
		sender.sendMessage(header);
		sender.sendMessage(ChatUtils.wordWrap(list.toString(), 50));
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender) {
		return true;
	}
}