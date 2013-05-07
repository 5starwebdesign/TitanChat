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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.format.ChatUtils;
import com.titankingdoms.dev.titanchat.format.Format;

/**
 * {@link Messaging} - Messaging util
 * 
 * @author NodinChan
 *
 */
public final class Messaging {
	
	/**
	 * Broadcasts the message
	 * 
	 * @param messages The messages to broadcast
	 */
	public static void broadcast(String... messages) {
		List<String> lines = new ArrayList<String>();
		
		for (String message : messages)
			lines.addAll(Arrays.asList(ChatUtils.wordWrap(Format.colourise(message), 65)));
		
		for (String line : lines)
			TitanChat.getInstance().getServer().broadcastMessage(line);
	}
	
	/**
	 * Broadcasts the message to the {@link World}
	 * 
	 * @param world The {@link World} to broadcast to
	 * 
	 * @param messages The messages to broadcast
	 */
	public static void broadcast(World world, String... messages) {
		if (world == null)
			return;
		
		List<String> lines = new ArrayList<String>();
		
		for (String message : messages)
			lines.addAll(Arrays.asList(ChatUtils.wordWrap(Format.colourise(message), 65)));
		
		for (Player player : world.getPlayers())
			player.sendMessage(lines.toArray(new String[0]));
	}
	
	/**
	 * Broadcasts the message to the {@link Channel}
	 * 
	 * @param channel The {@link Channel} to broadcast to
	 * 
	 * @param messages The messages to broadcast
	 */
	public static void broadcast(Channel channel, String... messages) {
		if (channel == null)
			return;
		
		List<String> lines = new ArrayList<String>();
		
		for (String message : messages)
			lines.addAll(Arrays.asList(ChatUtils.wordWrap(Format.colourise(message), 65)));
		
		for (Participant participant : channel.getParticipants())
			participant.sendMessage(lines.toArray(new String[0]));
	}
	
	/**
	 * Broadcasts the message to the area around the {@link CommandSender}
	 * 
	 * @param sender The {@link CommandSender}
	 * 
	 * @param radius The radius around the {@link CommandSender}
	 * 
	 * @param messages The messages to broadcast
	 */
	public static void broadcast(CommandSender sender, double radius, String... messages) {
		if (sender == null)
			return;
		
		if (!(sender instanceof Player))
			sendMessage(sender, messages);
		
		for (Entity entity : ((Player) sender).getNearbyEntities(radius, radius, radius))
			if (entity instanceof Player)
				sendMessage((Player) entity, messages);
	}
	
	/**
	 * Sends the message to the {@link CommandSender}
	 * 
	 * @param sender The {@link CommandSender} to broadcast to
	 * 
	 * @param messages The messages to broadcast
	 */
	public static void sendMessage(CommandSender sender, String... messages) {
		if (sender == null)
			return;
		
		List<String> lines = new ArrayList<String>();
		
		for (String message : messages)
			lines.addAll(Arrays.asList(ChatUtils.wordWrap(Format.colourise(message), 65)));
		
		sender.sendMessage(lines.toArray(new String[0]));
	}
}