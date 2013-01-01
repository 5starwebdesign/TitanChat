/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.core.command.defaults;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.util.C;

public final class PlaceCommand extends Command {
	
	public PlaceCommand() {
		super("Place");
		setAliases("p");
		setArgumentRange(1, 1024);
		setBriefDescription("Places the player(s)");
		setFullDescription(
				"Description: Places the player(s) in the specified channel\n" +
				"Aliases: 'place', 'p'\n" +
				"Usage: /titanchat <@[channel]> place [player]...");
		setUsage("[player]...");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (channel == null) {
			msg(sender, C.RED + "Please specify or join a channel to use the command");
			return;
		}
		
		for (String playerName : args) {
			OfflinePlayer player = getOfflinePlayer(playerName);
			
			if (player.isOnline()) {
				if (!channel.isParticipating(player)) {
					channel.join(plugin.getParticipantManager().getParticipant(player.getName()));
					msg(player, C.GOLD + "You have been placed into " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						msg(sender, C.GOLD + getDisplayName(player.getName()) + " has been placed into the channel");
					
					broadcast(channel, C.GOLD + getDisplayName(player.getName()) + " has been placed into the channel");
					
				} else { msg(sender, C.RED + getDisplayName(player.getName()) + " is already in the channel"); }
				
			} else { msg(sender, C.RED + "Player is not online"); }
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.isAdmin(sender.getName()))
			return true;
		
		if (hasPermission(sender, "TitanChat.place." + channel.getName()))
			return true;
		
		return false;
	}
}