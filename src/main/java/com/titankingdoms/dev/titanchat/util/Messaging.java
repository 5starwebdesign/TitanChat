package com.titankingdoms.dev.titanchat.util;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public class Messaging {
	
	public static void broadcast(String... messages) {
		for (String message : messages)
			TitanChat.getInstance().getServer().broadcastMessage(message);
	}
	
	public static void broadcast(World world, String... messages) {
		if (world == null)
			return;
		
		for (Player player : world.getPlayers())
			player.sendMessage(messages);
	}
	
	public static void broadcast(Channel channel, String... messages) {
		if (channel == null)
			return;
		
		for (Participant participant : channel.getParticipants())
			participant.sendMessage(messages);
	}
	
	public static void broadcast(CommandSender sender, double radius, String... messages) {
		if (sender == null)
			return;
		
		if (!(sender instanceof Player))
			sendMessage(sender, messages);
		
		for (Entity entity : ((Player) sender).getNearbyEntities(radius, radius, radius))
			if (entity instanceof Player)
				sendMessage((Player) entity, messages);
	}
	
	public static void sendMessage(CommandSender sender, String... messages) {
		if (sender == null)
			return;
		
		sender.sendMessage(messages);
	}
}