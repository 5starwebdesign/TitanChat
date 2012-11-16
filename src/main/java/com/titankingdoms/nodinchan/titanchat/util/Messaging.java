package com.titankingdoms.nodinchan.titanchat.util;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;

public class Messaging {
	
	public static void broadcast(String... messages) {
		for (String message : messages)
			TitanChat.getInstance().getServer().broadcastMessage(message);
	}
	
	public static void broadcast(World world, String... messages) {
		for (Player player : world.getPlayers())
			player.sendMessage(messages);
	}
	
	public static void broadcast(Channel channel, String... messages) {
		channel.broadcast(messages);
	}
	
	public static void broadcast(CommandSender sender, double radius, String... messages) {
		if (!(sender instanceof Player))
			sender.sendMessage(messages);
		
		for (Entity entity : ((Player) sender).getNearbyEntities(radius, radius, radius))
			if (entity instanceof Player)
				((Player) entity).sendMessage(messages);
	}
	
	public static void sendMessage(CommandSender sender, String... messages) {
		sender.sendMessage(messages);
	}
}