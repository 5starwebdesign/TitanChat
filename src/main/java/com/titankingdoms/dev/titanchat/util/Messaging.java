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

package com.titankingdoms.dev.titanchat.util;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.format.Format;

/**
 * {@link Messaging} - Messaging util
 * 
 * @author NodinChan
 *
 */
public final class Messaging {
	
	/**
	 * Broadcasts the messages
	 * 
	 * @param messages The messages to broadcast
	 */
	public static void broadcast(String... messages) {
		if (messages == null)
			return;
		
		for (String message : Format.colourise(messages))
			TitanChat.getInstance().getServer().broadcastMessage(message);
	}
	
	/**
	 * Broadcasts the messages to the {@link World}
	 * 
	 * @param world The {@link World} to broadcast to
	 * 
	 * @param messages The messages to broadcast
	 */
	public static void broadcast(World world, String... messages) {
		if (world == null || messages == null)
			return;
		
		messages = Format.colourise(messages);
		
		for (Player player : world.getPlayers())
			player.sendMessage(messages);
	}
	
	/**
	 * Broadcasts the messages to the area around the {@link CommandSender}
	 * 
	 * @param sender The {@link CommandSender}
	 * 
	 * @param radius The radius around the {@link CommandSender}
	 * 
	 * @param messages The messages to broadcast
	 */
	public static void broadcast(CommandSender sender, double radius, String... messages) {
		if (sender == null || radius == 0 || messages == null)
			return;
		
		messages = Format.colourise(messages);
		
		if (!(sender instanceof Player))
			sender.sendMessage(messages);
		
		for (Entity entity : ((Player) sender).getNearbyEntities(radius, radius, radius))
			if (entity instanceof Player)
				((Player) entity).sendMessage(messages);
	}
	
	/**
	 * Sends the messages to the {@link CommandSender}
	 * 
	 * @param sender The {@link CommandSender} to send to
	 * 
	 * @param messages The messages to send
	 */
	public static void sendMessage(CommandSender sender, String... messages) {
		if (sender == null || messages == null)
			return;
		
		sender.sendMessage(Format.colourise(messages));
	}
}