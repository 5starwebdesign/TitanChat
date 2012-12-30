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

public final class KickCommand extends Command {
	
	public KickCommand() {
		super("Kick");
		setAliases("k");
		setArgumentRange(1, 1024);
		setDescription("Kicks the player(s) from the channel");
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
					channel.leave(plugin.getParticipantManager().getParticipant(player.getName()));
					msg(player, C.RED + "You have been kicked from " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						msg(sender, C.RED + getDisplay(player) + " has been kicked from the channel");
					
					broadcast(channel, C.RED + getDisplay(player) + " has been kicked from the channel");
					
				} else { msg(sender, C.RED + getDisplay(player) + " is already in the channel"); }
				
			} else { msg(sender, C.RED + "Player is not online"); }
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		if (channel.isAdmin(sender.getName()))
			return true;
		
		if (hasPermission(sender, "TitanChat.kick." + channel.getName()))
			return true;
		
		return false;
	}
}