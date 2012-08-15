/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.command.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.command.CommandBase;
import com.titankingdoms.nodinchan.titanchat.command.info.*;

/**
 * RankingCommand - Command for promotion, demotion and whitelisting on Channels
 * 
 * @author NodinChan
 *
 */
public class RankingCommand extends CommandBase {
	
	/**
	 * Demote Command - Demotes the player of the channel
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Description("Demotes the player of the channel")
	@Usage("demote [player] <player> <player>...")
	public void demote(CommandSender sender, Channel channel, String[] args) {
		if (channel.handleCommand(sender, "demote", args))
			return;
		
		if (args.length > 0) {
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.rank." + channel.getName())) {
				for (String name : args) {
					OfflinePlayer targetPlayer = plugin.getPlayer(name);
					
					if (isOffline(sender, name))
						targetPlayer = plugin.getOfflinePlayer(name);
					
					if (channel.getAdmins().contains(targetPlayer.getName())) {
						channel.getAdmins().remove(targetPlayer.getName());
						channel.save();
						
						if (targetPlayer.isOnline())
							plugin.send(MessageLevel.WARNING, targetPlayer.getPlayer(), "You have been demoted in " + channel.getName());
						
						if (sender instanceof Player && !channel.isParticipating(sender.getName()))
							plugin.send(MessageLevel.INFO, sender, getDisplayName(targetPlayer) + " has been demoted");
						
						plugin.send(MessageLevel.INFO, channel, getDisplayName(targetPlayer) + " has been demoted");
						
					} else { plugin.send(MessageLevel.WARNING, sender, getDisplayName(targetPlayer) + " is not an admin"); }
				}
				
			} else { plugin.send(MessageLevel.WARNING, sender, "You do not have permission"); }
			
		} else { invalidArgLength(sender, "demote"); }
	}
	
	/**
	 * Promote Command - Promotes the player of the channel
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Description("Promotes the player of the channel")
	@Usage("promote [player] <player> <player>...")
	public void promote(CommandSender sender, Channel channel, String[] args) {
		if (channel.handleCommand(sender, "promote", args))
			return;
		
		if (args.length > 0) {
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.rank." + channel.getName())) {
				for (String name : args) {
					OfflinePlayer targetPlayer = plugin.getPlayer(name);
					
					if (isOffline(sender, name))
						targetPlayer = plugin.getOfflinePlayer(name);
					
					if (!channel.getAdmins().contains(targetPlayer.getName())) {
						channel.getAdmins().add(targetPlayer.getName());
						channel.save();
						
						if (targetPlayer.isOnline())
							plugin.send(MessageLevel.INFO, targetPlayer.getPlayer(), "You have been promoted in " + channel.getName());
						
						if (sender instanceof Player && !channel.isParticipating(sender.getName()))
							plugin.send(MessageLevel.INFO, sender, getDisplayName(targetPlayer) + " has been promoted");
						
						plugin.send(MessageLevel.INFO, channel, getDisplayName(targetPlayer) + " has been promoted");
						
					} else { plugin.send(MessageLevel.WARNING, sender, getDisplayName(targetPlayer) + " is already an admin"); }
				}
				
			} else { plugin.send(MessageLevel.WARNING, sender, "You do not have permission"); }
			
		} else { invalidArgLength(sender, "promote"); }
	}
	
	/**
	 * Unwhitelist Command - Unwhitelists the player
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Aliases("remove")
	@Description("Unwhitelists the player")
	@Usage("unwhitelist [player] <player> <player>...")
	public void unwhitelist(CommandSender sender, Channel channel, String[] args) {
		if (channel.handleCommand(sender, "unwhitelist", args))
			return;
		
		if (args.length > 0) {
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.rank." + channel.getName())) {
				for (String name : args) {
					OfflinePlayer targetPlayer = plugin.getPlayer(name);
					
					if (isOffline(sender, name))
						targetPlayer = plugin.getOfflinePlayer(name);
					
					if (channel.getWhitelist().contains(targetPlayer.getName())) {
						channel.getWhitelist().remove(targetPlayer.getName());
						channel.save();
						
						if (targetPlayer.isOnline())
							plugin.send(MessageLevel.WARNING, targetPlayer.getPlayer(), "You have been dewhitelisted in " + channel.getName());
						
						if (sender instanceof Player && !channel.isParticipating(sender.getName()))
							plugin.send(MessageLevel.INFO, sender, getDisplayName(targetPlayer) + " has been dewhitelisted");
						
					} else { plugin.send(MessageLevel.WARNING, sender, getDisplayName(targetPlayer) + " is not whitelisted"); }
				}
				
			} else { plugin.send(MessageLevel.WARNING, sender, "You do not have permission"); }
			
		} else { invalidArgLength(sender, "unwhitelist"); }
	}
	
	/**
	 * Whitelist Command - Whitelists the player
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Aliases("add")
	@Description("Whitelists the player")
	@Usage("whitelist [player] <player> <player>...")
	public void whitelist(CommandSender sender, Channel channel, String[] args) {
		if (channel.handleCommand(sender, "whitelist", args))
			return;
		
		if (args.length > 0) {
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.rank." + channel.getName())) {
				for (String name : args) {
					OfflinePlayer targetPlayer = plugin.getPlayer(name);
					
					if (isOffline(sender, args[0]))
						targetPlayer = plugin.getOfflinePlayer(name);
					
					if (!channel.getWhitelist().contains(targetPlayer.getName())) {
						channel.getWhitelist().add(targetPlayer.getName());
						channel.save();
						
						if (targetPlayer.isOnline())
							plugin.send(MessageLevel.INFO, targetPlayer.getPlayer(), "You have been whitelisted in " + channel.getName());
						
						if (sender instanceof Player && !channel.isParticipating(sender.getName()))
							plugin.send(MessageLevel.INFO, sender, getDisplayName(targetPlayer) + " has been whitelisted");
						
					} else { plugin.send(MessageLevel.WARNING, sender, getDisplayName(targetPlayer) + " is already whitelisted"); }
				}
				
			} else { plugin.send(MessageLevel.WARNING, sender, "You do not have permission"); }
			
		} else { invalidArgLength(sender, "whitelist"); }
	}
}