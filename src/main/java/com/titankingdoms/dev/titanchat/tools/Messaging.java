package com.titankingdoms.dev.titanchat.tools;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.api.Point;
import com.titankingdoms.dev.titanchat.tools.util.FormatUtils;

public final class Messaging {
	
	public static void sendNotice(CommandSender sender, String... messages) {
		if (sender == null || messages == null)
			return;
		
		String line = "[TitanChat] " + FormatUtils.colourise(StringUtils.join(messages, "\n&f[TitanChat] "), '&');
		sender.sendMessage(line);
	}
	
	public static void sendNotice(Point point, String... messages) {
		if (point == null || messages == null)
			return;
		
		String line = "[TitanChat] " + FormatUtils.colourise(StringUtils.join(messages, "\n&f[TitanChat] "), '&');
		point.sendRawLine(line);
	}
	
	public static void sendNotice(Player player, int radius, String... messages) {
		if (player == null || messages == null)
			return;
		
		String line = "[TitanChat] " + FormatUtils.colourise(StringUtils.join(messages, "\n&f[TitanChat] "), '&');
		
		for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
			if (!entity.getType().equals(EntityType.PLAYER))
				continue;
			
			((Player) entity).sendMessage(line);
		}
	}
	
	public static void sendNotice(World world, String... messages) {
		if (world == null || messages == null)
			return;
		
		String line = "[TitanChat] " + FormatUtils.colourise(StringUtils.join(messages, "\n&f[TitanChat] "), '&');
		
		for (Player player : world.getPlayers())
			player.sendMessage(line);
	}
	
	public static void sendNotice(String... messages) {
		if (messages == null)
			return;
		
		String line = "[TitanChat] " + FormatUtils.colourise(StringUtils.join(messages, "\n&f[TitanChat] "), '&');
		
		for (Player player : Bukkit.getOnlinePlayers())
			player.sendMessage(line);
	}
	
	public static void sendRawLine(CommandSender sender, String... messages) {
		if (sender == null || messages == null)
			return;
		
		sender.sendMessage(FormatUtils.colourise(StringUtils.join(messages, "\n"), '&'));
	}
	
	public static void sendRawLine(Point point, String... messages) {
		if (point == null || messages == null)
			return;
		
		point.sendRawLine(FormatUtils.colourise(StringUtils.join(messages, "\n"), '&'));
	}
	
	public static void sendRawLine(Player player, int radius, String... messages) {
		if (player == null || messages == null)
			return;
		
		String line = FormatUtils.colourise(StringUtils.join(messages, "\n"), '&');
		
		for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
			if (!entity.getType().equals(EntityType.PLAYER))
				continue;
			
			((Player) entity).sendMessage(line);
		}
	}
	
	public static void sendRawLine(World world, String... messages) {
		if (world == null || messages == null)
			return;
		
		String line = FormatUtils.colourise(StringUtils.join(messages, "\n"), '&');
		
		for (Player player : world.getPlayers())
			player.sendMessage(line);
	}
	
	public static void sendRawLine(String... messages) {
		if (messages == null)
			return;
		
		String line = FormatUtils.colourise(StringUtils.join(messages, "\n"), '&');
		
		for (Player player : Bukkit.getOnlinePlayers())
			player.sendMessage(line);
	}
}