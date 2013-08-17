package com.titankingdoms.dev.titanchat.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.core.EndPoint;

public final class Messaging {
	
	public static void sendMessage(CommandSender sender, String... messages) {
		if (sender == null || messages == null)
			return;
		
		sender.sendMessage(FormatUtils.colourise(StringUtils.join(messages, "\n"), '&'));
	}
	
	public static void sendMessage(EndPoint point, String... messages) {
		if (point == null || messages == null)
			return;
		
		point.sendNotice(FormatUtils.colourise(StringUtils.join(messages, "\n"), '&'));
	}
	
	public static void sendMessage(Player player, int radius, String... messages) {
		if (player == null || messages == null)
			return;
		
		for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
			if (!entity.getType().equals(EntityType.PLAYER))
				continue;
			
			((Player) entity).sendMessage(FormatUtils.colourise(StringUtils.join(messages, "\n"), '&'));
		}
	}
	
	public static void sendMessage(World world, String... messages) {
		if (world == null || messages == null)
			return;
		
		for (Player player : world.getPlayers())
			player.sendMessage(FormatUtils.colourise(StringUtils.join(messages, "\n"), '&'));
	}
}