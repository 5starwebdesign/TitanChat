/*
 *     Copyright (C) 2014  Nodin Chan
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
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.tools;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messaging {
	
	public static void message(Collection<Player> recipients, String... lines) {
		message(recipients.toArray(new Player[0]), lines);
	}
	
	public static void message(Player[] recipients, String... lines) {
		String line = StringUtils.join(lines, '\n');
		
		for (CommandSender recipient : recipients)
			recipient.sendMessage(line);
	}
	
	public static void message(Server server, String... lines) {
		message(server.getOnlinePlayers(), lines);
	}
	
	public static void message(World world, String... lines) {
		message(world.getPlayers(), lines);
	}
	
	public static void message(CommandSender recipient, String... lines) {
		recipient.sendMessage(StringUtils.join(lines, '\n'));
	}
}